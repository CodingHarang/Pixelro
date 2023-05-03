package com.pixelro.nenoonkiosk.ui.testresultcontent

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
        text = "${leftSelectedArea}, ${rightSelectedArea}"
    )
}