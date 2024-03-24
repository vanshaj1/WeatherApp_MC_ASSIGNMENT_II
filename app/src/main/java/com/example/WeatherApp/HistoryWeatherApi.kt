package com.example.WeatherApp

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HistoryWeatherApi {
    @GET("/v1/archive")
    suspend fun getWeatherStatus(@Query("latitude") latitude: Double, @Query("longitude") longitude: Double, @Query("daily") daily:String, @Query("daily") daily2: String, @Query("start_date") start_date:String, @Query("end_date") end_date:String): Response<WeatherDataX>
}