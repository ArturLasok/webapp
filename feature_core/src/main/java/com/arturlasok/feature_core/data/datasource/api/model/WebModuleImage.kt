package com.arturlasok.feature_core.data.datasource.api.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class WebModuleImage(
    @Contextual
    val _id: ObjectId? = null,
    val wImageLink: String = "",
    val wImageBorder: String = "",
    val wImageDesc: String = "",
    val wImageWidth: String = "",
    val wImagePosition: String = "",
)
