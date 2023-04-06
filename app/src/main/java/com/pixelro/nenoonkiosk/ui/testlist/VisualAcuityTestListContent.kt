package com.pixelro.nenoonkiosk.ui.testlist

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pixelro.nenoonkiosk.NenoonViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.pixelro.nenoonkiosk.data.GlobalConstants

@Composable
fun VisualAcuityTestListContent(
    toShortDistanceVisualAcuityTest: () -> Unit,
    toLongDistanceVisualAcuityTest: () -> Unit,
    toChildrenVisualAcuityTest: () -> Unit,
    toPreDescriptionScreen: () -> Unit,
    viewModel: NenoonViewModel = viewModel()
) {
    Column(
        modifier = Modifier.
        padding(bottom = 10.dp)
    ) {
        EyeTestSelectableBox(
            title = "near",
            description = "description",
            onClickMethod = {
                toShortDistanceVisualAcuityTest()
                toPreDescriptionScreen()
            }
        )
        EyeTestSelectableBox(
            title = "far",
            description = "description",
            onClickMethod = {
                toLongDistanceVisualAcuityTest()
                toPreDescriptionScreen()
            }
        )
        EyeTestSelectableBox(
            title = "child",
            description = "description",
            onClickMethod = {
                toChildrenVisualAcuityTest()
                toPreDescriptionScreen()
            }
        )
    }
}