package com.example.WeatherApp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import java.util.Date

@Dao
interface weatherDao{
    @Upsert
    suspend fun insertWeatherInfo(weatherInfo: WeatherInfo)

    @Query("SELECT * FROM weatherInfo")
    fun getAllWeatherInfo(): LiveData<List<WeatherInfo>>

    @Query("SELECT date,temperatureMax,temperatureMin FROM weatherInfo where date = :date")
    suspend fun getWeatherInfoByDate(date: String): WeatherInfo

}