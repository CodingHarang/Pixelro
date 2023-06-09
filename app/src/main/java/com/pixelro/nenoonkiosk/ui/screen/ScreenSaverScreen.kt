package com.pixelro.nenoonkiosk.ui.screen

import android.view.ViewGroup
import android.widget.FrameLayout
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.AspectRatioFrameLayout.ResizeMode
import androidx.media3.ui.PlayerView
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pixelro.nenoonkiosk.NenoonViewModel

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun ScreenSaverScreen(
    viewModel: NenoonViewModel
) {
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
        DisposableEffect(key1 = Unit) {
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
        Text(
            modifier = Modifier
                .padding(bottom = 20.dp),
            text = "Touch to Start an\nEye Test!",
            color = Color(0xffffffff),
            fontWeight = FontWeight.Bold,
            fontSize = 80.sp,
            textAlign = TextAlign.Center
        )
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
//                    layoutParams = FrameLayout.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT
//                    )
                    useController = false
                }
            }
        )
    }
}