package com.arturlasok.feature_core.data.datasource.api.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class WebPosition(
    @Contextual
    val _id:ObjectId? = null,
    val wPositionName : String = "",
    val wPosition : Int,
)
