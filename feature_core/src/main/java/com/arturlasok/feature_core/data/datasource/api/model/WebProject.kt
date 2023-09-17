package com.arturlasok.feature_core.data.datasource.api.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable


@Serializable
data class WebProject(
    @Contextual
    val _id:Any? = null,
    val wProject_id: Long? = null,
    val wProject_title: String = "",
    val wProject_address: String = "",
    val wProject_mail: String = "",
    val wProject_added: Long = 0L,
    val wProject_edited: Long = 0L,
    val wProject_lang: String = "",
    val wProject_country: String = "",
    val wProject_sync: Long = 0L,
    val wProject_type: String = "",
    val wProject_category: String = "",
    val wProject_token: String = "",
    val wProject_premium: Long = 0L,
    val wProject_enabled: Boolean = false,
)
