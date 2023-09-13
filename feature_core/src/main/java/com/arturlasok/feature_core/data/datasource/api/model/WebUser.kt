package com.arturlasok.feature_core.data.datasource.api.model

import kotlinx.serialization.Serializable

@Serializable
data class WebUser(
    val webUserKey : String? = null,
    val webUserToken: String? = null,
    val webUserBlocked: Boolean = false,
    val webUserMail: String = "",
    val webVerified: Boolean = false,
    val webLastLog: String = "",
    val webReg: String = "",
    val webSimCountry: String = "",
    val webUserLang: String = ""
)
