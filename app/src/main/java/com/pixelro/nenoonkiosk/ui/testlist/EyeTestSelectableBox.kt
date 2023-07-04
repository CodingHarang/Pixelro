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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pixelro.nenoonkiosk.R


@Composable
fun EyeTestSelectableBox(
    title: String,
    description: String,
    onClickMethod: () -> Unit,
    expanded: Boolean
) {
    Card(
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = Modifier
            .padding(start = 40.dp, end = 40.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(1.dp, Color(0xffc3c3c3)),
                shape = RoundedCornerShape(8.dp)
            )
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onClickMethod()
                },
            contentAlignment = Alignment.CenterEnd
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    modifier = Modifier
                        .padding(start = 28.dp, top = 20.dp, end = 80.dp),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                if (expanded) {
                    Text(
                        description,
                        modifier = Modifier
                            .height(60.dp)
                            .padding(start = 28.dp, top = 8.dp, end = 80.dp),
                        fontSize = 20.sp,
                        color = Color(0xff919191)
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                )
            }
//            Image(
//                modifier = Modifier
//                    .padding(end = 40.dp)
//                    .width(24.dp)
//                    .rotate(270f),
//                painter = painterResource(id = R.drawable.icon_back_black),
//                contentDescription = ""
//            )
        }
    }
}