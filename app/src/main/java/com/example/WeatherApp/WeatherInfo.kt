package com.example.WeatherApp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weatherInfo")
data class WeatherInfo(

    @PrimaryKey
    val date: String,
    val temperatureMin: String,
    val temperatureMax: String
)
