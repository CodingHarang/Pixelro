package com.pixelro.nenoonkiosk.ui.screen

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pixelro.nenoonkiosk.NenoonViewModel

@OptIn(ExperimentalTextApi::class)
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun ScreenSaverScreen(
    viewModel: NenoonViewModel
) {
    val transition = rememberInfiniteTransition()
    val shiftVal by transition.animateFloat(
        initialValue = 0f, targetValue = 0.5f, animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
            },
            repeatMode = RepeatMode.Reverse
        )
    )
    val text = buildAnnotatedString {
        withAnnotation("squiggles", annotation = "ignored") {
            withStyle(
                SpanStyle(
                    color = Color(0xff1d71e1),
                    baselineShift = BaselineShift(shiftVal)
                )
            ) {
                append("Touch")
            }
        }
        append(" to Start an\nEye Test!")
    }
    val systemUiController = rememberSystemUiController()
    val context = LocalContext.current
    val exoPlayer = viewModel.exoPlayer
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xff000000)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DisposableEffect(true) {
            systemUiController.systemBarsDarkContentEnabled = false
            exoPlayer.play()
            onDispose {
                systemUiController.systemBarsDarkContentEnabled = true
                exoPlayer.pause()
            }
        }
        Spacer(
            modifier = Modifier
                .padding(top = viewModel.statusBarPadding.collectAsState().value.dp)
        )
        Box() {
            Column(
                modifier = Modifier
                    .height(300.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 20.dp),
                    text = text,
                    color = Color(0xffffffff),
                    fontWeight = FontWeight.Bold,
                    fontSize = 80.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .background(
                    color = Color(0xff000000)
                ),
            factory = {
                PlayerView(context).apply {
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    player = exoPlayer
                    useController = false
                }
            }
        )
        Spacer(
            modifier = Modifier
                .height(300.dp)
        )
    }
}