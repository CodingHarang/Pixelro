package com.pixelro.nenoonkiosk.ui.testlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pixelro.nenoonkiosk.NenoonViewModel

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