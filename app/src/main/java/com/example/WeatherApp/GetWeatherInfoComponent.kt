package com.example.WeatherApp

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.annotations.Nullable
import retrofit2.create
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

//Citation:- https://medium.com/mobile-app-development-publication/date-and-time-picker-with-compose-9cadc4f50e6d
@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@Composable
fun GetWeatherInfoComponent(modifier: Modifier){
    val context = LocalContext.current
    val state = rememberDatePickerState()
    var makeVisible = remember{ mutableStateOf(false) }
    var date = state.selectedDateMillis?.let {
        getLocalDateFromMillis(it)
    }?: ""
    var minimumTemperature = remember {
        mutableStateOf("0.0")
    }
    var maximumTemperature = remember {
        mutableStateOf("0.0")

    }
    var animationVisibility = remember {
        MutableTransitionState(false).apply { targetState = false }
    }

    Column (
        modifier
            .background(
                Color.White
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        //    citetation:- https://stackoverflow.com/a/68640459
            AnimatedVisibility(visibleState = animationVisibility,
                enter = fadeIn(
                    animationSpec = tween(durationMillis = 2000)
                ),
                exit = fadeOut(
                    animationSpec = tween(durationMillis = 2000)
                )) {
                   Text(text = "Minimum temperature is :- ${minimumTemperature.value} \n Maximum temperature is :- ${maximumTemperature.value}"
                    ,style= TextStyle(
                        background = Color(android.graphics.Color.parseColor("#A92EF5")),
                            color = Color.White
                    )
                    )
            }


        if(makeVisible.value == true){
            DatePickerDialog(onDismissRequest = {
                   makeVisible.value = false                                     
            }, confirmButton = {
                Button(onClick = {
                    makeVisible.value = false
                }) {
                    Text(text = "confirm")
                }
            }, dismissButton = {
                Button(onClick = {
                    makeVisible.value = false
                }) {
                    Text(text = "Cancel")                    
                }
            }
            ) {
                DatePicker(state = state)
            }
        }
        TextButton(onClick = {
            makeVisible.value = true
            animationVisibility.targetState = false
        },
            border = BorderStroke(2.dp, Color.Blue),
            shape = RoundedCornerShape(0),
        ) {
            Text(text= if(date == ""){
                "yyyy-MM-dd"
            }else{
                date
            },
                modifier = modifier.padding(end = 8.dp))
            Icon(
               Icons.Rounded.DateRange,
               contentDescription = ""
            )
        }
        Button(onClick = {
            val api = RetrofitInt.getInstance().create(weatherApi::class.java)
            val currYear = SimpleDateFormat("yyyy-MM-dd").format(Date()).split("-")[0].toInt()
            val askedYear = date.split("-")[0].toInt()
            val previousYearDate = currYear.toString() + date.substring(4)
            val previousTenYearDate = (currYear - 9).toString() + date.substring(4)
            Log.d("testy ","test "+previousYearDate + " "+previousTenYearDate)
            if(currYear + 1 < askedYear){
                Toast.makeText(
                    context,
                    "We cannot show weather status for this year",
                    Toast.LENGTH_SHORT
                ).show()
            }

            if(currYear == askedYear){
                GlobalScope.launch {
                    val result = api.getWeatherStatus(28.6519,77.2315,"temperature_2m_max","temperature_2m_min",date,date)
                    if(result.isSuccessful){
                        minimumTemperature.value = result.body()?.daily?.temperature_2m_min?.get(0).toString()
                        maximumTemperature.value = result.body()?.daily?.temperature_2m_max?.get(0).toString()
                        animationVisibility.targetState = true
                    }
                }
            }
            if(currYear + 1 == askedYear){
                GlobalScope.launch {
                    val result = api.getWeatherStatus(28.6519,77.2315,"temperature_2m_max","temperature_2m_min",previousTenYearDate,previousYearDate)
                    Log.d("vanshaj",result.isSuccessful.toString() + " "+ result.body().toString())
                    if(result.isSuccessful){
                        minimumTemperature.value = result.body()?.daily?.temperature_2m_min?.average().toString()
                        maximumTemperature.value = result.body()?.daily?.temperature_2m_max?.average().toString()
                        animationVisibility.targetState = true
                    }
                }
            }

        }) {
            Text(text="Temperature Status")
        }
    }
}

//citetation:- https://medium.com/@rahulchaurasia3592/material3-datepicker-and-datepickerdialog-in-compose-in-android-54ec28be42c3
private fun getLocalDateFromMillis(millis: Long): String {
    val FormatObj = SimpleDateFormat("yyyy-MM-dd")
    return FormatObj.format(Date(millis))
}

@Preview
@Composable
fun DisplayGetWeatherInfoComponent(){
    GetWeatherInfoComponent(Modifier)
}