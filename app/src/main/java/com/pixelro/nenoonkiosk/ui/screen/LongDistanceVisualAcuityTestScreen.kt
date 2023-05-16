package com.pixelro.nenoonkiosk.ui.screen

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.pixelro.nenoonkiosk.NenoonViewModel

@Composable
fun LongDistanceVisualAcuityTestScreen(
    toResultScreen: () -> Unit,
    viewModel: NenoonViewModel
) {
    Text("LongDistanceVisualAcuityTestScreen")
}