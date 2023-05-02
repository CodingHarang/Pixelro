package com.pixelro.nenoonkiosk.ui.testresultcontent

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pixelro.nenoonkiosk.NenoonViewModel
import java.lang.Math.round
import kotlin.math.roundToInt

@Composable
fun PresbyopiaTestResultContent(
    viewModel: NenoonViewModel,
    navController: NavHostController
) {
    viewModel.updateAvgDistance()
    Text(
        modifier = Modifier
            .fillMaxWidth(),
        text = "조절력: ${(viewModel.avgDistance.collectAsState().value).roundToInt().toFloat() / 10}cm",
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
}