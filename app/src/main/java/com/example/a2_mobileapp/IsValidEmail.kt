package com.example.a2_mobileapp

import android.util.Patterns
import androidx.core.util.PatternsCompat

fun String.isValidEmail() = PatternsCompat.EMAIL_ADDRESS.matcher(this).matches()

