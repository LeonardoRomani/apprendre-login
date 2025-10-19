package com.emiliano.apprendre_login.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = AuthTokenManager.getToken()

        println("DEBUG - AuthInterceptor: Solicitant URL: ${originalRequest.url()}")
        println("DEBUG - AuthInterceptor: Token disponible: ${!token.isNullOrBlank()}")

        val requestBuilder = originalRequest.newBuilder()

        // Afegir el token si esta disponible
        if (!token.isNullOrBlank()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
            println("DEBUG - AuthInterceptor: Header Authorization afegit")
        } else {
            println("DEBUG - AuthInterceptor: ERROR - No hi ha token disponible!")
        }

        // Afegir headers comuns
        requestBuilder.addHeader("Content-Type", "application/json")
        requestBuilder.addHeader("Accept", "application/json")

        val newRequest = requestBuilder.build()
        return chain.proceed(newRequest)
    }
}