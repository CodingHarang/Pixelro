package com.pixelro.nenoonkiosk.ui.testscreen

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.pixelro.nenoonkiosk.NenoonViewModel

@Composable
fun LongDistanceVisualAcuityTestScreen(
    toResultScreen: () -> Unit,
    viewModel: NenoonViewModel
) {
    Text("LongDistanceVisualAcuityTestScreen")
}