package com.skyune.loficorner.ui.settingsScreen

import android.graphics.Color
import android.graphics.Paint
import com.patrykandpatrick.vico.core.component.Component
import com.patrykandpatrick.vico.core.context.DrawContext

class MyComponent : Component() {
    override fun draw(context: DrawContext, left: Float, top: Float, right: Float, bottom: Float) {
        // Calculate the center of the component
        val centerX = (left + right) / 2
        val centerY = (top + bottom) / 2

        // Calculate the radius of the component
        val radius = (right - left) / 2

        // Get a Paint object to draw the circle
        val paint = Paint()
        paint.color = Color.RED

        // Draw the circle using a Canvas object
        val canvas = context.canvas
        canvas.drawCircle(centerX, centerY, radius, paint)
    }
}