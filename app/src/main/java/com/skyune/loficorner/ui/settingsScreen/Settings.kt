package com.skyune.loficorner.ui.settingsScreen

import android.annotation.SuppressLint
import android.os.CountDownTimer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skyune.loficorner.ui.theme.Theme
import com.skyune.loficorner.viewmodels.MainViewModel
import com.skyune.loficorner.viewmodels.SettingsViewModel
import kotlinx.coroutines.launch
import java.time.*
import java.util.*


@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    onToggleTheme: (Theme) -> Unit,
    onToggleDarkMode: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary),
        contentAlignment = Alignment.Center
    ) {


        Popup()
    }
}



enum class PomodoroSession {
    WORK,
    SHORT_BREAK,
    LONG_BREAK
}

@Composable
fun Popup() {
    var isTimerRunning by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(0L) }
    var currentSession by remember { mutableStateOf(PomodoroSession.WORK) }
    var timePaused by remember { mutableStateOf(0L) }

    var pomodorosCompleted = 0
    val WORK_DURATION_MINUTES = 10
    val SHORT_BREAK_DURATION_MINUTES = 5
    val LONG_BREAK_DURATION_MINUTES = 15
    val POMODORO_SESSIONS_BEFORE_LONG_BREAK = 2
    var countDownTimer: CountDownTimer? = null


    // should be 60_000L
    val ONE_MINUTE_MILLIS = 1000L




    fun startTimer() {
        val durationInMillis = when (currentSession) {
            PomodoroSession.WORK -> WORK_DURATION_MINUTES * ONE_MINUTE_MILLIS
            PomodoroSession.SHORT_BREAK -> SHORT_BREAK_DURATION_MINUTES * ONE_MINUTE_MILLIS
            PomodoroSession.LONG_BREAK -> LONG_BREAK_DURATION_MINUTES * ONE_MINUTE_MILLIS
        } - timePaused // subtract paused time from duration

        countDownTimer = object : CountDownTimer(durationInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
            }

            override fun onFinish() {
                when (currentSession) {
                    PomodoroSession.WORK -> {
                        currentSession = PomodoroSession.SHORT_BREAK
                        pomodorosCompleted++
                        if (pomodorosCompleted >= POMODORO_SESSIONS_BEFORE_LONG_BREAK) {
                            currentSession = PomodoroSession.LONG_BREAK
                            pomodorosCompleted = 0
                        }
                        timeLeft = 0L
                        startTimer()
                    }
                    PomodoroSession.SHORT_BREAK -> {
                        timeLeft = 0L
                        startTimer()
                    }
                    PomodoroSession.LONG_BREAK -> {
                        currentSession = PomodoroSession.WORK
                        timeLeft = 0L
                        startTimer()
                    }
                }
                // TODO: show a notification or play a sound to indicate the end of the timer
            }
        }.start()
        isTimerRunning = true
        timePaused = 0L
    }

    fun pauseTimer() {
        countDownTimer?.cancel()
        countDownTimer = null // set to null to properly clean up the old instance
        isTimerRunning = false
        val durationInMillis = when (currentSession) {
            PomodoroSession.WORK -> WORK_DURATION_MINUTES * ONE_MINUTE_MILLIS
            PomodoroSession.SHORT_BREAK -> SHORT_BREAK_DURATION_MINUTES * ONE_MINUTE_MILLIS
            PomodoroSession.LONG_BREAK -> LONG_BREAK_DURATION_MINUTES * ONE_MINUTE_MILLIS
        }
        timePaused += durationInMillis - timeLeft
    }

    fun resetTimer() {
        countDownTimer?.cancel() // cancel the existing CountDownTimer instance
        countDownTimer = null
        isTimerRunning = false
        timeLeft = 0L
        timePaused = 0L
        currentSession = PomodoroSession.WORK
    }



        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .size(width = 280.dp, height = 400.dp),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Text(
                        text = "Pomodoro Timer",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    when (currentSession) {
                        PomodoroSession.WORK -> {
                            Text(
                                text = "Work Session",
                                fontSize = 18.sp,

                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        PomodoroSession.SHORT_BREAK -> {
                            Text(
                                text = "Short Break",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        PomodoroSession.LONG_BREAK -> {
                            Text(
                                text = "Long Break",
                                fontSize = 18.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    }

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
                            onClick = { if (isTimerRunning) pauseTimer() else startTimer() },
                            modifier = Modifier.width(120.dp)
                        ) {
                            Text(if (isTimerRunning) "Pause" else "Start")
                        }
                        Button(
                            onClick = { resetTimer() },
                            modifier = Modifier.width(120.dp)
                        ) {
                            Text("Reset")
                        }
                    }
                }
            }
        }
    }
@SuppressLint("DefaultLocale")
fun getTimeString(timeMillis: Long): String {
    val seconds = timeMillis / 1000 % 60
    val minutes = timeMillis / (1000 * 60) % 60
    return String.format("%02d:%02d", minutes, seconds)
}

@Composable
@Preview
fun SettingsScreenPreview() {
    SettingsScreen(onToggleTheme = {}, onToggleDarkMode = {})
}