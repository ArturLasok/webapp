package com.arturlasok.feature_core.data.datasource.api.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class WebDomains(
    @Contextual
    val _id:Any? = null,
    val wDomain_id: Long? = null,
    val wDomain_address: String ="",
    val wDomain_added: Long = 0L,
    val wDomain_enabled: Boolean = false,
    val wDomain_country: String = "@all",

)
