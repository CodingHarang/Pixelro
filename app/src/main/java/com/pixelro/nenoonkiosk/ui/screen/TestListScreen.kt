package com.pixelro.nenoonkiosk.ui.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.GlobalValue
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.data.TestType
import com.pixelro.nenoonkiosk.ui.testlist.TestListContent
import kotlinx.coroutines.delay


@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun TestListScreen(
    toTestScreen: (TestType) -> Unit,
    toSettingsScreen: () -> Unit,
    toSurveyScreen: () -> Unit,
    viewModel: NenoonViewModel,
    navController: NavHostController = rememberAnimatedNavController()
) {
//    BackHandler(enabled = true) {
//        viewModel.resetScreenSaverTimer()
//        Log.e("backhandler", "backhandler")
//    }
    val pagerState = rememberPagerState(
        initialPage = 50000
    )
    LaunchedEffect(true) {
        while(true) {
//            yield()
            delay(8000)
            pagerState.animateScrollToPage(
                page = (pagerState.currentPage + 1),
                animationSpec = tween(1000)
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xffffffff)
            )
    ) {
        Box(
            modifier = Modifier
                .padding(
                    start = 40.dp,
                    top = (GlobalValue.statusBarPadding + 20).dp,
                    end = 40.dp,
                    bottom = 20.dp
                )
                .fillMaxWidth()
                .height(40.dp)
        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .clickable {
//                        toSurveyScreen()
//                    },
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Image(
//                    modifier = Modifier
//                        .padding(top = 4.dp)
//                        .width(28.dp),
//                    painter = painterResource(id = R.drawable.icon_back_black),
//                    contentDescription = ""
//                )
//                Text(
//                    text = "문진하러 가기",
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.Medium
//                )
//            }
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = StringProvider.getString(R.string.test_list),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Image(
                    modifier = Modifier
                        .width(32.dp)
                        .height(32.dp)
                        .clickable {
                            toSettingsScreen()
                        },
                    painter = painterResource(id = R.drawable.icon_settings ),
                    contentDescription = ""
                )
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    color = Color(0xffebebeb)
                )
        )
        HorizontalPager(
            contentPadding = PaddingValues(start = 40.dp, top = 20.dp, end = 40.dp, bottom = 20.dp),
            pageSpacing = 40.dp,
            state = pagerState,
            pageCount = 100000,
        ) {
            Advertisement(it)
        }
        TestListContent(
            toTestScreen = toTestScreen
        )
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                modifier = Modifier
                    .padding(
                        start = 40.dp,
                        bottom = (GlobalValue.navigationBarPadding + 40).dp
                    )
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .width(44.dp),
                    painter = painterResource(id = R.drawable.icon_warning),
                    contentDescription = ""
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 40.dp),
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xff999999),
                                fontSize = 16.sp
                            )
                        ) {
                            append(StringProvider.getString(R.string.test_list_screen_warning1))
                        }
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xffff0000),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(StringProvider.getString(R.string.test_list_screen_warning2))
                        }
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xff999999),
                                fontSize = 16.sp
                            )
                        ) {
                            append(StringProvider.getString(R.string.test_list_screen_warning3))
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun Advertisement(
    idx: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
//            .padding(20.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xffffffff)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        shape = RoundedCornerShape(8.dp)
                    ),
                painter = painterResource(id = when(idx % 3) {
                    0 -> R.drawable.ad_1
                    1 -> R.drawable.ad_2
                    else -> R.drawable.ad_3
                }),
                contentScale = ContentScale.FillWidth,
                contentDescription = ""
            )
        }
    }
}
