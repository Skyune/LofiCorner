package com.skyune.loficorner.ui.profileScreen.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skyune.loficorner.R
import com.skyune.loficorner.ui.theme.Theme
import com.skyune.loficorner.widgets.RoomImage

@Composable
fun RoomImagesRow(onToggleTheme: (Theme) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,

        ) {
            Row(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)) {

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                RoomImage(
                    modifier = Modifier.weight(1f),
                    ImageId = R.drawable.rockstar,
                    onClick = { onToggleTheme(Theme.Jazz) },
                    roomTitle = "The Rockstar",
                )
                RoomImage(
                    modifier = Modifier.weight(1f),

                    ImageId = R.drawable.jazz,
                    onClick = { onToggleTheme(Theme.Jazz) },
                    roomTitle = "Jazz Enthusiast"
                )
                RoomImage(
                    modifier = Modifier.weight(1f),
                    ImageId = R.drawable.untitled,
                    onClick = { onToggleTheme(Theme.Witch) },
                    roomTitle = "The Delinquent"
                )
            }
        }
    }
}
