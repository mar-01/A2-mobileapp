package com.example.a2_mobileapp


import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MainActivityTest {

    @Test
    fun isValidEmail() {
        val validEmail = "test@example.com"
        assert(validEmail.isValidEmail())
    }
}
