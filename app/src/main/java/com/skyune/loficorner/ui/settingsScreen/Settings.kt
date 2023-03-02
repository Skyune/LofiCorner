package com.skyune.loficorner.ui.settingsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skyune.loficorner.viewmodels.PomodoroSession
import com.skyune.loficorner.viewmodels.SettingsViewModel
import com.skyune.loficorner.viewmodels.getTimeString


private val POPUP_WIDTH = 280.dp
private  val POPUP_HEIGHT = 400.dp
private  val POPUP_PADDING = 16.dp

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
) {

    //hehe it works KURWA
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(
                0f to MaterialTheme.colors.background,
        1f to MaterialTheme.colors.onBackground,
        start = Offset(250f, 300f),
        end = Offset(900f, 1900.5f))),
        contentAlignment = Alignment.Center
    ) {
        TimerPopup(
            currentSession = settingsViewModel.currentSession,
            timeLeft = settingsViewModel.timeLeft,
            isTimerRunning = settingsViewModel.isTimerRunning,
            onPause = settingsViewModel::pauseTimer,
            onStart = settingsViewModel::startTimer,
            onReset = settingsViewModel::resetTimer
        )
    }
}

@Composable
fun TimerPopup(
    currentSession: PomodoroSession,
    timeLeft: Long,
    isTimerRunning: Boolean,
    onPause: () -> Unit,
    onStart: () -> Unit,
    onReset: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
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
                    },
                    fontSize = 48.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
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
                        onClick = { onReset() },
                        modifier = Modifier.width(120.dp)
                    ) {
                        Text("Reset")
                    }
                }
            }
        }
    }
}