package com.pixelro.nenoonkiosk.ui

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.pixelro.nenoonkiosk.NenoonViewModel

@Composable
fun ScreenSaverScreen(
    viewModel: NenoonViewModel
) {
    val context = LocalContext.current
    val exoPlayer = viewModel.exoPlayer
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        DisposableEffect(key1 = Unit) {
            exoPlayer.play()
            onDispose {
                exoPlayer.pause()
            }
        }
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .height(600.dp)
                .background(
                    color = Color(0xff000000)
                ),
            factory = {
                PlayerView(context).apply {
                    player = exoPlayer
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    useController = false
                }
            }
        )
    }
}