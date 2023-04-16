package com.skyune.loficorner.ui.utils

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size

@Composable
fun GifImage(
    modifier: Modifier = Modifier,
    gifId: Int,
    colorFilter: ColorFilter? = null,
    isPaused: Boolean = false
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()


    Image(painter = rememberAsyncImagePainter(
        ImageRequest.Builder(context).data(data = gifId).apply(block = {
            size(Size.ORIGINAL)

        }
        ).build(), imageLoader = imageLoader
    ),
        contentDescription = null,
        modifier = modifier,
        colorFilter = colorFilter
    )
}