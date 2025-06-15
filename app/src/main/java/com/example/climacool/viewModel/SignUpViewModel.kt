package com.example.climacool.viewModel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climacool.core.ResultWrapper
import com.example.climacool.network.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel() {
    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean>
        get() = _loaderState

    private val _registrationSuccess = MutableLiveData<Boolean>()
    val registrationSuccess: LiveData<Boolean>
        get() = _registrationSuccess

    private val _isUserCreted = MutableLiveData<String>()
    val isUserCreted: LiveData<String>
        get() = _isUserCreted

    private val firebase = FirebaseAuth.getInstance()

    fun requestSignUp(email: String?, password: String?) {
        val trimmedEmail = email?.trim() ?: ""
        if (!Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches() || password.isNullOrEmpty()) {
            _loaderState.value = false
            _registrationSuccess.value = false
            Log.e("Firebase", "Correo o contraseña inválidos")
            return
        }

        _loaderState.value = true
        viewModelScope.launch {
            try {
                when (val result = repository.requestSignUp(trimmedEmail, password)) {
                    is ResultWrapper.Success -> {
                        _isUserCreted.value = result.data.uid ?: "Usuario desconocido"
                        _registrationSuccess.value = true
                        Log.i("Firebase", "Usuario registrado exitosamente")
                    }
                    is ResultWrapper.Error -> {
                        Log.e("Firebase", "Error: No se pudo crear el usuario")
                        _registrationSuccess.value = false
                    }
                }
            } catch (e: Exception) {
                Log.e("Firebase", "Excepción inesperada: ${e.message}")
                _registrationSuccess.value = false
            } finally {
                _loaderState.value = false // ✅ Garantizado que se apague
            }
        }
    }
}