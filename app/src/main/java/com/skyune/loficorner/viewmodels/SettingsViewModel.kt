package com.skyune.loficorner.viewmodels

import android.annotation.SuppressLint
import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skyune.loficorner.data.DataOrException
import com.skyune.loficorner.model.CurrentSong
import com.skyune.loficorner.model.TimePassed
import com.skyune.loficorner.model.Weather
import com.skyune.loficorner.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(InternalCoroutinesApi::class)
@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: WeatherRepository)
    : ViewModel() {



    private var countDownTimer: CountDownTimer? = null
    private var timePaused = 0L

    private val WORK_DURATION_MINUTES = 10
    private val SHORT_BREAK_DURATION_MINUTES = 5
    private val LONG_BREAK_DURATION_MINUTES = 15
    private val POMODORO_SESSIONS_BEFORE_LONG_BREAK = 2
    private val ONE_MINUTE_MILLIS = 60_000L

    private var pomodorosCompleted = 0

    var timeLeft by mutableStateOf(0L)
    var currentSession by mutableStateOf(PomodoroSession.WORK)
    var isTimerRunning by mutableStateOf(false)

    fun startTimer() {
        if (countDownTimer == null) {
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

}

enum class PomodoroSession {
    WORK,
    SHORT_BREAK,
    LONG_BREAK
}

@SuppressLint("DefaultLocale")
fun getTimeString(timeMillis: Long): String {
    val seconds = timeMillis / 1000 % 60
    val minutes = timeMillis / (1000 * 60) % 60
    return String.format("%02d:%02d", minutes, seconds)
}



