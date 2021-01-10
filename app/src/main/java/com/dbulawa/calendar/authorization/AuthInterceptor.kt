package com.dbulawa.calendar.authorization

import android.os.AsyncTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor (
    var authService: GoogleAuthService
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // If token has been saved, add it to the request
        authService.handleSignInResult()?.let {
            val token = it.token
            println("Added token: " + token)
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }


        return chain.proceed(requestBuilder.build())
    }
}