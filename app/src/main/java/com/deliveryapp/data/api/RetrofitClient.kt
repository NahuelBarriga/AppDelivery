package com.deliveryapp.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi

@UnstableApi
object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:3003/api/"
    //"https://pedidos-service-api.thankfulwave-605d56a6.brazilsouth.azurecontainerapps.io/"


    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}