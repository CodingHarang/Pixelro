package com.pixelro.nenoonkiosk.ui.testlist

import android.app.Activity
import android.view.KeyEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.data.TestType

@Composable
fun MacularTestListContent(
    toTestScreen: (TestType) -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .padding(bottom = 40.dp)
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(start = 40.dp, end = 20.dp, bottom = 20.dp)
                .clickable {
                    (context as Activity).dispatchKeyEvent(
                        KeyEvent(
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_BACK
                        )
                    )
                    context.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK))
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp),
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = ""
            )
            Spacer(
                modifier = Modifier
                    .width(10.dp)
            )
            Text(
                text = StringProvider.getString(R.string.test_list_go_back),
                fontSize = 24.sp,
                color = Color(0xFFAAAAAA)
            )
        }
//        EyeTestSelectableBox(
//            title = StringProvider.getString(R.string.amsler_grid_name),
//            description = StringProvider.getString(R.string.amsler_grid_short_description),
//            onClickMethod = { toTestScreen(TestType.AmslerGrid) }
//        )
//        Spacer(
//            modifier = Modifier
//                .height(20.dp)
//        )
//        EyeTestSelectableBox(
//            title = StringProvider.getString(R.string.mchart_name),
//            description = StringProvider.getString(R.string.mchart_short_description),
//            onClickMethod = { toTestScreen(TestType.MChart) }
//        )
    }
}