package com.arturlasok.feature_core.domain.model

import kotlinx.serialization.Serializable



data class ModuleText(
    val _id:String = "",
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
) : java.io.Serializable
