package com.chelinvest.notification.additional

import android.util.Patterns

fun isEmailValid(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

fun isPhoneValid(phone: String): Boolean = Patterns.PHONE.matcher(phone).matches()