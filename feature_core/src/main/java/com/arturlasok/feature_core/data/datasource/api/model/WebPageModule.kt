package com.arturlasok.feature_core.data.datasource.api.model

import com.arturlasok.feature_core.util.AnySerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class WebPageModule(
    @Contextual
    @Serializable(with = AnySerializer::class)
    val _id: Any? = null,
    @Contextual
    @Serializable(with = AnySerializer::class)
    val wProjectId: Any? = null,
    val wRouteToken:String = "",
    @Contextual
    @Serializable(with = AnySerializer::class)
    val wModuleId: Any? = null,
    val wPageModuleType: String = "",
    val wPageModuleSort: Long = System.currentTimeMillis(),
    val wPageModuleEnable: Boolean = true,
)
