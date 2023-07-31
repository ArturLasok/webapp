package com.arturlasok.webapp.feature_auth.model

data class AuthLoginDataState(
    val authLogin : String = "",
    val authPassword : String = "",
    val authPasswordRepeat : String = "",
    val authPasswordVisibility : Boolean = false,
    val authRememberUser: Boolean = false
)
