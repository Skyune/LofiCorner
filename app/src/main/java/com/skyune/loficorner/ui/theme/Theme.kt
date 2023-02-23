package com.skyune.loficorner.ui.theme

import android.annotation.SuppressLint
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController



public val BeautyQueenTheme = lightColors(
    primary = PrimaryJazzColor,
    primaryVariant = PrimaryVariantJazzColor,
    onPrimary = ComponentColor1,
    secondary = BorderStrokeColor1,
    secondaryVariant = BorderStrokeColor2,
    onSecondary = ComponentColor2,
    error = RedErrorDark,
    onError = RedErrorLight,
    background = BackgroundBrush1,
    onBackground = BackgroundBrush2,
    surface = SurfaceText,
    onSurface = Black1,
)
@SuppressLint("ConflictingOnColor")
public val WitchRoomTheme = darkColors(
    primary = Color(0xFF5A459C),
    primaryVariant = Color(0xFF2F013F),
    onPrimary = Color(0xFF9E50F3), //Component1
    secondary = Color(0xFF7800D6), //BorderStroke1
    secondaryVariant = Color(0xFF58448F), //BorderStroke2
    onSecondary = Color(0xFF842FA8), //Component2
    error = Color.White,
    background = Color(0xff483c73),
    onBackground = Color(0xFF350247),
    surface = Color(0xFFD4A9FF), //surfaceText
    onSurface = Color.White,
)

@SuppressLint("ConflictingOnColor")
public val BotanistRoomTheme = lightColors(
    primary = Color(0xFFFFFFFF),
    primaryVariant = Color(0xFF93FF89),
    onPrimary = Color(0xFF7DDD74), //Component1
    secondary = Color(0xFF74CA6C), //BorderStroke1
    secondaryVariant = Color(0xFF61864D), //BorderStroke2
    onSecondary = Color(0xFF3AAF53), //Component2
    error = Color.Black,
    background = Color(0xFFEDF8ED),
    onBackground = Color(0xFFAEFFA7),
    surface = Color(0xFF24300B), //surfaceText
    onSurface = Color.Black,
)

@SuppressLint("ConflictingOnColor")
public val JazzRoomTheme = lightColors(
    primary = Color(0xFFFEFBFB),
    primaryVariant = Color(0xFFFEB0AE),
    onPrimary = Color(0xFFE97774), //Component1
    secondary = Color(0xFFF6DBE2), //BorderStroke1
    secondaryVariant = Color(0xFFF6BCBE), //BorderStroke2
    onSecondary = Color(0xFFFCB0AE), //Component2
    error = Color.Black,
    background = Color(0xFFFEFBFB),
    onBackground = Color(0xFFFDB5B3),
    surface = Color(0xFFA45756), //surfaceText
    onSurface = Color.Black,
)

@SuppressLint("ConflictingOnColor")
public val RockstarRoomTheme = lightColors(
    primary = Color(0xFF95C8FA),
    primaryVariant = Color(0xFF44A4FE),
    onPrimary = Color(0xFF8BC4FB), //Component1
    secondary = Color(0xFFA4CFFB), //BorderStroke1
    secondaryVariant = Color(0xFF80BFFB), //BorderStroke2
    onSecondary = Color(0xFF339DFE), //Component2
    error = Color.Black,
    background = Color(0xFFEDF0F7),
    onBackground = Color(0xFF339CFE),
    surface = Color(0xFF353388), //surfaceText
    onSurface = Color.Black,
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
    Jazz,
    Piano,
    Witch,
    Queen,
    Botanist,
    Rockstar
}

@Composable
fun AppTheme(theme: Theme,
             content: @Composable() () -> Unit) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        color = Color(0xFFBA98AC)
    )
    val colors = when (theme) {
        Theme.Queen -> BeautyQueenTheme
        Theme.Witch -> WitchRoomTheme
        Theme.Jazz -> JazzRoomTheme
        Theme.Botanist -> BotanistRoomTheme
        Theme.Rockstar -> RockstarRoomTheme
        else -> JazzRoomTheme
    }
    MaterialTheme(

        colors = colors,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
    ) {
        content()
    }
}
