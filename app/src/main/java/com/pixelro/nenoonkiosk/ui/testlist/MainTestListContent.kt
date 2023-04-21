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
            title = "조절력 검사(안구 나이 검사)",
            description = "수정체의 탄력이 감소하여 가까이 있는 물체의 초점을 잘 맞추지 못하는 현상.",
            onClickMethod = toPreDescriptionScreen
        )
        EyeTestSelectableBox(
            title = "시력 검사",
            description = "내 눈의 시력이 어느 정도인지 알아봅니다.",
            onClickMethod = toVisualAcuityTestList
        )
        EyeTestSelectableBox(
            title = "황반변성 검사",
            description = "망막에 위치한 황반에 이상이 생기면 시력을 잃을 수 있습니다.\n조기 발견으로 예방하세요.",
            onClickMethod = toMacularDegenerationTestList
        )
    }
}