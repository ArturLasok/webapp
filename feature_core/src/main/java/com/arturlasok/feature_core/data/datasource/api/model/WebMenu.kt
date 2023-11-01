package com.arturlasok.feature_core.data.datasource.api.model

import com.arturlasok.feature_core.util.AnySerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class WebMenu(
    @Contextual
    @Serializable(with = AnySerializer::class)
    val _id: Any? = null,
    @Contextual
    @Serializable(with = AnySerializer::class)
    val wMenuProjectId: Any? = null,
    //where is
    val wMenuPlace: String = "",
    //where go
    val wMenuRoute: String = "",
    val wMenuSort: Long = System.currentTimeMillis(),
    val wMenuColor: String = "",
    val wMenuTextColor: String = "",
    val wMenuIconTint: String = ""
)
