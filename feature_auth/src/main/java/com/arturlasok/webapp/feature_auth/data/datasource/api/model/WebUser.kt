package com.arturlasok.webapp.feature_auth.data.datasource.api.model

import kotlinx.serialization.Serializable

@Serializable
data class WebUser(
    val webUserKey : String? = null,
    val webUserToken: String? = null,
    val webUserBlocked: Boolean = false,
    val webUserMail: String = "",
    val webVerified: Boolean = false,
    val webLastLog: String = "",
    val webReg: String = ""
)
