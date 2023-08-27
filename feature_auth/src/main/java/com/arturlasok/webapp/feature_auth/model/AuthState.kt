package com.arturlasok.webapp.feature_auth.model

sealed class AuthState {
    object Idle : AuthState()
    object DbSync : AuthState()
    object DbSyncError: AuthState()
    object Success : AuthState()
    class AuthError(val message: String? = null) : AuthState()
}