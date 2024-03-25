package com.example.WeatherApp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//citation:- https://www.youtube.com/watch?v=6Z_lTWKy1lg
@Database(entities = [WeatherInfo::class],version = 1)
abstract class weatherDatabase: RoomDatabase() {
    abstract fun weatherDao(): weatherDao

    companion object{
        @Volatile
        private var DbInstance: weatherDatabase? = null

        fun getDbInstance(context: Context): weatherDatabase{
            if(DbInstance != null){
                return DbInstance as weatherDatabase
            }

            synchronized(this){
                DbInstance = Room.databaseBuilder(context.applicationContext,
                    weatherDatabase::class.java,
                    "weatherDB").build()
            }

            return DbInstance!!
        }
    }
}