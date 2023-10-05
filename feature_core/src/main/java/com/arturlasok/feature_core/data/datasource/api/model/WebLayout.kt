package com.arturlasok.feature_core.data.datasource.api.model


import com.arturlasok.feature_core.util.AnySerializer
import com.arturlasok.feature_core.util.LocalObjectIdSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class WebLayout(
    @Contextual
    @Serializable(with = AnySerializer::class)
    val _id:Any? =null,
    @Contextual
    @Serializable(with = AnySerializer::class)
    val wLayoutProjectId: Any? = null,
    val wLayoutRouteToken: String = "",
    val wLayoutPageName: String = "",
    @Contextual
    @Serializable(with = AnySerializer::class)
    val wLayoutModule: Any? = null,
    val wLayoutModuleType: String = "",
    @Contextual
    @Serializable(with = AnySerializer::class)
    val wLayoutPosition: Any? = null,
    val wLayoutSort: Long = System.currentTimeMillis()
)
