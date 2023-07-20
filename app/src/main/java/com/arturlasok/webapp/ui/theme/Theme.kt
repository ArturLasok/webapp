package com.arturlasok.webapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.SystemUiController


private val DarkColorPalette = darkColors(

    primary = primaryDark,
    onPrimary = onPrimaryDark,

    primaryVariant = primaryVariantDark,

    secondary = secondaryDark,
    onSecondary = onSecondaryDark,

    background = backgroundDark,
    onBackground = onBackgroundDark,

    surface = surfaceDark,
    onSurface = onSurfaceDark,

    error = error,
    onError = onError
)

private val LightColorPalette = lightColors(

    primary = primary,
    onPrimary = onPrimary,

    primaryVariant = primaryVariant,

    secondary = secondary,
    onSecondary = onSecondary,

    background = background,
    onBackground = onBackground,

    surface = surface,
    onSurface = onSurface,

    error = error,
    onError = onError
)

//gradient background for themes
val lightBackgroundColorsList = listOf(Color(0xFFC7CCE7), Color(0xFFFFEBFA))
val darkBackgroundColorList = listOf( Color(0xFF000000), Color(0xFF181414))

val lightGradient = Brush.verticalGradient(
    lightBackgroundColorsList
)
val darkGradient = Brush.verticalGradient(
    darkBackgroundColorList
)

@Composable
fun StatusBarColor(is_dark: Boolean, systemUiController: SystemUiController) {

    if(is_dark) systemUiController.setStatusBarColor(darkBackgroundColorList[0])
    else systemUiController.setStatusBarColor(lightBackgroundColorsList[0])

}
@Composable
fun NavigationBarColor(is_dark: Boolean, systemUiController: SystemUiController) {

    if(is_dark) systemUiController.setNavigationBarColor(darkBackgroundColorList[1])
    else systemUiController.setNavigationBarColor(lightBackgroundColorsList[1])

}
@Composable
fun BackgroundColor(is_dark: Boolean, setBackgroundGradient: (brush: Brush) -> Unit) {

    if(is_dark) setBackgroundGradient(darkGradient) else setBackgroundGradient(lightGradient)

}

@Composable
fun WebAppTheme(
    systemUiController: SystemUiController,
    setBackgroundGradient: (brush: Brush) -> Unit,
    darkTheme: Int = 0,
    content: @Composable () -> Unit) {


    val colors =
        when(darkTheme) {
            0  -> if(isSystemInDarkTheme()) DarkColorPalette else LightColorPalette
            1  -> LightColorPalette
            2  -> DarkColorPalette
            else -> { LightColorPalette }
        }

        val isDark = when(darkTheme) {
            0  -> isSystemInDarkTheme()
            1  -> false
            2  -> true
            else -> { false }
        }

        StatusBarColor(is_dark = isDark, systemUiController = systemUiController)
        NavigationBarColor(is_dark= isDark, systemUiController = systemUiController)
        BackgroundColor(is_dark = isDark, setBackgroundGradient = { gradient-> setBackgroundGradient(gradient)})

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content =  content
    )


}