package com.pixelro.nenoonkiosk.ui.testlist

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.pixelro.nenoonkiosk.NenoonViewModel

@Composable
fun MacularTestListContent(
    toAmslerGridTest: () -> Unit,
    toMChartTest: () -> Unit,
    toPreDescriptionScreen: () -> Unit,
    viewModel: NenoonViewModel = viewModel()
) {
    Column(
        modifier = Modifier.
        padding(bottom = 10.dp)
    ) {
        EyeTestSelectableBox(
            title = "AmslerGrid",
            description = "description",
            onClickMethod = {
                toAmslerGridTest()
                toPreDescriptionScreen()
            }
        )
        EyeTestSelectableBox(
            title = "M-Chart",
            description = "description",
            onClickMethod = {
                toMChartTest()
                toPreDescriptionScreen()
            }
        )
    }
}