package com.arturlasok.feature_core.data.datasource.api.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class WebMenuDetails(
    @Contextual
    val _id: ObjectId? = null,
    val wMenuDetailsToken: String = "",
    val wMenuName: String = "",
    val wMenuSort: String = "",
    val wMenuStyle: String = "",
    val wMenuRoute: String = "",
    val wMenuIcon: String = ""
)
