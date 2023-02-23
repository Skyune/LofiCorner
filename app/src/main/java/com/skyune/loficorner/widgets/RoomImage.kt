package com.skyune.loficorner.widgets

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest


@Composable
fun RoomImage(
    modifier: Modifier = Modifier,
    ImageId: Int,
    onClick: () -> Unit,
    roomTitle: String,
    isSelected: Boolean,
) {
    Box(modifier = modifier
        .fillMaxWidth()
        .padding(all = 4.dp)
        .clickable {
            onClick.invoke()
        }
        .indication(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        )
    ) {
        Row {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    val animationSpec = spring<Float>(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )


                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.05f else 1f,
                        animationSpec = animationSpec
                    )

                    Box(
                        modifier = Modifier
                            .clickable(onClick = onClick)
                            .padding(0.dp, 0.dp, 0.dp, 10.dp)
                            .size(size = 130.dp)
                            .scale(scale)
                    ) {
                        Box(modifier = Modifier, Alignment.Center) {

                            Box(
                                Modifier
                                    .wrapContentSize()
                                    .scale(0.98f)
                            ) {

                                if(isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .width(width = 36.dp)
                                            .align(Alignment.Center)
                                            .offset(y = 36.dp)
                                            .height(height = 36.dp)
                                            .zIndex(30f)
                                            .clip(shape = RoundedCornerShape(90.dp))
                                            .background(brush = Brush.linearGradient(
                                                0f to MaterialTheme.colors.secondary,
                                                1f to MaterialTheme.colors.secondaryVariant,
                                                start = Offset(0f, 0f),
                                                end = Offset(26f, 25.59f)))) {
                                        Image(
                                            rememberAsyncImagePainter(
                                                ImageRequest.Builder(LocalContext.current)
                                                    .diskCachePolicy(CachePolicy.DISABLED)
                                                    .data(data = com.skyune.loficorner.R.drawable.tick)
                                                    .build()
                                            ),
                                            modifier = Modifier
                                            .width(width = 20.dp)
                                                .height(height = 20.dp)

                                                .wrapContentSize().align(Alignment.Center)
                                            ,
                                            contentDescription = null,
                                            colorFilter = ColorFilter.tint(Color.White)

                                        )

                                    }


                                }
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
                                    colorFilter = ColorFilter.tint(MaterialTheme.colors.secondaryVariant)

                                )

                                Image(
                                    rememberAsyncImagePainter(
                                        ImageRequest.Builder(LocalContext.current)
                                            .diskCachePolicy(CachePolicy.DISABLED)
                                            .data(data = com.skyune.loficorner.R.drawable.subtract)
                                            .build()
                                    ),
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .aspectRatio(0.9f)
                                        .zIndex(2f),
                                    contentDescription = null,
                                    contentScale = ContentScale.FillBounds,
                                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onSecondary)
                                )

                                Image(
                                    rememberAsyncImagePainter(
                                        ImageRequest.Builder(LocalContext.current)
                                            .diskCachePolicy(CachePolicy.DISABLED)
                                            .data(data = ImageId)
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
                    }
                    Box(
                        modifier = Modifier
                            .heightIn(MaterialTheme.typography.body1.lineHeight.value.dp)
                            .fillMaxWidth().padding(4.dp),
                        Alignment.Center,
                    ) {
                        Text(
                            text = roomTitle,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colors.onSurface,
                            textAlign = TextAlign.Center
                        )
                    }

            }
        }
    }
}