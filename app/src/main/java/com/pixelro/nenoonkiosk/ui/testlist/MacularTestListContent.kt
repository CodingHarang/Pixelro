package com.pixelro.nenoonkiosk.ui.testlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
            title = "암슬러 차트",
            description = "암슬러 차트를 이용하여 이상이 있는 곳의 범위를 알아냅니다.",
            onClickMethod = {
                toAmslerGridTest()
                toPreDescriptionScreen()
            }
        )
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        EyeTestSelectableBox(
            title = "엠식 변형시 검사",
            description = "수직 및 수평선의 도트 간격을 이용하여 시각 왜곡 정도를 측정합니다.",
            onClickMethod = {
                toMChartTest()
                toPreDescriptionScreen()
            }
        )
    }
}