package com.pixelro.nenoonkiosk.ui.testscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pixelro.nenoonkiosk.R

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xff1d71e1)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .height(300.dp),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = ""
            )
            Spacer(
                modifier = Modifier
                    .height(40.dp)
            )
            Text(
                text = "내눈",
                color = Color(0xffffffff),
                fontSize = 96.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}