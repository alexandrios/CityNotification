package com.chelinvest.notification.ui

interface RequestListener {
    fun onRequestFailure(message: String, checkOffline: Boolean = false)
}