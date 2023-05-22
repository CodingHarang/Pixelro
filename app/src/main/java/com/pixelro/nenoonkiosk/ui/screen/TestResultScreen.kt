package com.pixelro.nenoonkiosk.ui.screen

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavHostController
import com.github.mikephil.charting.utils.Utils.drawImage
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.GlobalConstants
import com.pixelro.nenoonkiosk.data.TestType
import com.pixelro.nenoonkiosk.ui.testresultcontent.*
import kotlinx.coroutines.launch
import mangoslab.nemonicsdk.nemonicWrapper

@Composable
fun TestResultScreen(
    viewModel: NenoonViewModel,
    navController: NavHostController
) {
    BackHandler(enabled = true) {
        navController.popBackStack(GlobalConstants.ROUTE_TEST_LIST, false)
    }

    val composableScope = rememberCoroutineScope()
    val testType = viewModel.selectedTestType.collectAsState().value
    val context = LocalContext.current
    val bluetoothManager = getSystemService(context, BluetoothManager::class.java) as BluetoothManager
    val bluetoothAdapter = bluetoothManager.adapter
    Log.e("bluetoothAdapter", "${bluetoothAdapter.hashCode()}")
    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            when(intent?.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

                    if (ActivityCompat.checkSelfPermission(
                            context.applicationContext,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    Log.e("onReceive", "ActionFound")

                    val deviceName = device?.name
                    val deviceHardwareAddress = device?.address // MAC address
                    if (deviceHardwareAddress != null && deviceName != null && deviceHardwareAddress.contains("74:F0:7D")) {
                        Log.e("onReceive", "$deviceName, $deviceHardwareAddress")
                        viewModel.updatePrinter(deviceName, deviceHardwareAddress)
                    }
                }
            }
        }
    }

    DisposableEffect(true) {

        Log.e("DisposableEffect", "DisposableEffect ${bluetoothAdapter.hashCode()}")
        context.registerReceiver(receiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
        bluetoothAdapter.startDiscovery()
        onDispose {
            Log.e("DisposableEffect", "onDispose")
            context.unregisterReceiver(receiver)
        }
    }

    val printerMacAddress = viewModel.printerMacAddress.collectAsState().value

    fun printResult(
        testType: TestType,
        printString: String
    ) {
        val mNemonicWrapper = nemonicWrapper(context)
        mNemonicWrapper.enableLastPageCut(true)
        mNemonicWrapper.setTimeoutConstant(500)
        mNemonicWrapper.setBrightnessLevel(200)
        mNemonicWrapper.setContrastLevel(0)
        val nCopies = 1

        val resources = context.resources
        val logoImg = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.pixelro_logo_black), 240, 80, false)

        val bm = textAsBitmap(testType, printString, logoImg)
        val nResult = 0
        val nPrintWidth = 576
        val nPaperHeight = ((bm.height.toFloat() / bm.width.toFloat()) * 576).toInt()
        Log.e("printResult", "${bm.height}, ${bm.width}, $nPaperHeight, $printerMacAddress")
        if(printerMacAddress != "") {
            Log.e("printResult", "print, ${bm.height}, ${bm.width}, $nPaperHeight, $printerMacAddress")
            mNemonicWrapper.openPrinter(printerMacAddress)
            mNemonicWrapper.print(bm, nPrintWidth, nPaperHeight, nCopies)
            mNemonicWrapper.closePrinter()
        } else {
            Toast.makeText(context, "연결된 프린터가 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val printString = viewModel.printString.collectAsState().value
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            text = when(testType) {
                TestType.Presbyopia -> "조절력 검사 결과"
                TestType.ShortDistanceVisualAcuity -> "근거리 시력 검사 결과"
                TestType.LongDistanceVisualAcuity -> "원거리 시력 검사 결과"
                TestType.ChildrenVisualAcuity -> "어린이 시력 검사 결과"
                TestType.AmslerGrid -> "암슬러 차트 검사 결과"
                TestType.MChart -> "엠식 변형시 검사 결과"
                else -> {
                    "None TestResultScreen"
                }
            },
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        when (testType) {
            TestType.Presbyopia -> {
                PresbyopiaTestResultContent(
                    viewModel = viewModel,
                    navController = navController
                )
            }
            TestType.ShortDistanceVisualAcuity -> {
                ShortDistanceVisualAcuityTestResultContent(
                    viewModel = viewModel,
                    navController = navController
                )
            }
            TestType.LongDistanceVisualAcuity -> {
                LongDistanceVisualAcuityTestResultContent(
                    viewModel = viewModel,
                    navController = navController
                )
            }
            TestType.ChildrenVisualAcuity -> {
                ChildrenVisualAcuityTestResultContent(
                    viewModel = viewModel,
                    navController = navController
                )
            }
            TestType.AmslerGrid -> {
                AmslerGridTestResultContent(
                    viewModel = viewModel,
                    navController = navController
                )

            }
            TestType.MChart -> {
                MChartTestResultContent(
                    viewModel = viewModel,
                    navController = navController
                )
            }
            else -> {
                Text("None TestResultScreen")
            }
        }
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 80.dp, end = 80.dp),
            onClick = {
                composableScope.launch {
                    bluetoothAdapter.startDiscovery()
                    Log.e("onClick", "${bluetoothAdapter.hashCode()}, ${bluetoothAdapter.isEnabled} ${bluetoothAdapter.isDiscovering} ${bluetoothAdapter.state}")
                    printResult(
                        testType = testType,
                        printString = printString
                    )
                }
            }
        ) {
            Text(
                text = "결과 프린트하기",
                color = Color(0xffffffff),
                fontSize = 30.sp
            )
        }
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 80.dp, end = 80.dp),
            onClick = {
                navController.popBackStack(GlobalConstants.ROUTE_TEST_LIST, false)
            }
        ) {
            Text(
                text = "돌아가기",
                color = Color(0xffffffff),
                fontSize = 30.sp
            )
        }
    }
}

