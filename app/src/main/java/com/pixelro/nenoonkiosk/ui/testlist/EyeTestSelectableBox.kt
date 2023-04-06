package com.pixelro.nenoonkiosk.ui.testlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun EyeTestSelectableBox(
    title: String,
    description: String,
    onClickMethod: () -> Unit
) {
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .padding(start = 20.dp, top = 10.dp, end = 20.dp)
            .fillMaxWidth()
            .clickable {
                onClickMethod()
            }
    ) {
        Box() {
            Column(
                modifier = Modifier
                    .height(150.dp)
            ) {
                Text(
                    title,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 10.dp),
                    fontSize = 30.sp
                )
                Text(
                    description,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
                    fontSize = 20.sp
                )
            }
        }
    }
}