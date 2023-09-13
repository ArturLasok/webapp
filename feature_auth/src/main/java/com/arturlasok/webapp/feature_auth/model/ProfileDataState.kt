package com.arturlasok.webapp.feature_auth.model

import androidx.compose.runtime.MutableState

data class ProfileDataState(
    val profileMail: String = "",
    val profileVerified : Boolean = false,
    val profileFirstLogin : Boolean = true,
    val profileCountry: String = "",
    val profileLang: String = "",
    val profileVerificationInteractionState : MutableState<ProfileInteractionState>,
    val profileInfoInteractionState: MutableState<ProfileInteractionState>,

)
