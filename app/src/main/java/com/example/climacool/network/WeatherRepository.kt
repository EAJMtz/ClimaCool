package com.example.climacool.network

import com.example.climacool.core.RealtimeAPI
import com.example.climacool.core.ResultWrapper
import com.example.climacool.core.safeCall
import com.example.climacool.model.ForecastResponse
import retrofit2.HttpException
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val realTimeAPI: RealtimeAPI
) {
    suspend fun getForecast(apiKey: String, location: String): ResultWrapper<ForecastResponse> = safeCall {
        val response = realTimeAPI.getForecastInfo(apiKey, location)
        //Log.d("API Response", "Días recibidos en repositorio: ${response.body()?.forecast?.forecastday?.size}")
        //Log.d("API Response", "Codigo: ${response.code()}, JSON: ${Gson().toJson(response.body())}")
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Datos nulos")
        } else {
            throw HttpException(response)
        }

    }
}