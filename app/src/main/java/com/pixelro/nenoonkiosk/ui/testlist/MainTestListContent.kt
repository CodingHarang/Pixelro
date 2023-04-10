package com.pixelro.nenoonkiosk.ui.testlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainTestListContent(
    toPreDescriptionScreen: () -> Unit,
    toVisualAcuityTestList: () -> Unit,
    toMacularDegenerationTestList: () -> Unit
) {
    Column(
        modifier = Modifier.padding(bottom = 10.dp)
    ) {
        EyeTestSelectableBox(
            title = "Presbyopia Test",
            description = "description",
            onClickMethod = toPreDescriptionScreen
        )
        EyeTestSelectableBox(
            title = "Visual acuity Test",
            description = "description",
            onClickMethod = toVisualAcuityTestList
        )
        EyeTestSelectableBox(
            title = "Macular degeneration test",
            description = "description",
            onClickMethod = toMacularDegenerationTestList
        )
    }
}