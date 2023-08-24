package com.pixelro.nenoonkiosk.ui.testlist

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.ui.theme.nanumSquareNeoFamily


@Composable
fun EyeTestSelectableBox(
    modifier: Modifier,
    title: String,
    onClickMethod: () -> Unit,
    isDone: Boolean,
    time: Int
) {
    Card(
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = modifier
            .padding(start = 40.dp, end = 40.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(1.dp, Color(0xffc3c3c3)),
                shape = RoundedCornerShape(8.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onClickMethod()
                },
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = title,
                modifier = Modifier
                    .padding(start = 28.dp),
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                color = when(isDone) {
                    true -> Color(0xff999999)
                    false -> Color(0xff000000)
                },
                fontFamily = nanumSquareNeoFamily
            )
            Image(
                modifier = Modifier
                    .padding(end = 40.dp)
                    .rotate(180f)
                    .align(Alignment.CenterEnd),
                painter = painterResource(id = R.drawable.icon_back_black),
                contentDescription = ""
            )
            Column(
                modifier = Modifier
                    .padding(end = 160.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier
                        .width(100.dp),
                    text = "소요 시간",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    modifier = Modifier
                        .width(100.dp),
                    text = "약 ${time}분",
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}