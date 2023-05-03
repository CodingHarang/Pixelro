package com.pixelro.nenoonkiosk.ui.testresultcontent

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.pixelro.nenoonkiosk.NenoonViewModel

@Composable
fun MChartTestResultContent(
    viewModel: NenoonViewModel,
    navController: NavHostController
) {
    val result = viewModel.savedResult.collectAsState().value
    Text(
        text = "${result}"
    )
}