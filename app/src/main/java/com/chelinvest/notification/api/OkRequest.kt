package com.chelinvest.notification.api

import android.os.Build
import com.chelinvest.notification.utils.Constants.REQUEST_BODY
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.net.URLEncoder
import java.security.KeyStore
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

@Suppress("DEPRECATION")
class OkRequest private constructor() {
    companion object {
        private const val ENCODING = "windows-1251"
        private val mediaType =
            ("application/x-www-form-urlencoded; charset=$ENCODING").toMediaTypeOrNull()

        private var INSTANCE: OkRequest? = null
        fun getInstance(): OkRequest {
            val instance = INSTANCE ?: OkRequest()
            if (INSTANCE == null)
                INSTANCE = instance
            return instance
        }
    }

    private val client = enableTls12OnPreLollipop(OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .hostnameVerifier { _, _ -> true }
    ).build()

    private val longClient = enableTls12OnPreLollipop(OkHttpClient.Builder()
        .connectTimeout(70, TimeUnit.SECONDS)
        .readTimeout(70, TimeUnit.SECONDS)
        .writeTimeout(70, TimeUnit.SECONDS)
        .hostnameVerifier { _, _ -> true }
    ).build()


    private fun enableTls12OnPreLollipop(client: OkHttpClient.Builder): OkHttpClient.Builder {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            try {
                val sc = SSLContext.getInstance("TLSv1.2")
                sc.init(null, null, null)
                client.sslSocketFactory(TLSSocketFactory(sc.socketFactory), trustedCertificate())

                val cs = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build()

                val specs = ArrayList<ConnectionSpec>()
                specs.add(cs)
                specs.add(ConnectionSpec.COMPATIBLE_TLS)
                specs.add(ConnectionSpec.CLEARTEXT)

                client.connectionSpecs(specs)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return client
    }

    fun reset() {
        client.dispatcher.cancelAll()
    }

    fun request(
        serverUrl: String,
        query: String,
        onCallCreated: (call: Call) -> Unit,
        isLong: Boolean
    ): OkResponse {
        val body = String.format(REQUEST_BODY, URLEncoder.encode(query, ENCODING))

        val request = Request.Builder()
                .url(serverUrl)
                .post(RequestBody.create(mediaType, body))
                .build()

        val call = if (isLong) longClient.newCall(request) else client.newCall(request)
        onCallCreated(call)
        val response = call.execute()

        return OkResponse().apply {
            this.errorCode = response.code
            this.errorMessage = response.message
            this.isSuccessful = response.isSuccessful
            this.body = response.body?.string()
        }
    }

    fun get(url: String): OkResponse {
        val request = Request.Builder()
                .url(url)
                .get()
                .build()

        val response = client.newCall(request).execute()

        return OkResponse().apply {
            this.errorCode = response.code
            this.errorMessage = response.message
            this.isSuccessful = response.isSuccessful
            this.body = response.body?.string()
            this.cookies = response.headers("Set-Cookie")
        }
    }

    private fun trustedCertificate(): X509TrustManager {
        val trustManagerFactory: TrustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(null as KeyStore?)
        val trustManagers: Array<TrustManager> = trustManagerFactory.trustManagers
        check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
            "Unexpected default trust managers:" + trustManagers.contentToString()
        }
        val trustManager: X509TrustManager = trustManagers[0] as X509TrustManager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(trustManager), null)
        return trustManager
    }

}
