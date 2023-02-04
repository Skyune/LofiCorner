package com.skyune.loficorner.ui.settingsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yeocak.parallaximage.ParallaxImage

@Composable
fun SettingsScreen(onToggleTheme: () -> Unit, onToggleDarkMode: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary),
        contentAlignment = Alignment.Center
    ) {

        Row() {
            IconButton(
                onClick = {onToggleTheme()  },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Toggle Theme")
            }
            IconButton(
                onClick = {onToggleTheme()},
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

        }
    }
}



@Composable
@Preview
fun SettingsScreenPreview() {
    SettingsScreen(onToggleTheme = {}, onToggleDarkMode = {})
}