package com.example.WeatherApp

data class WeatherDataX(
    val daily: Daily,
    val daily_units: DailyUnits,
    val elevation: Double,
    val generationtime_ms: Double,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Int
)