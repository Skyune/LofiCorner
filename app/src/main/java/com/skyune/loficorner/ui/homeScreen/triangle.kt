package com.skyune.loficorner.ui.homeScreen

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class triangle(val offset: Int) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val trianglePath = Path().apply {
            moveTo(x = 0f + offset, y = size.height)
            lineTo(x = 0f, y = size.height)
            lineTo(x = 0f, y = size.height - offset)
        }
        return Outline.Generic(path = trianglePath)
    }
}