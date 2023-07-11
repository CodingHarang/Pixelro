package com.pixelro.nenoonkiosk.test.dementia

import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.RowScopeInstance.weight
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.GlobalValue
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.test.presbyopia.PresbyopiaTestResult
import com.pixelro.nenoonkiosk.ui.testlist.EyeTestSelectableBox
import kotlin.math.roundToInt

@Composable
fun DementiaTestResultContent(
    testResult: DementiaTestResult,
    navController: NavHostController
) {
    val isWebViewShowing1 = remember { mutableStateOf(false) }
    val isWebViewShowing2 = remember { mutableStateOf(false) }
    val isWebViewShowing3 = remember { mutableStateOf(false) }

    when {
        isWebViewShowing1.value -> {
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
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .clickable {
                                isWebViewShowing1.value = false
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .width(28.dp),
                            painter = painterResource(id = R.drawable.icon_back_black),
                            contentDescription = ""
                        )
                        Text(
                            text = "뒤로 가기",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                TheContent( "https://www.nid.or.kr/info/facility_list.aspx?gubun_str=%7C3%7C" )
            }
        }

        isWebViewShowing2.value -> {
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
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .clickable {
                                isWebViewShowing2.value = false
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .width(28.dp),
                            painter = painterResource(id = R.drawable.icon_back_black),
                            contentDescription = ""
                        )
                        Text(
                            text = "뒤로 가기",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                TheContent("https://play.google.com/store/apps/details?id=com.devsquare.yuttogether&hl=ko&gl=US")
            }
        }

        isWebViewShowing3.value -> {
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
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .clickable {
                                isWebViewShowing3.value = false
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .width(28.dp),
                            painter = painterResource(id = R.drawable.icon_back_black),
                            contentDescription = ""
                        )
                        Text(
                            text = "뒤로 가기",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                TheContent("https://play.google.com/store/apps/details?id=kr.encom.gostop_p&hl=ko&gl=US")
            }
        }

        else -> {
            Column() {

                Column(
                    modifier = Modifier
                        .padding(40.dp)
                        .fillMaxWidth()
                        .background(
                            color = Color(0xfff7f7f7),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(40.dp)
                ) {
                    Text(
                        text = StringProvider.getString(R.string.dementia_result_wording1) + " " +testResult.countActiveScore()
                            .toString() + " " + StringProvider.getString(R.string.dementia_result_wording2),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = when {
                            testResult.countActiveScore() < 6 -> "운동과 외부 사회 활동을 유지하시고 치매예방수칙 3.3.3을 잘 실천하셔서 치매를 예방하세요."

                            testResult.countActiveScore() >= 6 -> "가까운 보건소나 치매안심센터를 방문하셔서 더 정확한 치매검지을 받아보시기 바랍니다."

                            else -> "No Dementia Scores"
                        },
                        fontSize = 28.sp,
                        color = Color(0xff878787)
                    )
                }
                Column() {
                    EyeTestSelectableBox(
                        title = "중앙치매센터 홈페이지",
                        description = "중앙치매센터 홈페이지",
                        onClickMethod = {
                            isWebViewShowing1.value = true
                        },
                        isDone = false
                    )
                    Spacer(
                        modifier = Modifier
                            .height(20.dp)
                    )
                    EyeTestSelectableBox(
                        title = "뇌 운동에 좋은 윷놀이 게임",
                        description = "뇌 운동에 좋은 윷놀이 게임",
                        onClickMethod = {
                            isWebViewShowing2.value = true
                        },
                        isDone = false
                    )
                    Spacer(
                        modifier = Modifier
                            .height(20.dp)
                    )
                    EyeTestSelectableBox(
                        title = "치매 예방에 좋은 고스톱 게임",
                        description = "치매 예방에 좋은 고스톱 게임",
                        onClickMethod = {
                            isWebViewShowing3.value = true
                        },
                        isDone = false
                    )
                }
            }
        }
    }
}

@Composable
fun TheContent(
    mUrl: String
) {
    AndroidView(factory = {
        android.webkit.WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = WebViewClient()
            loadUrl(mUrl)
        }
    }, update = {
        it.loadUrl(mUrl)
    })
}