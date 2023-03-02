package com.skyune.loficorner.ui.settingsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skyune.loficorner.ui.theme.Theme
import com.skyune.loficorner.viewmodels.ProfileViewModel
import com.skyune.loficorner.viewmodels.SettingsViewModel
import java.util.*

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel = hiltViewModel(), onToggleTheme: (Theme) -> Unit, onToggleDarkMode: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary),
        contentAlignment = Alignment.Center
    ) {

        Row() {
            IconButton(
                onClick = { },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Toggle Theme")
            }
            IconButton(
                onClick = {},
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Toggle Theme")
            }
            IconButton(
                onClick = {onToggleDarkMode()},
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Toggle Theme")
            }

            val timePassedList by settingsViewModel.getAllTimePassed().observeAsState(listOf())

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(timePassedList) { timePassed ->
                    val date = Date(timePassed.date)
                    Text(text = "Time passed: ${timePassed.time}, Date: ${date}")
                }
            }

        }
    }
}



@Composable
@Preview
fun SettingsScreenPreview() {
    SettingsScreen(onToggleTheme = {}) {}
}