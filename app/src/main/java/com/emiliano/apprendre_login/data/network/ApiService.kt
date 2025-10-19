package com.emiliano.apprendre_login.api

import com.emiliano.apprendre_login.data.model.LoginRequest
import com.emiliano.apprendre_login.data.model.LoginResponse
import com.emiliano.apprendre_login.data.model.RegisterRequest
import com.emiliano.apprendre_login.data.model.User
import com.emiliano.apprendre_login.data.model.UserResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Path

// Interface que defineix les crides a l'API mitjançant Retrofit
interface ApiService {


    @GET("user/me")
    suspend fun getCurrentUser(): Response<User>
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("user/create-user")
    suspend fun register(@Body request: RegisterRequest): Response<String>

    @GET("user/all")
    suspend fun getAllUsers(): Response<UserResponse> // Canvia a UserResponse
    @DELETE("user/{user_id}")
    suspend fun deleteUser(@Path("user_id") userId: Int?): Response<String>

    @PUT("user/")
    suspend fun updateUser(@Body updateRequest: Map<String, Any>): Response<String>

    @POST("auth/logout")
    suspend fun logout(): Response<String>

    companion object {
        fun create(): ApiService {
            // Configurar OkHttpClient amb l'interceptor
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor())
                .build()

            return Retrofit.Builder()
                .baseUrl("https://apprendre-servidor.onrender.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}


