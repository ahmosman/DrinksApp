package com.example.drinksapp.components

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest

@Composable
fun AnimatedLogoComponent(
    horizontal: Boolean = false,
    compact: Boolean = false,
    onAnimationEnd: () -> Unit = {}
) {
    val context = LocalContext.current

    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
    }

    val request = remember {
        ImageRequest.Builder(context)
            .data("file:///android_asset/drinksapp_logo.svg")
            .build()
    }

    val imageView = remember {
        ImageView(context).apply {
            scaleType = ImageView.ScaleType.FIT_CENTER
        }
    }

    LaunchedEffect(imageLoader, request) {
        imageLoader.execute(request).drawable?.let {
            imageView.setImageDrawable(it)
        }
    }

    if (horizontal) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(if (compact) 32.dp else 48.dp),
                contentAlignment = Alignment.Center
            ) {
                AndroidView(
                    factory = { imageView },
                    modifier = Modifier.size(if (compact) 32.dp else 48.dp)
                )

                LaunchedEffect(Unit) {
                    animateLogo(imageView, onAnimationEnd)
                }
            }

            Text(
                text = "DrinksApp",
                color = Color.White,
                fontSize = if (compact) 20.sp else 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(if (compact) 150.dp else 200.dp),
                contentAlignment = Alignment.Center
            ) {
                AndroidView(
                    factory = { imageView },
                    modifier = Modifier.size(if (compact) 150.dp else 200.dp)
                )

                LaunchedEffect(Unit) {
                    animateLogo(imageView, onAnimationEnd)
                }
            }

            Text(
                text = "DrinksApp",
                color = Color.White,
                fontSize = if (compact) 26.sp else 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .offset(y = (-20).dp)
                    .zIndex(1f)
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            imageView.clearAnimation()
        }
    }
}

private fun animateLogo(imageView: ImageView, onAnimationEnd: () -> Unit) {
    val rotateAnimator = ObjectAnimator.ofFloat(
        imageView, View.ROTATION, 0f, 1080f
    ).apply {
        duration = 2000
        interpolator = AccelerateDecelerateInterpolator()
    }

    val scaleX = PropertyValuesHolder.ofFloat(
        View.SCALE_X, 0.5f, 1.2f, 1.0f
    )
    val scaleY = PropertyValuesHolder.ofFloat(
        View.SCALE_Y, 0.5f, 1.2f, 1.0f
    )
    val scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(
        imageView, scaleX, scaleY
    ).apply {
        duration = 2000
        interpolator = AccelerateDecelerateInterpolator()
    }

    AnimatorSet().apply {
        playTogether(rotateAnimator, scaleAnimator)
        addListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(animation: android.animation.Animator) {}
            override fun onAnimationCancel(animation: android.animation.Animator) {}
            override fun onAnimationRepeat(animation: android.animation.Animator) {}
            override fun onAnimationEnd(animation: android.animation.Animator) {
                onAnimationEnd()
            }
        })
        start()
    }
}