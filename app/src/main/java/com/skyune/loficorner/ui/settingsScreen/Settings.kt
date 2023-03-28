package com.skyune.loficorner.ui.settingsScreen

import android.graphics.LinearGradient
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShader
import com.patrykandpatrick.vico.core.context.DrawContext
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.skyune.loficorner.ui.profileScreen.ClippedShadow
import com.skyune.loficorner.viewmodels.PomodoroSession
import com.skyune.loficorner.viewmodels.SettingsViewModel
import com.skyune.loficorner.viewmodels.getTimeString


private val POPUP_WIDTH = 280.dp
private val POPUP_HEIGHT = 400.dp
private val POPUP_PADDING = 16.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
) {

    val listState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    0f to MaterialTheme.colors.background,
                    1f to MaterialTheme.colors.onBackground,
                    start = Offset(250f, 300f),
                    end = Offset(900f, 1900.5f)
                )
            ), contentAlignment = Alignment.Center
    ) {


        LazyColumn(
            modifier = Modifier,
            contentPadding = PaddingValues(6.dp),
            state = listState,
            verticalArrangement = Arrangement.Top
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Activity",
                        color = Color(MaterialTheme.colors.surface.value),
                        textAlign = TextAlign.Start,
                        lineHeight = 26.sp,
                        style = TextStyle(
                            fontSize = 22.sp, fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .padding(10.dp, 4.dp, 0.dp, 4.dp)
                            .wrapContentSize()
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .padding(start = 6.dp, end = 6.dp)
                ) {
                    ClippedShadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .matchParentSize()
                            .padding(4.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .clip(shape = RoundedCornerShape(15.dp))
                            .background(
                                Brush.linearGradient(
                                    0f to Color(MaterialTheme.colors.primary.value),
                                    1f to Color(MaterialTheme.colors.primaryVariant.value),
                                    start = Offset(0f, 0f),
                                    end = Offset(20f, 500f)
                                )
                            )
                            .border(
                                BorderStroke(
                                    1.dp, brush = Brush.linearGradient(
                                        0f to Color(MaterialTheme.colors.secondary.value),
                                        1f to Color(MaterialTheme.colors.secondaryVariant.value),
                                        start = Offset(0f, 0f),
                                        end = Offset(20f, 500f)
                                    )
                                )
                            ), contentAlignment = Alignment.CenterStart
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column {


                                val firstColor = MaterialTheme.colors.onPrimary
                                val secondColor = MaterialTheme.colors.surface
                                val circleColor = Color.White

                                val dynamicShader: (DrawContext, Float, Float, Float, Float) -> Shader =
                                    { context, x1, y1, x2, y2 ->
                                        LinearGradient(
                                            x1, y1, x1, y2, intArrayOf(
                                                secondColor.copy(alpha = 0.4f).toArgb(), 0), null, android.graphics.Shader.TileMode.CLAMP
                                        )
                                    }


                                val chartEntryModel = entryModelOf(4f, 12f, 0f, 6f, 4f, 7f, 8f)
                                ProvideChartStyle(remember {
                                    ChartStyle(
                                        axis = ChartStyle.Axis(
                                            axisGuidelineColor = Color.Transparent,
                                            axisLineColor = Color.Transparent,
                                            axisLabelColor = Color.Magenta
                                        ),
                                        columnChart = ChartStyle.ColumnChart(
                                            columns = listOf(
                                                LineComponent(Color.Transparent.toArgb())
                                            )
                                        ),
                                        ChartStyle.LineChart(
                                            listOf(
                                                LineChart.LineSpec(
                                                    secondColor.toArgb(),
                                                    lineBackgroundShader = DynamicShader(
                                                        dynamicShader
                                                    ),
                                                    pointSizeDp = 10f,
                                                    point = MyComponent(circleColor.toArgb(),secondColor.toArgb()),
                                                    pointConnector = StraightConnector()
                                                )
                                            )
                                        ),
                                        ChartStyle.Marker(),
                                        elevationOverlayColor = Color.DarkGray
                                    )
                                }) {


                                    Chart(
                                        chart = lineChart(),
                                        model = chartEntryModel,
                                        bottomAxis = bottomAxis()
                                    )
                                }

                            }
                        }
                    }
                }
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)
                ) {
                    Text(
                        text = "Performed tasks",
                        color = Color(MaterialTheme.colors.surface.value),
                        textAlign = TextAlign.End,
                        lineHeight = 26.sp,
                        style = TextStyle(

                            fontSize = 20.sp, fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .padding(10.dp, 0.dp, 0.dp, 0.dp)
                            .height(42.dp)

                    )

                    Text(
                        text = "Show All",
                        color = Color(MaterialTheme.colors.onSurface.value),
                        textAlign = TextAlign.End,
                        lineHeight = 20.sp,
                        style = TextStyle(
                            fontSize = 20.sp
                        ),
                        modifier = Modifier
                            .padding(0.dp, 0.dp, 10.dp, 0.dp)
                            .weight(0.2f)
                            .height(42.dp)
                    )
                }
            }

            item {


                Box(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .padding(start = 6.dp, end = 6.dp)
                ) {
                    ClippedShadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .matchParentSize()
                            .padding(4.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .wrapContentHeight()
                            .clip(shape = RoundedCornerShape(15.dp))
                            .background(
                                Brush.linearGradient(
                                    0f to Color(MaterialTheme.colors.primary.value),
                                    1f to Color(MaterialTheme.colors.primaryVariant.value),
                                    start = Offset(0f, 0f),
                                    end = Offset(20f, 500f)
                                )
                            )
                            .border(
                                BorderStroke(
                                    1.dp, brush = Brush.linearGradient(
                                        0f to Color(MaterialTheme.colors.secondary.value),
                                        1f to Color(MaterialTheme.colors.secondaryVariant.value),
                                        start = Offset(0f, 0f),
                                        end = Offset(20f, 500f)
                                    )
                                )
                            ), contentAlignment = Alignment.CenterStart
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            PieChart(
                                pieChartData = PieChartData(
                                    listOf(
                                        PieChartData.Slice(10.2f, Color.White),
                                        PieChartData.Slice(32.2f, Color.Blue),
                                        PieChartData.Slice(42f, Color.Green)
                                    )
                                ),
                                // Optional properties.
                                modifier = Modifier
                                    .size(200.dp)
                                    .padding(10.dp),
                                sliceDrawer = SimpleSliceDrawer()
                            )
                        }
                    }
                }
            }
        }


        /*TimerPopup(
            currentSession = settingsViewModel.currentSession,
            timeLeft = settingsViewModel.timeLeft,
            isTimerRunning = settingsViewModel.isTimerRunning,
            onPause = settingsViewModel::pauseTimer,
            onStart = settingsViewModel::startTimer,
            onReset = settingsViewModel::resetTimer,

        )*/
    }
}


@Composable
fun TimerPopup(
    currentSession: PomodoroSession,
    timeLeft: Long,
    isTimerRunning: Boolean,
    onPause: () -> Unit,
    onStart: () -> Unit,
    onReset: () -> Unit,

    ) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(POPUP_PADDING)
                .size(width = POPUP_WIDTH, height = POPUP_HEIGHT),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(
                    text = "Pomodoro Timer",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = when (currentSession) {
                        PomodoroSession.WORK -> "Work Session"
                        PomodoroSession.SHORT_BREAK -> "Short Break"
                        PomodoroSession.LONG_BREAK -> "Long Break"
                    }, fontSize = 48.sp, modifier = Modifier.padding(bottom = 16.dp)
                )


                Text(
                    text = getTimeString(timeLeft),
                    fontSize = 48.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )


                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { if (isTimerRunning) onPause() else onStart() },
                        modifier = Modifier.width(120.dp)
                    ) {
                        Text(if (isTimerRunning) "Pause" else "Start")
                    }
                    Button(
                        onClick = { onReset() }, modifier = Modifier.width(120.dp)
                    ) {
                        Text("Reset")
                    }
                }
            }
        }
    }
}