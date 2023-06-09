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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.ui.testlist.TestListContent
import kotlinx.coroutines.delay


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TestListScreen(
    toPreDescriptionScreen: () -> Unit,
    toSettingsScreen: () -> Unit,
    navController: NavHostController,
    viewModel: NenoonViewModel
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
            delay(5000)
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
                    top = (viewModel.statusBarPadding.collectAsState().value + 20).dp,
                    end = 40.dp,
                    bottom = 20.dp
                )
                .fillMaxWidth()
                .height(40.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                Image(
                    modifier = Modifier
                        .width(28.dp),
                    painter = painterResource(id = R.drawable.icon_back_black),
                    contentDescription = ""
                )
            }
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
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(100.dp),
//            horizontalArrangement = Arrangement.End,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Spacer(
//                modifier = Modifier.width(20.dp)
//            )
//            Text(
//                modifier = Modifier
//                    .padding(end = 20.dp),
//                text = "Pixelro",
//                fontSize = 30.sp
//            )
//        }
        HorizontalPager(
            contentPadding = PaddingValues(40.dp),
//            modifier = Modifier
//                .padding(20.dp),
            pageSpacing = 40.dp,
            state = pagerState,
            pageCount = 100000,
        ) {
            Advertisement(it)
        }
//        AutoScrollingLazyRow(list = (1..8).take(4)) {
//            LazyListItem(text = "Item $it")
//        }
        TestListContent(
            toPreDescriptionScreen = toPreDescriptionScreen,
            navController = navController,
            viewModel = viewModel
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
                        bottom = (viewModel.navigationBarPadding.collectAsState().value + 40).dp
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
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                modifier = Modifier
                    .clip(
                        shape = RoundedCornerShape(8.dp)
                    ),
                painter = painterResource(id = when(idx % 2) {
                    0 -> R.drawable.ad_1
                    else -> R.drawable.ad_2
                }),
                contentDescription = ""
            )
        }
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
