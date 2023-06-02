package com.pixelro.nenoonkiosk.data

import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.ui.unit.IntOffset

object AnimationProvider {
    val enterTransition = slideIn(
    animationSpec = TweenSpec(durationMillis = 800),
    initialOffset = { IntOffset(100, 0) }
) + fadeIn(
    animationSpec = TweenSpec(durationMillis = 800)
)
    val exitTransition = slideOut(
        animationSpec = TweenSpec(durationMillis = 800),
        targetOffset = { IntOffset(-100, 0) }
    ) + fadeOut(
        animationSpec = TweenSpec(durationMillis = 800)
    )
}