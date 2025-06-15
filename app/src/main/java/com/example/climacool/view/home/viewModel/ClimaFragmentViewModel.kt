package com.example.climacool.view.home.viewModel

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climacool.core.LocationProvider
import com.example.climacool.core.ResultWrapper
import com.example.climacool.model.Weather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import com.example.climacool.network.ClimaRepository

@HiltViewModel
class ClimaFragmentViewModel @Inject constructor(
    private val repository: ClimaRepository,
    private val locationProvider: LocationProvider
): ViewModel() {

    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean> get() = _loaderState

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _weatherInfo = MutableLiveData<Weather>()
    val weatherInfo: LiveData<Weather> get() = _weatherInfo



    fun fetchWeather(apiKey: String) {
        _loaderState.value = true
        viewModelScope.launch {

            val hardcodedCoordinates = "19.303710263763854,-99.05872923057291" // Latitud,Longitud fijas

            when (val result = repository.getWeatherInfo(apiKey, hardcodedCoordinates)) {
                is ResultWrapper.Success -> {
                    _loaderState.value = false
                    _weatherInfo.value = result.data
                }
                is ResultWrapper.Error -> {
                    _loaderState.value = false
                    _errorMessage.value = "Error: ${result.exception.message}"
                }
            }
        }
    }

    fun formatLocalTime(localTime: String): String {
        // Parsear la fecha original
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val date = inputFormat.parse(localTime) ?: return localTime // fallback si hay error

        // Formatear el día de la semana
        val dayFormat = SimpleDateFormat("EEEE", Locale.ENGLISH)
        val day = dayFormat.format(date).uppercase()

        // Formatear la hora
        val timeFormat = SimpleDateFormat("h:mm a", Locale.ENGLISH)
        val time = timeFormat.format(date).uppercase()

        return "$day $time"
    }

}