package com.pixelro.nenoonkiosk.ui.testlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun EyeTestSelectableBox(
    title: String,
    description: String,
    onClickMethod: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = Modifier
//            .clip(RoundedCornerShape(16.dp))
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFC9D5FF)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onClickMethod()
                }
        ) {
            Column(
                modifier = Modifier
                    .height(150.dp)
            ) {
                Text(
                    text = title,
                    modifier = Modifier
                        .padding(start = 20.dp, top = 20.dp),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                )
                Text(
                    description,
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp),
                    fontSize = 20.sp
                )
            }
        }
    }
}