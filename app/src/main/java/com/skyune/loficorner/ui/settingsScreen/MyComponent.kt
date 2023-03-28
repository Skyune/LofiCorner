package com.skyune.loficorner.ui.settingsScreen

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import com.patrykandpatrick.vico.core.component.Component
import com.patrykandpatrick.vico.core.context.DrawContext

class MyComponent(private val circleColor: Int, private val outlineColor: Int) : Component() {
    override fun draw(context: DrawContext, left: Float, top: Float, right: Float, bottom: Float) {
        // Calculate the center of the component
        val centerX = (left + right) / 2
        val centerY = (top + bottom) / 2

        // Calculate the radius of the component
        val radius = (right - left) / 2

        // Get a Paint object to draw the circle
        val paint = Paint()
        paint.color = circleColor
        paint.style = Paint.Style.FILL

        val canvas = context.canvas
        canvas.drawCircle(centerX, centerY, radius, paint)

        // Set the paint style to STROKE to draw an outline
        paint.style = Paint.Style.STROKE
        // Set the outline color to red
        paint.color = outlineColor
        // Set the outline width to 5 pixels
        paint.strokeWidth = 5f

        // Draw the circle with the red outline using a Canvas object
        canvas.drawCircle(centerX, centerY, radius, paint)

    }
}