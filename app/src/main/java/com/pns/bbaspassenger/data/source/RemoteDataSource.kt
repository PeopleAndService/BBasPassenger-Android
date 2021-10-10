package com.pns.bbaspassenger.data.source

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RemoteDataSource {
    private const val BBAS_URL = "http://172.18.3.23:8000/v1/pnsApp/"
    private const val BUS_OPEN_URL = "http://openapi.tago.go.kr/openapi/service/"

    private val gson = GsonBuilder().setLenient().create()

    private val bbasRetrofit = Retrofit.Builder()
        .baseUrl(BBAS_URL)
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val busRetrofit = Retrofit.Builder()
        .baseUrl(BUS_OPEN_URL)
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val bbasService: BBasService = bbasRetrofit.create(BBasService::class.java)
    val busService: BusService = busRetrofit.create(BusService::class.java)
}