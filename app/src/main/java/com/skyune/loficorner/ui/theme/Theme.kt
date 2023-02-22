package com.skyune.loficorner.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightThemeColors = lightColors(
    primary = JazzMainScreenPrimary,
    primaryVariant = JazzMainScreenPrimaryVariant,
    onPrimary = Color.White,
    secondary = JazzMainScreenSecondary,
    secondaryVariant = Teal300,
    onSecondary = Black1,
    error = RedErrorDark,
    onError = RedErrorLight,
    background = Grey1,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Black1,
)

public val JazzRoomTheme = lightColors(
    primary = JazzMainScreenPrimary,
    primaryVariant = JazzMainScreenPrimaryVariant,
    onPrimary = Color.White,
    secondary = JazzMainScreenSecondary,
    secondaryVariant = Teal300,
    onSecondary = Black1,
    error = RedErrorDark,
    onError = RedErrorLight,
    background = Grey1,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Black1,
)

public val DarkThemeColors = darkColors(
    primary = JazzMainScreenPrimary,
    primaryVariant = JazzMainScreenPrimaryVariant,
    onPrimary = Color.White,
    secondary = JazzMainScreenSecondary,
    onSecondary = Color.White,
    error = RedErrorLight,
    background = Color.Black,
    onBackground = Color.White,
    surface = Black1,
    onSurface = Color.White,
)
public val RainbowTheme = darkColors(
    primary = JazzMainScreenPrimary,
    primaryVariant = JazzMainScreenPrimaryVariant,
    onPrimary = Color.Black,
    secondary = JazzMainScreenSecondary,
    onSecondary = Color.White,
    error = RedErrorLight,
    background = Color.Black,
    onBackground = Color.White,
    surface = Black1,
    onSurface = Color.White,
)

enum class Theme {
    Light,
    Dark,
    Auto
}

@Composable
fun AppTheme(theme: Theme,
             content: @Composable() () -> Unit) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        color = Color(0xFFBA98AC)
    )
    val colors = when (theme) {
        Theme.Light -> JazzRoomTheme
        Theme.Dark -> RainbowTheme
        else -> LightThemeColors
    }
    MaterialTheme(

        colors = colors,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
    ) {
        content()
    }
}
