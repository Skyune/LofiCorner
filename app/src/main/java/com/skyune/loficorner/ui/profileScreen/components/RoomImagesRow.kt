package com.skyune.loficorner.ui.profileScreen.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skyune.loficorner.R
import com.skyune.loficorner.model.CurrentRoom
import com.skyune.loficorner.ui.theme.Theme
import com.skyune.loficorner.viewmodels.ProfileViewModel
import com.skyune.loficorner.widgets.RoomImage

@Composable
fun RoomImagesRow(
    showAll: Boolean,
    onToggleTheme: (Theme) -> Unit,
    profileViewModel: ProfileViewModel
) {
    val selectedButtonIndex = remember { mutableStateOf(profileViewModel.selectedRoomIndexId.value) }

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
                        ImageId = R.drawable.biologistborder,
                        onClick = { onToggleTheme(Theme.Botanist)
                            profileViewModel.insertRoom(CurrentRoom(imageId = R.drawable.biologistborder))
                            profileViewModel.selectRoomIndex(0)
                            selectedButtonIndex.value = 0
                                  },
                        roomTitle = "The Rockstar",
                        isSelected = selectedButtonIndex.value == 0

                    )
                    RoomImage(
                        modifier = Modifier.weight(1f),

                        ImageId = R.drawable.queenborder,
                        onClick = { onToggleTheme(Theme.Queen)
                            profileViewModel.insertRoom(CurrentRoom(imageId = R.drawable.queenborder))
                            profileViewModel.selectRoomIndex(1)
                            selectedButtonIndex.value = 1
                                  },
                        roomTitle = "Jazz Enthusiast",
                        isSelected =  selectedButtonIndex.value == 1,
                    )
                    RoomImage(
                        modifier = Modifier.weight(1f),
                        ImageId = R.drawable.rockstarborder,
                        onClick = { onToggleTheme(Theme.Rockstar)
                            profileViewModel.insertRoom(CurrentRoom(imageId = R.drawable.rockstarborder))
                            profileViewModel.selectRoomIndex(2)
                            selectedButtonIndex.value = 2
                                  },
                        roomTitle = "The Delinquent",
                        isSelected =  selectedButtonIndex.value == 2,
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
                            onClick = { onToggleTheme(Theme.Witch)
                                profileViewModel.insertRoom(CurrentRoom(imageId = R.drawable.witch))
                                profileViewModel.selectRoomIndex(3)
                                selectedButtonIndex.value = 3
                                      },
                            roomTitle = "The Witch's Bedroom",
                            isSelected =  selectedButtonIndex.value == 3,
                        )
                        RoomImage(
                            modifier = Modifier.weight(1f),

                            ImageId = R.drawable.jazzborder,
                            onClick = { onToggleTheme(Theme.Jazz)
                                profileViewModel.insertRoom(CurrentRoom(imageId = R.drawable.jazzborder))
                                profileViewModel.selectRoomIndex(4)
                                selectedButtonIndex.value = 4
                                      },
                            roomTitle = "Jazz Enthusiast",
                            isSelected =  selectedButtonIndex.value == 4,
                        )
                        RoomImage(
                            modifier = Modifier.weight(1f),
                            ImageId = R.drawable.witchopaque,
                            onClick = { onToggleTheme(Theme.Witch)
                                profileViewModel.insertRoom(CurrentRoom(imageId = R.drawable.witchopaque))
                                profileViewModel.selectRoomIndex(5)
                                selectedButtonIndex.value = 5
                                      },
                            roomTitle = "The Delinquent",
                            isSelected =  selectedButtonIndex.value == 5,
                        )
                    }
            }
        }
    }
}
