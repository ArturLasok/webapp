package com.arturlasok.feature_core.domain.model

import kotlinx.serialization.Serializable



data class ModuleText(
    val _id:String = "",
    val wTextName:String = "Text Module",
    val wTextRoute: String = "",
    val wText: String = "Empty",
    val wTextWeight: String = "normal",
    val wTextAlign: String = "left",
    val wTextDecoration: String ="None",
    val wTextH: String = "h3",
    val wTextColor:String = "0,0,0,255",
    val wTextBackgroundColor:String = "255,255,255,255",
    val wTextBackgroundRoundedRectangle : String = "true",
    val wTextMarginTop: String = "10",
    val wTextMarginBottom: String = "10",
    val wTextMarginStart: String = "10",
    val wTextMarginEnd: String = "10",
    val wTextBorderMarginTop: String = "0",
    val wTextBorderMarginBottom: String = "0",
    val wTextBorderMarginStart: String = "0",
    val wTextBorderMarginEnd: String = "0",
    val wTextBorder: String = "yes",
    val wTextBorderColor: String = "0,0,0,255",
    val wTextLink: String = "",
) : java.io.Serializable
