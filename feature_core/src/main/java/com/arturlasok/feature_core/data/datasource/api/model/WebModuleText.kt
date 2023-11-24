package com.arturlasok.feature_core.data.datasource.api.model

import com.arturlasok.feature_core.util.AnySerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId


@Serializable
data class WebModuleText(
    @Contextual
    @Serializable(with = AnySerializer::class)
    val _id:Any? = null,
    val wTextName:String = "Text Module",
    val wTextRoute: String = "",
    val wText: String = "Empty",
    val wTextWeight: String = "normal",
    val wTextAlign: String = "left",
    val wTextDecoration: String ="",
    val wTextH: String = "h3",
    val wTextColor:String = "Black",
    val wTextBackgroundColor:String = "Transparent",
    val wTextBackgroundRoundedRectangle : String = "no",
    val wTextBackgroundRectangle: String = "no",
    val wTextBorder: String = "no",
    val wTextBorderColor: String = "Transparent",
    val wLink: String = "",
)
