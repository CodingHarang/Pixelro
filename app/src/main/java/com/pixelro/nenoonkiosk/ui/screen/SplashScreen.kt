package com.pixelro.nenoonkiosk.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.GlobalValue
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.ui.theme.cafe24Family
import com.pixelro.nenoonkiosk.ui.theme.notoSansKrFamily

@Composable
fun SplashScreen() {
    val systemUiController = rememberSystemUiController()
    
    DisposableEffect(true) {
        systemUiController.systemBarsDarkContentEnabled = false
        onDispose {
            systemUiController.systemBarsDarkContentEnabled = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xff1d71e1)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = StringProvider.getString(R.string.splash_description),
                color = Color(0xffffffff),
                fontSize = 28.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(
                modifier = Modifier
                    .height(28.dp)
            )
            Image(
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp),
                painter = painterResource(id = R.drawable.nenoon_logo_white),
                contentDescription = ""
            )
            Text(
                text = StringProvider.getString(R.string.splash_app_name),
                color = Color(0xffffffff),
                fontSize = 92.sp,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.body1,
                fontFamily = cafe24Family
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Text(
//                modifier = Modifier
//                    .padding(40.dp),
//                text = "품목명: 암슬러 격자검사 소프트웨어\n" +
//                        "모델명: Screening Charts for Macular Degeneration\n" +
//                        "제조업허가번호: 제 8237호\n" +
//                        "제조업자의 상호 및 주소\n" +
//                        "  상호: 주식회사 픽셀로\n" +
//                        "  주소: 경기도 성남시 수정구 대왕판교로 815, 판교제2테크노밸리 기업지원허브 838호(시흥동, 판교창조경제밸리)\n" +
//                        "사용목적: 격자 형태의 표를 제시하여 시야의 중앙 및 중앙부 불규칙성을 검사하기 위해 사용하는 소프트웨어\n" +
//                        "제조번호: PX1-23-1\n" +
//                        "제조연월일: 2023.06.01\n" +
//                        "성능 및 사용방법: 사용설명서 참조\n" +
//                        "사용 시 주의사항: 사용설명서 참조\n" +
//                        "보관 또는 저장방법: 사용설명서 참조\n" +
//                        "소프트웨어 명칭 및 버전: 내눈 황반변성(암슬러차트) 검사, ver. 1.0\n" +
//                        "본 제품은 의료기기임.\n",
//                fontSize = 20.sp
//            )
//            Image(
//                modifier = Modifier
//                    .padding(top = 20.dp)
//                    .width(200.dp)
//                    .height(100.dp),
//                painter = painterResource(id = R.drawable.udi_code),
//                contentDescription = null
//            )
            Image(
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 40.dp)
                    .height(50.dp),
                painter = painterResource(id = R.drawable.pixelro_logo),
                contentDescription = null
            )
        }
    }
}