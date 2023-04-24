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
            title = "근거리 시력 검사",
            description = "내 눈의 시력이 어느정도인지 알아볼 수 있어요.",
            onClickMethod = {
                toShortDistanceVisualAcuityTest()
                toPreDescriptionScreen()
            }
        )
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        EyeTestSelectableBox(
            title = "원거리 시력 검사",
            description = "내 눈의 시력이 어느정도인지 알아볼 수 있어요.",
            onClickMethod = {
                toLongDistanceVisualAcuityTest()
                toPreDescriptionScreen()
            }
        )
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        EyeTestSelectableBox(
            title = "어린이 시력 검사",
            description = "내 눈의 시력이 어느정도인지 알아볼 수 있어요.",
            onClickMethod = {
                toChildrenVisualAcuityTest()
                toPreDescriptionScreen()
            }
        )
    }
}