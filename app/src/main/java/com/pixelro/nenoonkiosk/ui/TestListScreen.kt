package com.pixelro.nenoonkiosk.ui

import TestListContent
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TestListScreen(
    toPreDescriptionScreen: () -> Unit,
    navController: NavHostController,
    viewModel: NenoonViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = rememberPagerState(
                initialPage = 500000
            ),
            pageCount = 1000000
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
    }
}

@Composable
fun Advertisement() {
    Card(
        elevation = 8.dp,
        modifier = Modifier
            .padding(20.dp)
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
