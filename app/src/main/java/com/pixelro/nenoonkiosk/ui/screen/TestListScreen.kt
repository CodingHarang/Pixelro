package com.pixelro.nenoonkiosk.ui.screen

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import kotlinx.coroutines.yield


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TestListScreen(
    toPreDescriptionScreen: () -> Unit,
    toSettingsScreen: () -> Unit,
    navController: NavHostController,
    viewModel: NenoonViewModel
) {
    val pagerState = rememberPagerState(
        initialPage = 50000
    )
    LaunchedEffect(true) {
        while(true) {
//            yield()
            delay(2000)
            pagerState.animateScrollToPage(
                page = (pagerState.currentPage + 1),
                animationSpec = tween(600)
            )
        }
    }
    Column(
        modifier = Modifier
            .background(
                color = Color(0xffffffff)
            )
    ) {
        Spacer(
            modifier = Modifier.height(20.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .clickable {
                        toSettingsScreen()
                    },
                painter = painterResource(id = R.drawable.baseline_settings_48),
                contentDescription = ""
            )
            Spacer(
                modifier = Modifier.width(20.dp)
            )
            Text(
                modifier = Modifier
                    .padding(end = 20.dp),
                text = "Pixelro",
                fontSize = 30.sp
            )
        }
        HorizontalPager(
            contentPadding = PaddingValues(20.dp),
//            modifier = Modifier
//                .padding(20.dp),
            pageSpacing = 20.dp,
            state = pagerState,
            pageCount = 100000,
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
                    append(StringProvider.getString(R.string.test_list_screen_warning1))
                }
                withStyle(
                    style = SpanStyle(
                        color = Color(0xffff0000),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(StringProvider.getString(R.string.test_list_screen_warning2))
                }
                withStyle(
                        style = SpanStyle(
                            color = Color(0xff999999),
                            fontSize = 20.sp
                        )
                ) {
                    append(StringProvider.getString(R.string.test_list_screen_warning3))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
        )
    }
}

@Composable
fun Advertisement() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
//            .padding(20.dp),
        elevation = CardDefaults.cardElevation(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xffffffff)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {

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
