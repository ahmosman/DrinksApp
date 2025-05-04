package com.example.drinksapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest

@Composable
fun LogoComponent(horizontal: Boolean) {
    val context = LocalContext.current
    val logo = ImageRequest.Builder(context)
        .data("file:///android_asset/drinksapp_logo.svg")
        .decoderFactory(SvgDecoder.Factory())
        .build()

    if (horizontal) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = logo,
                contentDescription = "Logo",
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "DrinksApp",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = logo,
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp)
            )
            Text(
                text = "DrinksApp",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .offset(y = (-20).dp)
                    .zIndex(1f)
            )
        }
    }
}