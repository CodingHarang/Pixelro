package com.pixelro.nenoonkiosk.ui.testscreen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.pixelro.nenoonkiosk.NenoonViewModel

@Composable
fun ChildrenVisualAcuityTestScreen(
    navController: NavHostController,
    viewModel: NenoonViewModel
) {
    Text("ChildrenVisualAcuityTestScreen")
}