fun textAsBitmap(
    testType: TestType,
    printString: String,
    logoImg: Bitmap
): Bitmap {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    paint.color = 0xff000000.toInt()
    paint.textSize = 40f
    paint.textAlign = Paint.Align.CENTER
    val width = 600
    val baseline = -paint.ascent()
//    paint.textSize = 40f
//    paint.color = 0xff000000.toInt()
//    paint.textAlign = Paint.Align.CENTER
//    val baseline = -paint.ascent() // ascent() is negative
//    val width = 600
//    val image = Bitmap.createBitmap(width, 400, Bitmap.Config.ARGB_8888)
//    val canvas = Canvas(image)
//    canvas.drawColor(android.graphics.Color.parseColor("#FFFFFFFF"))
    when(testType) {
        TestType.Presbyopia -> {
            val image = Bitmap.createBitmap(width, 400, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(image)
            canvas.drawARGB(255, 255, 255, 255)

            canvas.drawBitmap(logoImg, 360f, 320f, null)

            canvas.drawText("조절력 검사", 300f, baseline, paint)

            paint.typeface = Typeface.DEFAULT_BOLD
            canvas.drawText("조절근점: 30cm", 300f, baseline + 160f, paint)
            paint.typeface = Typeface.DEFAULT

            paint.textAlign = Paint.Align.LEFT
            canvas.drawText("분당 서울대 병원", 0f, baseline + 320f, paint)
            canvas.drawText("☎1588-3369", 0f, baseline + 360f, paint)
            return image!!
        }
        TestType.ShortDistanceVisualAcuity -> {
            val image = Bitmap.createBitmap(width, 400, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(image)
            canvas.drawARGB(255, 255, 255, 255)

            canvas.drawBitmap(logoImg, 360f, 320f, null)

            canvas.drawText("근거리 시력 검사", 300f, baseline, paint)

            canvas.drawText("좌안", 150f, baseline + 60f, paint)
            canvas.drawText("우안", 450f, baseline + 60f, paint)

            paint.typeface = Typeface.DEFAULT_BOLD
            canvas.drawText("0.6 난시", 150f, baseline + 190f, paint)
            canvas.drawText("1.0 정상", 450f, baseline + 190f, paint)
            paint.typeface = Typeface.DEFAULT

            paint.textAlign = Paint.Align.LEFT
            canvas.drawLine(300f, 100f, 300f, 300f, paint)
            canvas.drawText("분당 서울대 병원", 0f, baseline + 320f, paint)
            canvas.drawText("☎1588-3369", 0f, baseline + 360f, paint)
            return image!!
        }
        TestType.LongDistanceVisualAcuity -> {
            val image = Bitmap.createBitmap(width, 400, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(image)
            canvas.drawARGB(255, 255, 255, 255)

            canvas.drawBitmap(logoImg, 360f, 320f, null)

            canvas.drawText("근거리 시력 검사", 300f, baseline, paint)

            canvas.drawText("좌안", 150f, baseline + 60f, paint)
            canvas.drawText("우안", 450f, baseline + 60f, paint)

            paint.typeface = Typeface.DEFAULT_BOLD
            canvas.drawText("0.6 난시", 150f, baseline + 190f, paint)
            canvas.drawText("1.0 정상", 450f, baseline + 190f, paint)
            paint.typeface = Typeface.DEFAULT

            paint.textAlign = Paint.Align.LEFT
            canvas.drawLine(300f, 100f, 300f, 300f, paint)
            canvas.drawText("분당 서울대 병원", 0f, baseline + 320f, paint)
            canvas.drawText("☎1588-3369", 0f, baseline + 360f, paint)
            return image!!
        }
        TestType.ChildrenVisualAcuity -> {
            val image = Bitmap.createBitmap(width, 400, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(image)
            canvas.drawARGB(255, 255, 255, 255)

            canvas.drawBitmap(logoImg, 360f, 320f, null)

            canvas.drawText("근거리 시력 검사", 300f, baseline, paint)

            canvas.drawText("좌안", 150f, baseline + 60f, paint)
            canvas.drawText("우안", 450f, baseline + 60f, paint)

            paint.typeface = Typeface.DEFAULT_BOLD
            canvas.drawText("0.6 난시", 150f, baseline + 190f, paint)
            canvas.drawText("1.0 정상", 450f, baseline + 190f, paint)
            paint.typeface = Typeface.DEFAULT

            paint.textAlign = Paint.Align.LEFT
            canvas.drawLine(300f, 100f, 300f, 300f, paint)
            canvas.drawText("분당 서울대 병원", 0f, baseline + 320f, paint)
            canvas.drawText("☎1588-3369", 0f, baseline + 360f, paint)
            return image!!
        }
        TestType.AmslerGrid -> {
            val image = Bitmap.createBitmap(width, 500, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(image)
            canvas.drawARGB(255, 255, 255, 255)

            canvas.drawBitmap(logoImg, 360f, 420f, null)

            canvas.drawText("암슬러 차트 검사", 300f, baseline, paint)

            canvas.drawText("좌안", 150f, baseline + 60f, paint)
            canvas.drawText("우안", 450f, baseline + 60f, paint)

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 4f

            canvas.drawRect(RectF(30f, 140f, 110f, 220f), paint)
            canvas.drawRect(RectF(110f, 140f, 190f, 220f), paint)
            canvas.drawRect(RectF(190f, 140f, 270f, 220f), paint)
            canvas.drawRect(RectF(30f, 220f, 110f, 300f), paint)
            canvas.drawRect(RectF(110f, 220f, 190f, 300f), paint)
            canvas.drawRect(RectF(190f, 220f, 270f, 300f), paint)
            canvas.drawRect(RectF(30f, 300f, 110f, 380f), paint)
            canvas.drawRect(RectF(110f, 300f, 190f, 380f), paint)
            canvas.drawRect(RectF(190f, 300f, 270f, 380f), paint)

            canvas.drawRect(RectF(330f, 140f, 410f, 220f), paint)
            canvas.drawRect(RectF(410f, 140f, 490f, 220f), paint)
            canvas.drawRect(RectF(490f, 140f, 570f, 220f), paint)
            canvas.drawRect(RectF(330f, 220f, 410f, 300f), paint)
            canvas.drawRect(RectF(410f, 220f, 490f, 300f), paint)
            canvas.drawRect(RectF(490f, 220f, 570f, 300f), paint)
            canvas.drawRect(RectF(330f, 300f, 410f, 380f), paint)
            canvas.drawRect(RectF(410f, 300f, 490f, 380f), paint)
            canvas.drawRect(RectF(490f, 300f, 570f, 380f), paint)

            paint.style = Paint.Style.FILL
            paint.typeface = Typeface.DEFAULT_BOLD
            canvas.drawText("이상", 70f, baseline + 160f, paint)
            canvas.drawText("이상", 450f, baseline + 320f, paint)
            paint.typeface = Typeface.DEFAULT

            paint.textAlign = Paint.Align.LEFT
            paint.strokeWidth = 1f
            canvas.drawLine(300f, 100f, 300f, 400f, paint)
            canvas.drawText("분당 서울대 병원", 0f, baseline + 420f, paint)
            canvas.drawText("☎1588-3369", 0f, baseline + 460f, paint)
            return image!!
        }
        TestType.MChart -> {
            val image = Bitmap.createBitmap(width, 400, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(image)
            canvas.drawARGB(255, 255, 255, 255)

            canvas.drawBitmap(logoImg, 360f, 320f, null)

            canvas.drawText("엠식 변형시 검사", 300f, baseline, paint)

            canvas.drawText("좌안", 150f, baseline + 60f, paint)
            canvas.drawText("우안", 450f, baseline + 60f, paint)

            paint.typeface = Typeface.DEFAULT_BOLD
            canvas.drawText("수직: 정상", 150f, baseline + 170f, paint)
            canvas.drawText("수평: 정상", 150f, baseline + 210f, paint)
            canvas.drawText("수직: 이상", 450f, baseline + 170f, paint)
            canvas.drawText("수평: 정상", 450f, baseline + 210f, paint)
            paint.typeface = Typeface.DEFAULT

            paint.textAlign = Paint.Align.LEFT
            canvas.drawLine(300f, 100f, 300f, 300f, paint)
            canvas.drawText("분당 서울대 병원", 0f, baseline + 320f, paint)
            canvas.drawText("☎1588-3369", 0f, baseline + 360f, paint)
            return image!!
        }
        else -> {
            val image = Bitmap.createBitmap(width, 400, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(image)
            return image!!
        }
    }
}