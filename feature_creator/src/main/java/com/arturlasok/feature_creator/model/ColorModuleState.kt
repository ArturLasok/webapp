package com.arturlasok.feature_creator.model

import android.util.Log
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.graphics.alpha
import com.arturlasok.feature_core.util.TAG

data class ColorModuleState(
    var r: Int,
    val g: Int,
    val b: Int,
    val alpha: Int,
    val componentId: String = ""
)
fun convertComposeRGBAtoCssRGBA(composeRGB: ColorModuleState) : String {
    return "${composeRGB.r},${composeRGB.g},${composeRGB.b},${composeRGB.alpha}"
}
fun convertCssRGBAtoComposeRGBA(cssRGB: String) : Color {
    val (r,g,b,a) = cssRGB.split(",")
    return Color(r.toInt(),g.toInt(),b.toInt(),a.toInt())

}
fun convertCssRGBAStringToRedInt(cssRGB: String) : Int {
    val (r,g,b,a) = cssRGB.split(",")
    return r.toInt()
}
fun convertCssRGBAStringToGreenInt(cssRGB: String) : Int {
    val (r,g,b,a) = cssRGB.split(",")
    return g.toInt()
}
fun convertCssRGBAStringToBlueInt(cssRGB: String) : Int {
    val (r,g,b,a) = cssRGB.split(",")
    return b.toInt()
}
fun convertCssRGBAStringToAlphaInt(cssRGB: String) : Int {
    val (r,g,b,a) = cssRGB.split(",")
    return a.toInt()
}
fun convertCssRGBAPlusComponentIdToColorModuleState(cssRGB: String,componentId: String) : ColorModuleState {
    Log.i(TAG,"color red: ${convertCssRGBAStringToRedInt(cssRGB)}")
    Log.i(TAG,"color green: ${convertCssRGBAStringToGreenInt(cssRGB)}")
    Log.i(TAG,"color blue: ${convertCssRGBAStringToBlueInt(cssRGB)}")
    Log.i(TAG,"color alpha: ${convertCssRGBAStringToAlphaInt(cssRGB)}")
    return ColorModuleState(
    convertCssRGBAStringToRedInt(cssRGB),
    convertCssRGBAStringToGreenInt(cssRGB),
    convertCssRGBAStringToBlueInt(cssRGB),
    convertCssRGBAStringToAlphaInt(cssRGB),
    componentId
    )
}
fun iconsDataForColorPalette() : List<ColorModuleState> {
    return listOf(
        ColorModuleState(255,255,255,255,"WHITE"),
        ColorModuleState(0,0,0,255,"BLACK"),
        ColorModuleState(255,0,0,255,"RED"),
        ColorModuleState(0,255,0,255,"GREEN"),
        ColorModuleState(0,0,255,255,"BLUE"),
        ColorModuleState(255,255,0,255,"YELLOW"),
        ColorModuleState(0,255,255,255,"CYAN"),
        ColorModuleState(255,0,255,255,"MAGENTA"),
        ColorModuleState(192,192,192,255,"SILVER"),
        ColorModuleState(128,128,128,255,"GRAY"),
        ColorModuleState(128,0,0,255,"MAROON"),
        ColorModuleState(0,128,0,255,"DARKGREEN"),
        ColorModuleState(128,0,128,255,"PURPLE"),
        ColorModuleState(0,128,128,255,"TEAL"),
        ColorModuleState(0,0,128,255,"NAVY"),


        )

}
