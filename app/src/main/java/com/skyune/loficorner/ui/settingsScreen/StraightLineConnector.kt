package com.skyune.loficorner.ui.settingsScreen

import android.graphics.Paint
import androidx.compose.ui.graphics.toArgb
import com.patrykandpatrick.vico.core.component.Component
import com.patrykandpatrick.vico.core.context.DrawContext
import kotlin.math.absoluteValue

class StraightLineConnector(private val color: Int, private val strokeWidth: Float) : Component() {

    override fun draw(context: DrawContext, left: Float, top: Float, right: Float, bottom: Float) {
        val canvas = context.canvas
        val paint = Paint().apply {
            color = this@StraightLineConnector.color
            style = Paint.Style.STROKE
            strokeWidth = this@StraightLineConnector.strokeWidth
            isAntiAlias = true
        }
        canvas.drawLine(left, top, right, bottom, paint)
    }
}