package com.arturlasok.webapp.feature_auth.model

data class ProfileDataState(
    val profileMail: String = "",
    val profileVerified : Boolean = false,
    val profileFirstLogin : Boolean = true,
)
