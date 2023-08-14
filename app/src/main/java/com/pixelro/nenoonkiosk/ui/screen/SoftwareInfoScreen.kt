package com.pixelro.nenoonkiosk.ui.screen

import android.app.Activity
import android.provider.Settings.Global
import android.view.KeyEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.GlobalValue
import com.pixelro.nenoonkiosk.data.StringProvider

@Composable
fun SoftwareInfoScreen() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .padding(top = (GlobalValue.statusBarPadding + 20).dp, bottom = 20.dp)
                .fillMaxWidth()
                .height(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        (context as Activity).dispatchKeyEvent(
                            KeyEvent(
                                KeyEvent.ACTION_DOWN,
                                KeyEvent.KEYCODE_BACK
                            )
                        )
                        context.dispatchKeyEvent(
                            KeyEvent(
                                KeyEvent.ACTION_UP,
                                KeyEvent.KEYCODE_BACK
                            )
                        )
                    },
                contentAlignment = Alignment.CenterStart
            ) {
                Image(
                    modifier = Modifier
                        .padding(start = 40.dp, top = 4.dp)
                        .width(28.dp),
                    painter = painterResource(id = R.drawable.icon_back_black),
                    contentDescription = ""
                )
            }
            Text(
                textAlign = TextAlign.Center,
                text = "소프트웨어 정보",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 20.dp)
        ) {
            Text(
                text = "품목명: 암슬러 격자검사 소프트웨어\n" +
                        "모델명: Screening Charts for Macular Degeneration\n" +
                        "제조업허가번호: \n" +
                        "제조업자의 상호 및 주소\n" +
                        "  상호: 주식회사 픽셀로\n" +
                        "  주소: 경기도 성남시 수정구 대왕판교로 815, 판교제2테크노밸리 기업지원허브 838호(시흥동, 판교창조경제밸리)\n" +
                        "품목허가번호: \n" +
                        "사용목적: 격자 형태의 표를 제시하여 시야의 중앙 및 중앙부 불규칙성을 검사하기 위해 사용하는 소프트웨어\n" +
                        "제조번호: \n" +
                        "제조연월일: \n" +
                        "성능 및 사용방법: 사용설명서 참조\n" +
                        "사용 시 주의사항: 사용설명서 참조\n" +
                        "보관 또는 저장방법: 사용설명서 참조\n" +
                        "소프트웨어 명칭 및 버전: 내눈 황반변성(암슬러차트) 검사, 1.0\n" +
                        "본 제품은 의료기기임.\n" +
                        "의료기기 표준코드(UDI): (01)8809685071280"
            )
            Image(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .width(200.dp)
                    .height(100.dp),
                painter = painterResource(id = R.drawable.udi_code),
                contentDescription = null
            )
        }
    }
}