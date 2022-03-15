package ics340kyang.weatherapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("weather")
    suspend fun getCurrentConditions(
        @Query("zip") zip: String,
        @Query("units") units: String = "imperial",
        @Query("appid") appId: String = "bb04a900b609b9b072402a95ab92a33c",
    ): CurrentConditions

    @GET("forecast/daily")
    suspend fun getForecast(
        @Query("zip") zip: String,
        @Query("units") units: String = "imperial",
        @Query("cnt") cnt: String = "16",
        @Query("appid") appId: String = "bb04a900b609b9b072402a95ab92a33c",
    ): ForecastList

}