package com.pixelro.nenoonkiosk.ui.screen

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.ui.theme.cafe24Family

@Composable
fun IntroScreen(
    toPrimaryTestListScreen: () -> Unit
) {
    val transition = rememberInfiniteTransition()
    val alphaVal by transition.animateFloat(
        initialValue = 1f, targetValue = 0f, animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 700
            },
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "내눈\n눈 건강 지킴이",
                fontSize = 100.sp,
                fontFamily = cafe24Family,

                textAlign = TextAlign.Center,
                color = Color(0xFF1D71E1)
            )
            Spacer(
                modifier = Modifier
                    .height(40.dp)
            )
            Image(
                painter = painterResource(R.drawable.nenoon_logo_blue),
                contentDescription = null
            )
            Spacer(
                modifier = Modifier
                    .height(200.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(520.dp)
                        .height(240.dp)
                        .alpha(alphaVal)
                        .border(
                            border = BorderStroke(20.dp, Color(0xFF1D71E1)),
                            shape = RoundedCornerShape(26.dp)
                        )
                )
                Box(
                    modifier = Modifier
                        .width(440.dp)
                        .height(160.dp)
                        .clip(
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(
                            color = Color(0xFF1D71E1),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            toPrimaryTestListScreen()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "시작",
                        fontSize = 100.sp,
                        color = Color(0xFFFFFFFF),
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}