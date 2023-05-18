package com.pixelro.nenoonkiosk.ui.testlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.StringProvider

@Composable
fun MainTestListContent(
    toPreDescriptionScreen: () -> Unit,
    toVisualAcuityTestList: () -> Unit,
    toMacularDegenerationTestList: () -> Unit
) {
    Column(
        modifier = Modifier.padding(bottom = 40.dp)
    ) {
        EyeTestSelectableBox(
            title = StringProvider.getString(R.string.presbyopia_name1),
            description = StringProvider.getString(R.string.presbyopia_short_description),
            onClickMethod = toPreDescriptionScreen
        )
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        EyeTestSelectableBox(
            title = StringProvider.getString(R.string.visual_acuity_name),
            description = StringProvider.getString(R.string.visual_acuity_short_description),
            onClickMethod = toVisualAcuityTestList
        )
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        EyeTestSelectableBox(
            title = StringProvider.getString(R.string.macular_degeneration_name),
            description = StringProvider.getString(R.string.macular_degeneration_short_script),
            onClickMethod = toMacularDegenerationTestList
        )
    }
}