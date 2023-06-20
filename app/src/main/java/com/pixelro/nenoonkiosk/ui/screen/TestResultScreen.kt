package com.pixelro.nenoonkiosk.ui.screen

import android.Manifest
import android.app.Activity
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
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavHostController
import com.github.mikephil.charting.utils.Utils.drawImage
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.GlobalConstants
import com.pixelro.nenoonkiosk.data.StringProvider
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
        Log.e("backhandler", "backhandler")
        navController.popBackStack(GlobalConstants.ROUTE_TEST_LIST, false)
        viewModel.resetScreenSaverTimer()
    }
    val composableScope = rememberCoroutineScope()
    val testType = viewModel.selectedTestType.collectAsState().value
    val context = LocalContext.current
    val bluetoothManager = getSystemService(context, BluetoothManager::class.java) as BluetoothManager
    val bluetoothAdapter = bluetoothManager.adapter
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

                    val deviceName = device?.name
                    val deviceHardwareAddress = device?.address // MAC address
                    if (deviceHardwareAddress != null && deviceName != null && deviceHardwareAddress.contains("74:F0:7D")) {
                        viewModel.updatePrinter(deviceName, deviceHardwareAddress)
                    }
                }
            }
        }
    }

    DisposableEffect(true) {

        context.registerReceiver(receiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
        bluetoothAdapter.startDiscovery()
        onDispose {
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
        if(printerMacAddress != "") {
            mNemonicWrapper.openPrinter(printerMacAddress)
            mNemonicWrapper.print(bm, nPrintWidth, nPaperHeight, nCopies)
            mNemonicWrapper.closePrinter()
        } else {
            Toast.makeText(context, "연결된 프린터가 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val printString = viewModel.printString.collectAsState().value
        Box(
            modifier = Modifier
                .padding(
                    start = 40.dp,
                    top = (viewModel.statusBarPadding.collectAsState().value + 20).dp,
                    end = 40.dp,
                    bottom = 20.dp
                )
                .fillMaxWidth()
                .height(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = when(testType) {
                    TestType.Presbyopia -> StringProvider.getString(R.string.presbyopia_result_title)
                    TestType.ShortDistanceVisualAcuity -> StringProvider.getString(R.string.short_visual_acuity_result_title)
                    TestType.LongDistanceVisualAcuity -> StringProvider.getString(R.string.long_visual_acuity_result_title)
                    TestType.ChildrenVisualAcuity -> StringProvider.getString(R.string.children_visual_acuity_result_title)
                    TestType.AmslerGrid -> StringProvider.getString(R.string.amsler_grid_result_title)
                    TestType.MChart -> StringProvider.getString(R.string.mchart_result_title)
                    else -> {
                        "None TestResultScreen"
                    }
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    color = Color(0xffebebeb)
                )
        )
//        Text(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 40.dp),
//            text = when(testType) {
//                TestType.Presbyopia -> StringProvider.getString(R.string.presbyopia_result_title)
//                TestType.ShortDistanceVisualAcuity -> StringProvider.getString(R.string.short_visual_acuity_result_title)
//                TestType.LongDistanceVisualAcuity -> StringProvider.getString(R.string.long_visual_acuity_result_title)
//                TestType.ChildrenVisualAcuity -> StringProvider.getString(R.string.children_visual_acuity_result_title)
//                TestType.AmslerGrid -> StringProvider.getString(R.string.amsler_grid_result_title)
//                TestType.MChart -> StringProvider.getString(R.string.mchart_result_title)
//                else -> {
//                    "None TestResultScreen"
//                }
//            },
//            fontSize = 40.sp,
//            fontWeight = FontWeight.Bold,
//            textAlign = TextAlign.Center
//        )
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
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, end = 40.dp),
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
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .padding(start = 40.dp, end = 40.dp, bottom = 20.dp)
                        .fillMaxWidth()
                        .clip(
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            border = BorderStroke(1.dp, Color(0xffc3c3c3)),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            composableScope.launch {
                                bluetoothAdapter.startDiscovery()
                                printResult(
                                    testType = testType,
                                    printString = printString
                                )
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier
                                .width(28.dp),
                            painter = painterResource(id = R.drawable.icon_print),
                            contentDescription = ""
                        )
                        Text(
                            modifier = Modifier
                                .padding(20.dp),
                            text = StringProvider.getString(R.string.result_screen_print),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .padding(
                            start = 40.dp,
                            end = 40.dp,
                            bottom = (viewModel.navigationBarPadding.collectAsState().value).dp
                        )
                        .fillMaxWidth()
                        .clip(
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            border = BorderStroke(1.dp, Color(0xffc3c3c3)),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            navController.popBackStack(GlobalConstants.ROUTE_TEST_LIST, false)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier
                                .width(28.dp),
                            painter = painterResource(id = R.drawable.icon_back2),
                            contentDescription = ""
                        )
                        Text(
                            modifier = Modifier
                                .padding(20.dp),
                            text = StringProvider.getString(R.string.result_screen_go_back),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
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
    val printName = "태전그룹"
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
            canvas.drawText(printString, 300f, baseline + 160f, paint)
            paint.typeface = Typeface.DEFAULT

            paint.textAlign = Paint.Align.LEFT
//            canvas.drawText(printName, 0f, baseline + 320f, paint)
//            canvas.drawText("☎0000-0000", 0f, baseline + 360f, paint)
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
            canvas.drawText(printString.split(",")[0], 150f, baseline + 190f, paint)
            canvas.drawText(printString.split(",")[1], 450f, baseline + 190f, paint)
            paint.typeface = Typeface.DEFAULT

            paint.textAlign = Paint.Align.LEFT
            canvas.drawLine(300f, 100f, 300f, 300f, paint)
//            canvas.drawText(printName, 0f, baseline + 320f, paint)
//            canvas.drawText("☎0000-0000", 0f, baseline + 360f, paint)
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
//            canvas.drawText(printName, 0f, baseline + 320f, paint)
//            canvas.drawText("☎0000-0000", 0f, baseline + 360f, paint)
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
//            canvas.drawText(printName, 0f, baseline + 320f, paint)
//            canvas.drawText("☎0000-0000", 0f, baseline + 360f, paint)
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
            Log.e("", printString.split(",")[0])
            if(printString.split(",")[0] != "Normal") {
                canvas.drawText("이상", 70f, baseline + 160f, paint)
            }
            if(printString.split(",")[1] != "Normal") {
                canvas.drawText("이상", 150f, baseline + 160f, paint)
            }
            if(printString.split(",")[2] != "Normal") {
                canvas.drawText("이상", 230f, baseline + 160f, paint)
            }
            if(printString.split(",")[3] != "Normal") {
                canvas.drawText("이상", 70f, baseline + 240f, paint)
            }
            if(printString.split(",")[4] != "Normal") {
                canvas.drawText("이상", 150f, baseline + 240f, paint)
            }
            if(printString.split(",")[5] != "Normal") {
                canvas.drawText("이상", 230f, baseline + 240f, paint)
            }
            if(printString.split(",")[6] != "Normal") {
                canvas.drawText("이상", 70f, baseline + 320f, paint)
            }
            if(printString.split(",")[7] != "Normal") {
                canvas.drawText("이상", 150f, baseline + 320f, paint)
            }
            if(printString.split(",")[8] != "Normal") {
                canvas.drawText("이상", 230f, baseline + 320f, paint)
            }

            if(printString.split(",")[9] != "Normal") {
                canvas.drawText("이상", 370f, baseline + 160f, paint)
            }
            if(printString.split(",")[10] != "Normal") {
                canvas.drawText("이상", 450f, baseline + 160f, paint)
            }
            if(printString.split(",")[11] != "Normal") {
                canvas.drawText("이상", 530f, baseline + 160f, paint)
            }
            if(printString.split(",")[12] != "Normal") {
                canvas.drawText("이상", 370f, baseline + 240f, paint)
            }
            if(printString.split(",")[13] != "Normal") {
                canvas.drawText("이상", 450f, baseline + 240f, paint)
            }
            if(printString.split(",")[14] != "Normal") {
                canvas.drawText("이상", 530f, baseline + 240f, paint)
            }
            if(printString.split(",")[15] != "Normal") {
                canvas.drawText("이상", 370f, baseline + 320f, paint)
            }
            if(printString.split(",")[16] != "Normal") {
                canvas.drawText("이상", 450f, baseline + 320f, paint)
            }
            if(printString.split(",")[17] != "Normal") {
                canvas.drawText("이상", 530f, baseline + 320f, paint)
            }
//            canvas.drawText("이상", 450f, baseline + 320f, paint)
            paint.typeface = Typeface.DEFAULT

            paint.textAlign = Paint.Align.LEFT
            paint.strokeWidth = 1f
            canvas.drawLine(300f, 100f, 300f, 400f, paint)
//            canvas.drawText(printName, 0f, baseline + 420f, paint)
//            canvas.drawText("☎0000-0000", 0f, baseline + 460f, paint)
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
            canvas.drawText(when(printString.split(",")[0]) {
                                "0" -> "수직: 정상"
                                else -> "수직: 이상"
                            }, 150f, baseline + 170f, paint)
            canvas.drawText(when(printString.split(",")[1]) {
                                "0" -> "수평: 정상"
                                else -> "수평: 이상"
                            }, 150f, baseline + 210f, paint)
            canvas.drawText(when(printString.split(",")[2]) {
                                "0" -> "수직: 정상"
                                else -> "수직: 이상"
                            }, 450f, baseline + 170f, paint)
            canvas.drawText(when(printString.split(",")[3]) {
                                "0" -> "수평: 정상"
                                else -> "수평: 이상"
                            }, 450f, baseline + 210f, paint)
            paint.typeface = Typeface.DEFAULT

            paint.textAlign = Paint.Align.LEFT
            canvas.drawLine(300f, 100f, 300f, 300f, paint)
//            canvas.drawText(printName, 0f, baseline + 320f, paint)
//            canvas.drawText("☎0000-0000", 0f, baseline + 360f, paint)
            return image!!
        }
        else -> {
            val image = Bitmap.createBitmap(width, 400, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(image)
            return image!!
        }
    }
}