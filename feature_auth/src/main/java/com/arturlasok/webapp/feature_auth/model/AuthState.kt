package com.arturlasok.webapp.feature_auth.model

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    object MailErrors : AuthState()
    class AuthError(val message: String? = null) : AuthState()
}