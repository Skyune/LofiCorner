package com.skyune.loficorner.widgets

import android.graphics.BlendMode
import android.graphics.ColorMatrix
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode.Companion.SrcAtop
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest


@Composable
fun RoomImage(
    modifier: Modifier = Modifier,
    ImageId: Int,
    onClick: () -> Unit,
    roomTitle: String) {
    Box(modifier = modifier
        .fillMaxWidth()
        .padding(all = 4.dp)
        .clickable {
            onClick.invoke()
        }
        .indication(interactionSource = remember { MutableInteractionSource() }, indication = null)) {
        Row {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {


                Box(modifier = Modifier, Alignment.Center) {

                    Box(
                        Modifier
                            .wrapContentSize()
                            .scale(0.98f)) {
                        Image(
                            rememberAsyncImagePainter(
                                ImageRequest.Builder(LocalContext.current)
                                    .diskCachePolicy(CachePolicy.DISABLED)
                                    .data(data = com.skyune.loficorner.R.drawable.subtract__4_)
                                    .build()
                            ),
                            modifier = Modifier
                                .wrapContentSize()
                                .aspectRatio(0.9f)
                                .zIndex(0.1f)
                                .scale(1.02f),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,

                            )

                        Image(
                            rememberAsyncImagePainter(
                                ImageRequest.Builder(LocalContext.current)
                                    .diskCachePolicy(CachePolicy.DISABLED)
                                    .data(data =  com.skyune.loficorner.R.drawable.subtract)
                                    .build()
                            ),
                            modifier = Modifier
                                .wrapContentSize()
                                .aspectRatio(0.9f)
                                .zIndex(2f),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds
                        )

                        Image(
                            rememberAsyncImagePainter(
                                ImageRequest.Builder(LocalContext.current)
                                    .diskCachePolicy(CachePolicy.DISABLED)
                                    .data(data =  ImageId)
                                    .build()
                            ),
                            modifier = Modifier
                                .wrapContentSize()
                                .aspectRatio(0.9f)
                                .zIndex(1f),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                        )
                    }

                }
                Text(text = roomTitle, modifier = Modifier.padding(vertical = 10.dp), fontSize = 12.sp, color = Color(MaterialTheme.colors.onSurface.value))
            }
        }
    }

}

