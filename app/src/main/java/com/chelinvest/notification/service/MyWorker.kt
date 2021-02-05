package com.chelinvest.notification.service

import android.content.Context
import android.util.Log
import androidx.work.ListenableWorker

import androidx.work.Worker
import androidx.work.WorkerParameters
import com.chelinvest.notification.utils.Constants.LOG_TAG

class MyWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): ListenableWorker.Result {
        Log.d(LOG_TAG, "Performing long running task in scheduled job")
        // TODO(developer): add long running task here.
        return ListenableWorker.Result.success()
    }
}