package com.pixelro.nenoonkiosk.ui

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.ui.testlist.TestListContent


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TestListScreen(
    toPreDescriptionScreen: () -> Unit,
    navController: NavHostController,
    viewModel: NenoonViewModel
) {
    Column() {
        Row() {
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_2_1),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_2_2),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_2_3),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_2_4),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_2_5),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_2_6),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_2_7),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_2_8),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_2_9),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_2_10),
                contentDescription = ""
            )
        }
        Row() {
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_3_1),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_3_2),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_3_3),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_3_4),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_3_5),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_3_6),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_3_7),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_3_8),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_3_9),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_3_10),
                contentDescription = ""
            )
        }
        Row() {
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_4_1),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_4_2),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_4_3),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_4_4),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_4_5),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_4_6),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_4_7),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_4_8),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_4_9),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_4_10),
                contentDescription = ""
            )
        }
        Row() {
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_5_1),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_5_2),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_5_3),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_5_4),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_5_5),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_5_6),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_5_7),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_5_8),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_5_9),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_5_10),
                contentDescription = ""
            )
        }
        Row() {
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_6_1),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_6_2),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_6_3),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_6_4),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_6_5),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_6_6),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_6_7),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_6_8),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_6_9),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_6_10),
                contentDescription = ""
            )
        }
        Row() {
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_7_1),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_7_2),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_7_3),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_7_4),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_7_5),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_7_6),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_7_7),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_7_8),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_7_9),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .padding(20.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable._50cm_7_10),
                contentDescription = ""
            )
        }
        HorizontalPager(
            state = rememberPagerState(
                initialPage = 500000
            ),
            pageCount = 1000000,
        ) {
            Advertisement()
        }
//        AutoScrollingLazyRow(list = (1..8).take(4)) {
//            LazyListItem(text = "Item $it")
//        }
        TestListContent(
            toPreDescriptionScreen = toPreDescriptionScreen,
            navController = navController,
            viewModel = viewModel
        )
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color(0xff999999),
                        fontSize = 20.sp
                    )
                ) {
                    append("내눈 앱의 모든 눈 검사는 자가 검사로 ")
                }
                withStyle(
                    style = SpanStyle(
                        color = Color(0xffff0000),
                        fontSize = 20.sp
                    )
                ) {
                    append("의학적 소견이 아니기에\n")
                }
                withStyle(
                        style = SpanStyle(
                            color = Color(0xff999999),
                            fontSize = 20.sp
                        )
                ) {
                    append("정확한 진단은 병원을 방문하여 의사와 상담하시길 바랍니다.\n내 눈의 상태와 주변 환경 또는 기기에 따라 검사 결과가 달라질 수 있습니다.")
                }
            },
            modifier = Modifier
                .padding(20.dp)
        )
    }
}

@Composable
fun Advertisement() {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .padding(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xffffffff)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.home_banner_shinhan_ko_01),
            contentDescription = ""
        )
    }
}

//@Composable
//fun LazyListItem(text: String) {
//    Box(
//        modifier = Modifier
//            .padding(12.dp)
//            .size(150.dp)
//            .background(
//                color = Color.White,
//                shape = RoundedCornerShape(8.dp)
//            ),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = text,
//            fontSize = 24.sp
//        )
//    }
//}
