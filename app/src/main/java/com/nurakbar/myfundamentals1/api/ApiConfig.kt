package com.nurakbar.myfundamentals1.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        // Memasukkan Token Github User agar tak memasukkan token pada ApiService
        private fun AuthIntercept(): Interceptor {
            return Interceptor { chain ->
                val req = chain.request()
                val headers = req.newBuilder()
                    .addHeader("Authorization", "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")
                    .build()
                chain.proceed(headers)
            }
        }

        // Mengambil ApiService
        fun getApiService(): ApiService {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}