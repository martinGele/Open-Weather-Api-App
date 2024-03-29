package com.martin.openweatherapi.vo

import com.google.gson.annotations.SerializedName

data class Weather(
   @SerializedName("coord")
   val coord: Coord = Coord(),
   @SerializedName("weather")
   val weather: List<WeatherX> = listOf(),
   @SerializedName("base")
   val base: String = "",
   @SerializedName("main")
   val main: Main = Main(),
   @SerializedName("wind")
   val wind: Wind = Wind(),
   @SerializedName("clouds")
   val clouds: Clouds = Clouds(),
   @SerializedName("dt")
   val dt: Int = 0,
   @SerializedName("sys")
   val sys: Sys = Sys(),
   @SerializedName("timezone")
   val timezone: Int = 0,
   @SerializedName("id")
   val id: Int = 0,
   @SerializedName("name")
   val name: String = "",
   @SerializedName("cod")
   val cod: Int = 0
)