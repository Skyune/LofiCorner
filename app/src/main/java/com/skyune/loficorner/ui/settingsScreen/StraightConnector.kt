package com.skyune.loficorner.ui.settingsScreen

import android.graphics.*
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.chart.segment.SegmentProperties

class StraightConnector : LineChart.LineSpec.PointConnector {
    override fun connect(path: Path, prevX: Float, prevY: Float, x: Float, y: Float, segmentProperties: SegmentProperties, bounds: RectF) {
        path.lineTo(x, y)
    }
}