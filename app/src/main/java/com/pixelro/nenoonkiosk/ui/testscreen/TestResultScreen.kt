package com.pixelro.nenoonkiosk.ui.testscreen

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.data.GlobalConstants
import com.pixelro.nenoonkiosk.data.TestType
import com.pixelro.nenoonkiosk.ui.testresultcontent.*
import mangoslab.nemonicsdk.nemonicWrapper

@Composable
fun TestResultScreen(
    viewModel: NenoonViewModel,
    navController: NavHostController
) {
    BackHandler(enabled = true) {
        navController.popBackStack(GlobalConstants.ROUTE_TEST_LIST, false)
    }
    when (viewModel.selectedTestType.collectAsState().value) {
        TestType.Presbyopia -> {
            PresbyopiaTestResultContent(
                viewModel = viewModel,
                navController = navController
            )
        }
        TestType.ShortDistanceVisualAcuity -> {
            ShortDistanceVisualAcuityTestResultContent(
                viewModel = viewModel,
                navController = navController
            )
        }
        TestType.LongDistanceVisualAcuity -> {
            LongDistanceVisualAcuityTestResultContent(
                viewModel = viewModel,
                navController = navController
            )
        }
        TestType.ChildrenVisualAcuity -> {
            ChildrenVisualAcuityTestResultContent(
                viewModel = viewModel,
                navController = navController
            )
        }
        TestType.AmslerGrid -> {
            AmslerGridTestResultContent(
                viewModel = viewModel,
                navController = navController
            )

        }
        TestType.MChart -> {
            MChartTestResultContent(
                viewModel = viewModel,
                navController = navController
            )
        }
        else -> {
            Text("None TestResultScreen")
        }
    }
}