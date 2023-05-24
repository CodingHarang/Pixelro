package com.pixelro.nenoonkiosk.ui.screen

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
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R

@Composable
fun SplashScreen(
    viewModel: NenoonViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xff1d71e1)
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                modifier = Modifier
                    .height(150.dp)
                    .padding(bottom = 40.dp),
                painter = painterResource(id = R.drawable.pixelro_logo),
                contentDescription = ""
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier
                    .height(200.dp)
            )
            Text(
                text = "Vision Test and Eye care Solution",
                color = Color(0xffffffff),
                fontSize = 40.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(
                modifier = Modifier
                    .height(80.dp)
            )
            Image(
                modifier = Modifier
                    .width(350.dp)
                    .height(350.dp),
                painter = painterResource(id = R.drawable.nenoon_logo),
                contentDescription = ""
            )
            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )
            Text(
                text = "NENOON",
                color = Color(0xffffffff),
                fontSize = 96.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}