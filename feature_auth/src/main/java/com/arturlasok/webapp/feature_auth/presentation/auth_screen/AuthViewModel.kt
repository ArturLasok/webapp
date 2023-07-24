package com.arturlasok.webapp.feature_auth.presentation.auth_screen

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.arturlasok.feature_core.util.isOnline
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val isOnline: isOnline


) : ViewModel() {



    val applications = mutableStateOf(application)
    fun haveNetwork() : Boolean {
        return isOnline.isNetworkAvailable.value
    }

}