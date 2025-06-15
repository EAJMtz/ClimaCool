package com.example.climacool.network

import com.example.climacool.core.RealtimeAPI
import com.example.climacool.core.ResultWrapper
import com.example.climacool.core.safeCall
import com.example.climacool.model.Weather
import retrofit2.HttpException

import javax.inject.Inject

class ClimaRepository @Inject constructor(
    private val realTimeAPI: RealtimeAPI



) {
    suspend fun getWeatherInfo(apiKey: String, location: String): ResultWrapper<Weather> = safeCall {
        val response = realTimeAPI.getWeatherInfo(apiKey, location)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Datos nulos en la respuesta.")
        } else {
            throw HttpException(response)
        }
    }
}