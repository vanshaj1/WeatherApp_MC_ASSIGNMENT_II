package com.example.WeatherApp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInt {

    private var baseURL = "https://api.open-meteo.com/"
    fun getInstance(): Retrofit{
        val retrofitInstance =  Retrofit.Builder()
                            .baseUrl(baseURL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
        return retrofitInstance
    }
}