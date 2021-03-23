package smoke.env

data class EnvConfig(
    val workspace_name: String,
    val analytics_events_submission_endpoint: String,
    val analytics_events_submission_function_name: String,
    val analytics_events_submission_health_endpoint: String,
    val analytics_events_submission_store: String,
    val analytics_submission_ingestion_interval: String,
    val analytics_submission_endpoint: String,
    val analytics_submission_health_endpoint: String,
    val analytics_submission_store: String,
    val analytics_submission_parquet_store: String,
    val availability_android_distribution_endpoint: String,
    val availability_android_distribution_store: String,
    val availability_ios_distribution_endpoint: String,
    val availability_ios_distribution_store: String,
    val base_distribution_endpoint: String,
    val exposure_notification_circuit_breaker_analytics_lambda_function_name: String,
    val diagnosis_keys_distribution_2hourly_endpoint: String,
    val diagnosis_keys_distribution_daily_endpoint: String,
    val diagnosis_keys_distribution_store: String,
    val diagnosis_keys_processing_function: String,
    val diagnosis_keys_submission_endpoint: String,
    val diagnosis_keys_submission_health_endpoint: String,
    val diagnosis_keys_submission_store: String,
    val exposure_configuration_distribution_endpoint: String,
    val exposure_configuration_distribution_store: String,
    val exposure_notification_circuit_breaker_endpoint: String,
    val exposure_notification_circuit_breaker_health_endpoint: String,
    val federation_keys_download_analytics_lambda_function_name: String,
    val federation_keys_upload_analytics_lambda_function_name: String,
    val federation_keys_processing_upload_function: String,
    val federation_keys_processing_download_function: String,
    val isolation_payment_consume_lambda_function_name: String,
    val isolation_payment_create_endpoint: String,
    val isolation_payment_health_endpoint: String,
    val isolation_payment_consume_endpoint: String,
    val isolation_payment_verify_endpoint: String,
    val isolation_payment_order_lambda_function_name: String,
    val isolation_payment_tokens_table: String,
    val isolation_payment_update_endpoint: String,
    val isolation_payment_verify_lambda_function_name: String,
    val post_districts_distribution_endpoint: String,
    val post_districts_distribution_store: String,
    val risky_post_districts_upload_endpoint: String,
    val risky_post_districts_upload_gateway_endpoint: String,
    val risky_post_districts_upload_health_endpoint: String,
    val risky_venue_configuration_distribution_endpoint: String,
    val risky_venue_configuration_distribution_store: String,
    val risky_venues_circuit_breaker_endpoint: String,
    val risky_venues_circuit_breaker_health_endpoint: String,
    val risky_venues_distribution_endpoint: String,
    val risky_venues_distribution_store: String,
    val risky_venues_upload_endpoint: String,
    val risky_venues_upload_health_endpoint: String,
    val self_isolation_distribution_endpoint: String,
    val self_isolation_distribution_store: String,
    val symptomatic_questionnaire_distribution_endpoint: String,
    val symptomatic_questionnaire_distribution_store: String,
    val test_results_health_endpoint: String,
    val test_results_npex_upload_endpoint: String,
    val test_results_fiorano_upload_endpoint: String,
    val test_results_eng_tokengen_upload_endpoint: String,
    val test_results_wls_tokengen_upload_endpoint: String,
    val test_results_v2_npex_upload_endpoint: String,
    val test_results_v2_fiorano_upload_endpoint: String,
    val test_results_v2_eng_tokengen_upload_endpoint: String,
    val test_results_v2_wls_tokengen_upload_endpoint: String,
    val virology_kit_endpoint: String,
    val virology_kit_health_endpoint: String,
    val virology_table_results: String,
    val virology_table_submission_tokens: String,
    val virology_table_test_orders: String,
    val virology_submission_lambda_function_name: String,
    val virology_tokens_processing_function: String,
    val virology_tokens_processing_output_store: String,
    val virology_tokens_processing_sms_topic_arn: String,
    val virology_tokens_processing_email_topic_arn: String,
    val virology_upload_lambda_function_name: String,
    val auth_headers: AuthHeaders,
    val empty_submission_endpoint: String,
    val empty_submission_v2_endpoint: String
)

data class AuthHeaders(
    val mobile: String,
    val testResultUpload: String,
    val highRiskVenuesCodeUpload: String,
    val highRiskPostCodeUpload: String,
    val isolationPayment: String,
    val health: String
)
