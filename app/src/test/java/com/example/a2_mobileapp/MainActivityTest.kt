package com.example.a2_mobileapp

import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class MainActivityTest {

    private lateinit var mainActivity: MainActivity

    @Before
    fun setUp() {
        mainActivity = MainActivity()
    }

    @Test
    fun isValidEmail() {
        val validEmail = "test@example.com"
        assert(mainActivity.isValidEmail(validEmail))
    }
}
