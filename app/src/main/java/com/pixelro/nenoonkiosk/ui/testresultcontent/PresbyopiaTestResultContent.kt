package com.pixelro.nenoonkiosk.ui.testresultcontent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.pixelro.nenoonkiosk.ChartValueFormatter
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.AccommodationData
import com.pixelro.nenoonkiosk.data.StringProvider
import kotlin.math.roundToInt

@Composable
fun PresbyopiaTestResultContent(
    viewModel: NenoonViewModel,
    navController: NavHostController
) {
    Column() {
        Box(
        ) {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(start = 60.dp, top = 80.dp, end = 60.dp, bottom = 80.dp),
                factory = { context ->
                    val lineChart = LineChart(context)
                    val allEntries = AccommodationData.allEntries
                    val entries2 = mutableListOf(
                        Entry(22f, 8.4f),
                        Entry(23f, 8.6f),
                        Entry(24f, 8.8f),
                        Entry(25f, 9.1f),
                        Entry(26f, 9.3f),
                        Entry(27f, 9.6f),
                        Entry(28f, 9.9f),
                        Entry(29f, 10.2f),
                        Entry(30f, 10.5f),
                        Entry(31f, 10.9f),
                        Entry(32f, 11.2f),
                        Entry(33f, 11.6f),
                        Entry(34f, 12.0f),
                        Entry(35f, 12.5f),
                        Entry(36f, 13.0f),
                        Entry(37f, 13.5f),
                        Entry(38f, 14.1f),
                        Entry(39f, 14.7f),
                        Entry(40f, 15.4f),
                        Entry(41f, 16.1f),
                        Entry(42f, 16.9f),
                        Entry(43f, 17.9f),
                        Entry(44f, 18.9f),
                        Entry(45f, 20.0f),
                        Entry(46f, 21.3f)
                    )
                    val startEntry = mutableListOf(
                        Entry(22f, 8.4f)
                    )
                    val midEntry = mutableListOf(
                        Entry(34f, 12.0f),
                    )
                    val endEntry = mutableListOf(
                        Entry(46f, 21.3f)
                    )

                    val dataset1 = LineDataSet(allEntries, "Label1")
                    dataset1.color = 0xff0000ff.toInt()
//                dataset1.setCircleColor(0xff0000ff.toInt())
                    dataset1.setDrawCircleHole(false)
                    dataset1.setDrawCircles(false)
//                dataset1.circleRadius = 4f
                    dataset1.setDrawValues(false)

                    val dataset2 = LineDataSet(entries2, "Label2")
                    dataset2.color = 0xff0000ff.toInt()
//                dataset2.setCircleColor(0xff0000ff.toInt())
                    dataset2.setDrawCircleHole(false)
                    dataset2.setDrawCircles(false)
                    dataset2.setDrawFilled(true)
                    dataset2.setDrawValues(false)
                    dataset2.fillColor = 0xff0000ff.toInt()
                    dataset2.lineWidth = 6f

//                    val dataset3 = LineDataSet(startEntry, "Label3")
//                    dataset3.valueTextSize = 20f
//                    dataset3.circleRadius = 8f
//                    dataset3.setCircleColor(0xff0000ff.toInt())
//                    dataset3.setDrawCircleHole(false)
//                    val dataset4 = LineDataSet(midEntry, "Label4")
//                    dataset4.valueTextSize = 20f
//                    dataset4.circleRadius = 8f
//                    dataset4.setCircleColor(0xff0000ff.toInt())
//                    dataset4.setDrawCircleHole(false)
//                    val dataset5 = LineDataSet(endEntry, "Label5")
//                    dataset5.valueTextSize = 20f
//                    dataset5.circleRadius = 8f
//                    dataset5.setCircleColor(0xff0000ff.toInt())
//                    dataset5.setDrawCircleHole(false)

                    val xAxis = lineChart.xAxis
                    xAxis.setDrawGridLines(false)
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.axisMinimum = 0f
                    xAxis.axisMaximum = 100f
                    xAxis.textSize = 20f

                    val leftAxis = lineChart.axisLeft
                    leftAxis.setDrawGridLines(true)
                    leftAxis.enableGridDashedLine(5f, 4f, 1f)
                    leftAxis.axisMinimum = 0f
                    leftAxis.axisMaximum = 60f
                    leftAxis.textSize = 20f
                    leftAxis.valueFormatter = ChartValueFormatter()
//            leftAxis.setGridDashedLine(DashPathEffect(floatArrayOf(2f), 2f))

                    val rightAxis = lineChart.axisRight
                    rightAxis.setDrawAxisLine(false)
                    rightAxis.setDrawGridLines(false)
                    rightAxis.setDrawLabels(false)
//            rightAxis.enableGridDashedLine(10f, 4f, 1f)

                    val lineData = LineData(dataset1)
//                    lineData.addDataSet(dataset2)
//                    lineData.addDataSet(dataset3)
//                    lineData.addDataSet(dataset4)
//                    lineData.addDataSet(dataset5)
                    lineChart.data = lineData
                    lineChart.setTouchEnabled(false)
                    lineChart.legend.isEnabled = false
                    lineChart.description.isEnabled = false
                    lineChart.animateY(1000)
                    lineChart.invalidate()

                    lineChart
                }
            )
//            Text(
//                modifier = Modifier
//                    .padding(start = 40.dp, top = 230.dp),
//                text = StringProvider.getString(R.string.result_screen_distance),
//                fontSize = 20.sp,
//                textAlign = TextAlign.Center
//            )
            Text(
                modifier = Modifier
                    .padding(start = 380.dp, top = 430.dp),
                text = StringProvider.getString(R.string.result_screen_age),
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(start = 40.dp, end = 40.dp)
                .background(
                    color = Color(0xff888888),
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = StringProvider.getString(R.string.presbyopia_result_accommodation_amplitude)
                        + ": ${(viewModel.avgDistance.collectAsState().value).roundToInt().toFloat() / 10}cm",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color(0xffffffff)
            )
        }
    }
}