package com.skyune.loficorner.ui.profileScreen.components

import android.graphics.Bitmap
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skyune.loficorner.R
import com.skyune.loficorner.ui.theme.Theme
import com.skyune.loficorner.widgets.RoomImage

@Composable
fun RoomImagesRow(showAll: Boolean, onToggleTheme: (Theme) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,

        ) {
        Column(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
        ) {
            Row(modifier = Modifier.padding(top = 0.dp, bottom = 10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    RoomImage(
                        modifier = Modifier.weight(1f),
                        ImageId = R.drawable.rockstar,
                        onClick = { onToggleTheme(Theme.Botanist) },
                        roomTitle = "The Rockstar",
                    )
                    RoomImage(
                        modifier = Modifier.weight(1f),

                        ImageId = R.drawable.jazz,
                        onClick = { onToggleTheme(Theme.Queen) },
                        roomTitle = "Jazz Enthusiast"
                    )
                    RoomImage(
                        modifier = Modifier.weight(1f),
                        ImageId = R.drawable.witch,
                        onClick = { onToggleTheme(Theme.Rockstar) },
                        roomTitle = "The Delinquent"
                    )
                }
            }

            if (showAll) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        RoomImage(
                            modifier = Modifier.weight(1f),
                            ImageId = R.drawable.witch,
                            onClick = { onToggleTheme(Theme.Witch) },
                            roomTitle = "The Witch's Bedroom",
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
}
