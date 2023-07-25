package com.pixelro.nenoonkiosk.test.presbyopia

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.ui.theme.NenoonKioskTheme
import kotlin.math.roundToInt

@Composable
fun PresbyopiaTestResultContent(
    testResult: PresbyopiaTestResult
) {
    Box(
    ) {
    //            AndroidView(
    //                modifier = Modifier
    //                    .fillMaxWidth()
    //                    .height(400.dp)
    //                    .padding(start = 60.dp, top = 40.dp, end = 60.dp, bottom = 40.dp),
    //                factory = { context ->
    //                    val lineChart = LineChart(context)
    //                    val allEntries = AccommodationData.allEntries
    //                    val entries2 = mutableListOf(
    //                        Entry(22f, 8.4f),
    //                    )
    //                    val startEntry = mutableListOf(
    //                        Entry(22f, 8.4f)
    //                    )
    //                    val midEntry = mutableListOf(
    //                        Entry(34f, 12.0f),
    //                    )
    //                    val endEntry = mutableListOf(
    //                        Entry(46f, 21.3f)
    //                    )
    //
    //                    val dataset1 = LineDataSet(allEntries, "Label1")
    //                    dataset1.color = 0xff0000ff.toInt()
    ////                dataset1.setCircleColor(0xff0000ff.toInt())
    //                    dataset1.setDrawCircleHole(false)
    //                    dataset1.setDrawCircles(false)
    ////                dataset1.circleRadius = 4f
    //                    dataset1.setDrawValues(false)
    //
    //                    val dataset2 = LineDataSet(entries2, "Label2")
    //                    dataset2.color = 0xff0000ff.toInt()
    ////                dataset2.setCircleColor(0xff0000ff.toInt())
    //                    dataset2.setDrawCircleHole(false)
    //                    dataset2.setDrawCircles(false)
    //                    dataset2.setDrawFilled(true)
    //                    dataset2.setDrawValues(false)
    //                    dataset2.fillColor = 0xff0000ff.toInt()
    //                    dataset2.lineWidth = 6f
    //
    ////                    val dataset3 = LineDataSet(startEntry, "Label3")
    ////                    dataset3.valueTextSize = 20f
    ////                    dataset3.circleRadius = 8f
    ////                    dataset3.setCircleColor(0xff0000ff.toInt())
    ////                    dataset3.setDrawCircleHole(false)
    ////                    val dataset4 = LineDataSet(midEntry, "Label4")
    ////                    dataset4.valueTextSize = 20f
    ////                    dataset4.circleRadius = 8f
    ////                    dataset4.setCircleColor(0xff0000ff.toInt())
    ////                    dataset4.setDrawCircleHole(false)
    ////                    val dataset5 = LineDataSet(endEntry, "Label5")
    ////                    dataset5.valueTextSize = 20f
    ////                    dataset5.circleRadius = 8f
    ////                    dataset5.setCircleColor(0xff0000ff.toInt())
    ////                    dataset5.setDrawCircleHole(false)
    //
    //                    val xAxis = lineChart.xAxis
    //                    xAxis.setDrawGridLines(false)
    //                    xAxis.position = XAxis.XAxisPosition.BOTTOM
    //                    xAxis.axisMinimum = 0f
    //                    xAxis.axisMaximum = 100f
    //                    xAxis.textSize = 20f
    //
    //                    val leftAxis = lineChart.axisLeft
    //                    leftAxis.setDrawGridLines(true)
    //                    leftAxis.enableGridDashedLine(5f, 4f, 1f)
    //                    leftAxis.axisMinimum = 0f
    //                    leftAxis.axisMaximum = 60f
    //                    leftAxis.textSize = 20f
    //                    leftAxis.valueFormatter = ChartValueFormatter()
    ////            leftAxis.setGridDashedLine(DashPathEffect(floatArrayOf(2f), 2f))
    //
    //                    val rightAxis = lineChart.axisRight
    //                    rightAxis.setDrawAxisLine(false)
    //                    rightAxis.setDrawGridLines(false)
    //                    rightAxis.setDrawLabels(false)
    ////            rightAxis.enableGridDashedLine(10f, 4f, 1f)
    //
    //                    val lineData = LineData(dataset1)
    ////                    lineData.addDataSet(dataset2)
    ////                    lineData.addDataSet(dataset3)
    ////                    lineData.addDataSet(dataset4)
    ////                    lineData.addDataSet(dataset5)
    //                    lineChart.data = lineData
    //                    lineChart.setTouchEnabled(false)
    //                    lineChart.legend.isEnabled = false
    //                    lineChart.description.isEnabled = false
    //                    lineChart.animateY(1000)
    //                    lineChart.invalidate()
    //
    //                    lineChart
    //                }
    //            )
    ////            Text(
    ////                modifier = Modifier
    ////                    .padding(start = 40.dp, top = 230.dp),
    ////                text = StringProvider.getString(R.string.result_screen_distance),
    ////                fontSize = 20.sp,
    ////                textAlign = TextAlign.Center
    ////            )
    //            Text(
    //                modifier = Modifier
    //                    .padding(start = 380.dp, top = 360.dp),
    //                text = StringProvider.getString(R.string.result_screen_age),
    //                fontSize = 20.sp,
    //                textAlign = TextAlign.Center
    //            )
    //        }
    }
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
        when (testResult.firstDistance.toInt() == 20 || testResult.secondDistance.toInt() == 20 || testResult.thirdDistance.toInt() == 20) {
            true -> {
                Text(
                    text = "노안 증상이 발견되지 않았습니다",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(
                    modifier = Modifier
                        .height(40.dp)
                )
                Text(
                    text = buildAnnotatedString {
                        append("안구 나이는 ")
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xff1d71e1),
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("45세 이하")
                        }
                        append("입니다")
                    },
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = "25cm 이하의 거리에서도 물체를 선명하게 볼 수 있습니다",
                    fontSize = 24.sp,
                    color = Color(0xff878787)
                )
            }
            false -> {
                Text(
                    text = "노안 증상으로 인해\n일상생활에 불편함이 있을 수 있습니다",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(
                    modifier = Modifier
                        .height(40.dp)
                )
                Text(
                    text = buildAnnotatedString {
                        append("안구 나이는 ")
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xff1d71e1),
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("" + (testResult.age - 2) + " ~ " + (testResult.age + 2) + "세")
                        }
                        append("입니다")
                    },
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = "약 " + String.format("%.1f", testResult.avgDistance) + "cm 이하의 거리에 있는 물체가 흐리게 보입니다",
                    fontSize = 24.sp,
                    color = Color(0xff878787)
                )
            }
        }
    }
}

//@Preview(name = "S6Lite", device = "spec:shape=Normal,width=1200,height=2000,unit=px,dpi=224", showBackground = true)
@Preview(device = "id:Galaxy S6 Lite", fontScale = 1.1f)
@Composable
fun ResultScreenPreview() {
    NenoonKioskTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            PresbyopiaTestResultContent(
                testResult = PresbyopiaTestResult(
                    age = 45,
                    firstDistance = 20f,
                    secondDistance = 20f,
                    thirdDistance = 20f,
                    avgDistance = 20f
                )
            )
            PresbyopiaTestResultContent(
                testResult = PresbyopiaTestResult(
                    age = 46,
                    firstDistance = 40f,
                    secondDistance = 40f,
                    thirdDistance = 40f,
                    avgDistance = 43f
                )
            )
        }
    }
}