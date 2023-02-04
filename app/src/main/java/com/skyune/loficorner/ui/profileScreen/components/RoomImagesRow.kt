package com.skyune.loficorner.ui.profileScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skyune.loficorner.R
import com.skyune.loficorner.widgets.RoomImage

@Preview()
@Composable
fun RoomImagesRow() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,

        ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                "Your Lofi Corner,",
                style = MaterialTheme.typography.h4, fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text("What would you like to listen to?", fontSize = 16.sp, color = Color.Black)
            Card(modifier = Modifier
                .height(30.dp)
                .fillMaxWidth()
                , backgroundColor = MaterialTheme.colors.secondary, ) {
                Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {


                Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colors.primaryVariant, modifier = Modifier.padding(start = 8.dp))
                Text("Search for playlists", fontSize = 16.sp, color = Color.Black)
                }
            }
            //SearchBar(modifier = Modifier.height(30.dp))
            Row(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)) {
                Text("Rooms", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1f))
                Text("Show All")
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                RoomImage(
                    modifier = Modifier.weight(1f),
                    ImageId = R.drawable.rockstar,
                    onClick = { /*TODO*/ },
                    roomTitle = "The Rockstar",
                )
                RoomImage(
                    modifier = Modifier.weight(1f),

                    ImageId = R.drawable.jazz,
                    onClick = { /*TODO*/ },
                    roomTitle = "Jazz Enthusiast"
                )
                RoomImage(
                    modifier = Modifier.weight(1f),
                    ImageId = R.drawable.untitled,
                    onClick = { /*TODO*/ },
                    roomTitle = "The Delinquent"
                )
            }
        }
    }
}