package com.pixelro.nenoonkiosk.ui.testscreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.data.GlobalConstants
import com.pixelro.nenoonkiosk.data.TestType
import com.pixelro.nenoonkiosk.ui.testresultcontent.*

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
                viewModel = viewModel
            )
        }
        TestType.ShortDistanceVisualAcuity -> {
            ShortDistanceVisualAcuityTestResultContent(
                viewModel = viewModel
            )
        }
        TestType.LongDistanceVisualAcuity -> {
            LongDistanceVisualAcuityTestResultContent(
                viewModel = viewModel
            )
        }
        TestType.ChildrenVisualAcuity -> {
            ChildrenVisualAcuityTestResultContent(
                viewModel = viewModel
            )
        }
        TestType.AmslerGrid -> {
            AmslerGridTestResultContent(
                viewModel = viewModel
            )

        }
        TestType.MChart -> {
            MChartTestResultContent(
                viewModel = viewModel
            )
        }
        else -> {
            Text("None TestResultScreen")
        }
    }
}