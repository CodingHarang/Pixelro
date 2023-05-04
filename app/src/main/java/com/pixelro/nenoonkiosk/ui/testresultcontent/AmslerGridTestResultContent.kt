package com.pixelro.nenoonkiosk.ui.testresultcontent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pixelro.nenoonkiosk.NenoonViewModel

@Composable
fun AmslerGridTestResultContent(
    viewModel: NenoonViewModel,
    navController: NavHostController
) {
    val leftSelectedArea = viewModel.leftSelectedArea.collectAsState().value
    val rightSelectedArea = viewModel.rightSelectedArea.collectAsState().value
    Text(
        text = "${leftSelectedArea}\n ${rightSelectedArea}"
    )
    Column(
        modifier = Modifier
            .width(300.dp)
            .height(300.dp)
            .padding(start = 80.dp, end = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .width(300.dp)
                .height(300.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .width(300.dp)
                    .height(100.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = Color(0xffff0000)
                        )
                ) {

                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = Color(0xff00ff00)
                        )
                ) {

                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = Color(0xffff00ff)
                        )
                ) {

                }
            }
            Row(
                modifier = Modifier
                    .weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = Color(0xffff0000)
                        )
                ) {

                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = Color(0xff00ff00)
                        )
                ) {

                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = Color(0xffff00ff)
                        )
                ) {

                }
            }
            Row(
                modifier = Modifier
                    .weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = Color(0xffff0000)
                        )
                ) {

                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = Color(0xff00ff00)
                        )
                ) {

                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = Color(0xffff00ff)
                        )
                ) {

                }
            }
        }
    }
}