package uk.nhs.nhsx.keyfederation.upload

import com.amazonaws.services.lambda.runtime.events.ScheduledEvent
import com.amazonaws.services.s3.model.S3ObjectSummary
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import org.assertj.core.api.Assertions.assertThat
import org.jose4j.jws.JsonWebSignature
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.skyscreamer.jsonassert.Customization
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.skyscreamer.jsonassert.comparator.CustomComparator
import uk.nhs.nhsx.core.Json
import uk.nhs.nhsx.core.TestEnvironments
import uk.nhs.nhsx.core.aws.s3.BucketName
import uk.nhs.nhsx.core.aws.secretsmanager.SecretName
import uk.nhs.nhsx.core.aws.ssm.ParameterName
import uk.nhs.nhsx.core.events.RecordingEvents
import uk.nhs.nhsx.domain.ReportType
import uk.nhs.nhsx.domain.TestType
import uk.nhs.nhsx.keyfederation.InMemoryBatchTagService
import uk.nhs.nhsx.keyfederation.InteropClient
import uk.nhs.nhsx.keyfederation.TestKeyPairs
import uk.nhs.nhsx.testhelper.ContextBuilder
import uk.nhs.nhsx.testhelper.mocks.FakeInteropDiagnosisKeysS3
import uk.nhs.nhsx.testhelper.wiremock.WireMockExtension
import java.time.Instant
import java.util.*

@ExtendWith(WireMockExtension::class)
class KeyFederationUploadHandlerVerifyPayloadTest(private val wireMock: WireMockServer) {

    private val events = RecordingEvents()
    private val now = Instant.parse("2020-02-05T10:00:00.000Z")
    private val submissionDate = Instant.parse("2020-02-04T10:00:00.000Z")

    @Test
    fun `upload exposure keys uses correct test type and report type`() {
        wireMock.stubFor(
            WireMock.post("/diagnosiskeys/upload")
                .withHeader("Authorization", WireMock.equalTo("Bearer DUMMY_TOKEN"))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(200)
                        .withBody(
                            """
                            {
                                "batchTag": "75b326f7-ae6f-42f6-9354-00c0a6b797b3",
                                "insertedExposures":0
                            }
                            """.trimIndent()
                        )
                )
        )

        val fakeS3 = FakeInteropDiagnosisKeysS3(listOf(
            S3ObjectSummary().apply {
                bucketName = "SUBMISSION_BUCKET"
                key = "mobile/LAB_RESULT/abc"
                lastModified = Date.from(submissionDate)
            },
            S3ObjectSummary().apply {
                bucketName = "SUBMISSION_BUCKET"
                key = "mobile/RAPID_RESULT/def"
                lastModified = Date.from(submissionDate)
            },
            S3ObjectSummary().apply {
                bucketName = "SUBMISSION_BUCKET"
                key = "mobile/RAPID_SELF_REPORTED/ghi"
                lastModified = Date.from(submissionDate)
            }
        ))

        val handler = KeyFederationUploadHandler(
            TestEnvironments.environmentWith(),
            { now },
            RecordingEvents(),
            BucketName.of("SUBMISSION_BUCKET"),
            KeyFederationUploadConfig(
                100,
                14,
                0,
                { true },
                false,
                -1,
                wireMock.baseUrl(),
                SecretName.of("authToken"),
                ParameterName.of("parameter"),
                "DUMMY_TABLE",
                "GB-EAW",
                emptyList()
            ),
            batchTagService = InMemoryBatchTagService(),
            interopClient = InteropClient(
                wireMock.baseUrl(),
                "DUMMY_TOKEN",
                JWS(KmsCompatibleSigner(TestKeyPairs.ecPrime256r1.private)),
                events
            ),
            awsS3Client = fakeS3
        )

        handler.handleRequest(ScheduledEvent(), ContextBuilder.TestContext())

        val events = wireMock.allServeEvents

        assertThat(events).hasSize(1)

        val request = Json.readJsonOrThrow<DiagnosisKeysUploadRequest>(events[0].request.bodyAsString)
        val jws = JsonWebSignature().apply {
            key = TestKeyPairs.ecPrime256r1.public
            compactSerialization = request.payload
        }

        val expectedPayLoad = Json.toJson(
            listOf(
                ExposureUpload(
                    keyData = fakeS3.getEncodedKeyData(),
                    rollingStartNumber = 2696400,
                    transmissionRiskLevel = 7,
                    rollingPeriod = 144,
                    regions = listOf("GB-EAW"),
                    testType = TestType.LAB_RESULT,
                    reportType = ReportType.CONFIRMED_TEST,
                    daysSinceOnset = 0
                ), ExposureUpload(
                    keyData = fakeS3.getEncodedKeyData(),
                    rollingStartNumber = 2696400,
                    transmissionRiskLevel = 7,
                    rollingPeriod = 144,
                    regions = listOf("GB-EAW"),
                    testType = TestType.RAPID_RESULT,
                    reportType = ReportType.UNKNOWN,
                    daysSinceOnset = 0
                ), ExposureUpload(
                    keyData = fakeS3.getEncodedKeyData(),
                    rollingStartNumber = 2696400,
                    transmissionRiskLevel = 7,
                    rollingPeriod = 144,
                    regions = listOf("GB-EAW"),
                    testType = TestType.RAPID_SELF_REPORTED,
                    reportType = ReportType.UNKNOWN,
                    daysSinceOnset = 0
                )
            )
        )

        JSONAssert.assertEquals(expectedPayLoad, jws.payload, CustomComparator(JSONCompareMode.LENIENT, Customization("[*].rollingStartNumber") { _, _ -> true }))
    }

}
