package com.skyune.loficorner.ui.homeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusDirection.Companion.In
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImagePainter.State.Empty.painter
import com.skyune.loficorner.R

@Preview
@Composable
fun room() {

    //This is dumb but it actually works so...
    Box(modifier = Modifier.zIndex(2f).wrapContentSize()) {

        Image(modifier= Modifier.wrapContentSize().aspectRatio(0.9f),painter = painterResource(R.drawable.jazz), contentDescription = "")

        Box(
            Modifier
                .zIndex(1f)
                .align(Alignment.Center)
                .aspectRatio(1f)
        ) {
            //Image(modifier= Modifier.fillMaxHeight(),painter = painterResource(R.drawable.run1), contentDescription = "")

            Row(modifier = Modifier.wrapContentSize()) {
                Spacer(Modifier.weight(2.4f)) //left horizontal spacer
                Column(modifier = Modifier.wrapContentSize()) {
                    Spacer(Modifier.weight(2.2f)) //top vertical spacer
//                    Image(
//                        modifier = Modifier.weight(2.5f,true),
//                        painter = painterResource(R.drawable.run1),
//                        contentDescription = ""
//                    )
              //    GifImage(modifier = Modifier.weight(2.3f))

                    Spacer(Modifier.weight(1.1f)) //bottom vertical spacer
                }
                Spacer(modifier = Modifier.weight(0.4f)) //right horizontal spacer
            }
        }

     }
    }
