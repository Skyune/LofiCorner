package com.skyune.loficorner.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun RoomImage(
    modifier: Modifier = Modifier,
    ImageId: Int,
    onClick: () -> Unit,
    roomTitle: String) {
    Box(modifier = modifier.fillMaxWidth()
        .padding(all = 4.dp)
        .clickable {
            onClick.invoke()
        }.indication(interactionSource = remember { MutableInteractionSource() }, indication = null  )) {
        Row {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {


                Box(modifier = Modifier, Alignment.Center) {
                    Image(
                        painter = painterResource(id = ImageId),
                        modifier = Modifier,
                        contentDescription = "clickable room"
                    )
//                    Column(
//                        verticalArrangement = Arrangement.Bottom,
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        //Spacer(Modifier.fillMaxSize(0.5f))
//
//                        RoundIconButton(
//                            modifier = Modifier,
//                            imageVector = Icons.Default.Star,
//                            onClick = { /*TODO*/ })
//                    }
                }
                Text(text = roomTitle, modifier = Modifier.padding(vertical = 10.dp), fontSize = 12.sp)

            }
        }
    }

}