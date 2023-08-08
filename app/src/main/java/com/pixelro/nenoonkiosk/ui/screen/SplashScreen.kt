package com.pixelro.nenoonkiosk.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.GlobalValue
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.ui.theme.cafe24Family
import com.pixelro.nenoonkiosk.ui.theme.notoSansKrFamily

@Composable
fun SplashScreen() {
    val systemUiController = rememberSystemUiController()
    DisposableEffect(true) {
        systemUiController.systemBarsDarkContentEnabled = false
        onDispose {
            systemUiController.systemBarsDarkContentEnabled = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xff1d71e1)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = StringProvider.getString(R.string.splash_description),
                color = Color(0xffffffff),
                fontSize = 28.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(
                modifier = Modifier
                    .height(28.dp)
            )
            Image(
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp),
                painter = painterResource(id = R.drawable.nenoon_logo_white),
                contentDescription = ""
            )
            Text(
                text = StringProvider.getString(R.string.splash_app_name),
                color = Color(0xffffffff),
                fontSize = 92.sp,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.body1,
                fontFamily = cafe24Family
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ver 1.0",
                color = Color(0xFFFFFFFF)
            )
            Image(
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 40.dp)
                    .height(50.dp),
                painter = painterResource(id = R.drawable.pixelro_logo),
                contentDescription = null
            )
            Image(
                modifier = Modifier
                    .padding(bottom = (GlobalValue.navigationBarPadding + 20).dp)
                    .width(200.dp)
                    .height(100.dp),
                painter = painterResource(id = R.drawable.udi_code),
                contentDescription = null
            )
        }
    }
}