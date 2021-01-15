package com.chelinvest.notification.data.remote

import kotlinx.coroutines.*
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val remoteService: RemoteService
) {
    fun getSession(user: String, pass: String) = remoteService.getSession(user, pass)

}