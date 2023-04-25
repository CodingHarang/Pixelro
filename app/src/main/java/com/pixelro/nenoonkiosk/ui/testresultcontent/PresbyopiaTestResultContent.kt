package com.pixelro.nenoonkiosk.ui.testresultcontent

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.pixelro.nenoonkiosk.NenoonViewModel

@Composable
fun PresbyopiaTestResultContent(
    viewModel: NenoonViewModel,
    navController: NavHostController
) {
    viewModel.updateAvgDistance()
    Text(
        text = "${viewModel.avgDistance.collectAsState().value / 10}"
    )
}