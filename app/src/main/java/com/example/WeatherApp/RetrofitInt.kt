package com.example.WeatherApp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInt {

    fun getInstance(baseURL:String): Retrofit{
        val retrofitInstance =  Retrofit.Builder()
                            .baseUrl(baseURL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
        return retrofitInstance
    }
}