package com.martin.openweatherapi.api


import com.martin.openweatherapi.vo.Weather
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {


    @GET("/data/2.5/weather?")
    fun getWeatherGeo(@Query("lat") lat: String, @Query("lon") lon: String, @Query("appid") id: String): Single<Weather>

    @GET("/data/2.5/weather?")
    fun getWeatherCity(@Query("q") city: String, @Query("appid") id: String): Single<Weather>

}