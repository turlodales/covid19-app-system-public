package uk.nhs.nhsx.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class CtaTokenTest {

    @Test
    fun `create valid ctaToken`() {
        assertThat(CtaToken.of("cc8f0b6z").value).isEqualTo("cc8f0b6z")
    }

    @Test
    fun `throw exception when invalid ctaToken`() {
        assertThatThrownBy { CtaToken.of("invalid-token") }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Validation failed for: (invalid-token)")
    }
}
