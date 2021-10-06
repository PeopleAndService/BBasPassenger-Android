package com.pns.bbaspassenger.data.source

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RemoteDataSource {
    private const val BBAS_URL = "http://172.18.3.23:8000/v1/pnsApp/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BBAS_URL)
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val bbasService: ApiService = retrofit.create(ApiService::class.java)
}