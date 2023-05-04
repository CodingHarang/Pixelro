package com.pixelro.nenoonkiosk.ui.testresultcontent

import android.graphics.DashPathEffect
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.pixelro.nenoonkiosk.NenoonViewModel
import java.lang.Math.round
import kotlin.math.roundToInt

@Composable
fun PresbyopiaTestResultContent(
    viewModel: NenoonViewModel,
    navController: NavHostController
) {
    viewModel.updateAvgDistance()
    Text(
        modifier = Modifier
            .fillMaxWidth(),
        text = "조절력: ${(viewModel.avgDistance.collectAsState().value).roundToInt().toFloat() / 10}cm",
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(40.dp),
        factory = { context ->
            val lineChart = LineChart(context)
            val entries1 = mutableListOf(
                Entry(1f, 1f),
                Entry(2f, 2f),
                Entry(3f, 3f),
                Entry(4f, 4f),
                Entry(5f, 5f),
                Entry(6f, 6f),
                Entry(7f, 7f),
                Entry(8f, 8f),
                Entry(9f, 9f),
                Entry(10f, 10f)
            )
            val entries2 = mutableListOf(
                Entry(4f, 4f),
                Entry(5f, 5f),
                Entry(6f, 6f)
            )
            val dataset1 = LineDataSet(entries1, "Label1")
            dataset1.color = 0xff0000ff.toInt()
            dataset1.setCircleColor(0xff0000ff.toInt())

            val dataset2 = LineDataSet(entries2, "Label2")
            dataset2.color = 0xff0000ff.toInt()
            dataset2.setDrawFilled(true)
            dataset2.fillColor = 0xff0000ff.toInt()
            dataset2.lineWidth = 5f

            val xAxis = lineChart.xAxis
            xAxis.setDrawGridLines(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM

            val leftAxis = lineChart.axisLeft
            leftAxis.setDrawGridLines(true)
            leftAxis.enableGridDashedLine(10f, 4f, 1f)
//            leftAxis.setGridDashedLine(DashPathEffect(floatArrayOf(2f), 2f))

            val rightAxis = lineChart.axisRight
            rightAxis.setDrawAxisLine(false)
            rightAxis.setDrawGridLines(false)
//            rightAxis.enableGridDashedLine(10f, 4f, 1f)

            val lineData = LineData(dataset1)
            lineData.addDataSet(dataset2)
            lineChart.data = lineData
            lineChart.animateY(500)
            lineChart.invalidate()

            lineChart
        }
    )
}