//package com.pixelro.nenoonkiosk.test.dementia
//
//import android.Manifest
//import android.bluetooth.BluetoothDevice
//import android.bluetooth.BluetoothManager
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.content.IntentFilter
//import android.content.pm.PackageManager
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.util.Log
//import android.widget.Toast
//import androidx.activity.compose.BackHandler
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.DisposableEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.SpanStyle
//import androidx.compose.ui.text.buildAnnotatedString
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.withStyle
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavHostController
//import com.pixelro.nenoonkiosk.R
//import com.pixelro.nenoonkiosk.data.GlobalConstants
//import com.pixelro.nenoonkiosk.data.GlobalValue
//import com.pixelro.nenoonkiosk.data.StringProvider
//import com.pixelro.nenoonkiosk.data.TestType
//import com.pixelro.nenoonkiosk.test.result.TestResultUtil
//import com.pixelro.nenoonkiosk.test.result.TestResultViewModel
//import mangoslab.nemonicsdk.nemonicWrapper
//
//@Composable
//fun DementiaResultScreen(
//    navController: NavHostController,
//    testResultViewModel: TestResultViewModel = hiltViewModel()
//) {
//    BackHandler(enabled = true) {
//        Log.e("backhandler", "backhandler")
//        navController.popBackStack(GlobalConstants.ROUTE_TEST_LIST, false)
////        viewModel.resetScreenSaverTimer()
//    }
////    LaunchedEffect(true) {
//////        Log.e("threadName", Thread.currentThread().name)
////        testResultViewModel.sendResultToServer(
////            testType = testType,
////            testResult = testResult
////        )
////    }
//    val coroutineScope = rememberCoroutineScope()
//    val context = LocalContext.current
//    val bluetoothManager =
//        ContextCompat.getSystemService(context, BluetoothManager::class.java) as BluetoothManager
//    val bluetoothAdapter = bluetoothManager.adapter
//    val receiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent?) {
//            when (intent?.action) {
//                BluetoothDevice.ACTION_FOUND -> {
//                    // Discovery has found a device. Get the BluetoothDevice
//                    // object and its info from the Intent.
//                    val device: BluetoothDevice? =
//                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
//
//                    if (ActivityCompat.checkSelfPermission(
//                            context.applicationContext,
//                            Manifest.permission.BLUETOOTH_CONNECT
//                        ) != PackageManager.PERMISSION_GRANTED
//                    ) {
//                        return
//                    }
//
//                    val deviceName = device?.name
//                    val deviceHardwareAddress = device?.address // MAC address
//                    if (deviceHardwareAddress != null && deviceName != null && deviceHardwareAddress.contains(
//                            "74:F0:7D"
//                        )
//                    ) {
//                        testResultViewModel.updatePrinter(deviceName, deviceHardwareAddress)
//                    }
//                }
//            }
//        }
//    }
//
//    DisposableEffect(true) {
//        context.registerReceiver(receiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
//        bluetoothAdapter.startDiscovery()
//        onDispose {
//            context.unregisterReceiver(receiver)
//        }
//    }
//
//    val printerMacAddress = testResultViewModel.printerMacAddress.collectAsState().value
//
//    fun printResult(
//        testType: TestType,
//        testResult: Any?
//    ) {
//        val mNemonicWrapper = nemonicWrapper(context)
//        mNemonicWrapper.enableLastPageCut(true)
//        mNemonicWrapper.setTimeoutConstant(500)
//        mNemonicWrapper.setBrightnessLevel(200)
//        mNemonicWrapper.setContrastLevel(0)
//        val nCopies = 1
//
//        val resources = context.resources
//        val logoImg = Bitmap.createScaledBitmap(
//            BitmapFactory.decodeResource(
//                resources,
//                R.drawable.pixelro_logo_black
//            ), 240, 80, false
//        )
//        val bm = TestResultUtil.textAsBitmap(testType, testResult, logoImg)
//        val nResult = 0
//        val nPrintWidth = 576
//        val nPaperHeight = ((bm.height.toFloat() / bm.width.toFloat()) * 576).toInt()
//        if (printerMacAddress != "") {
//            mNemonicWrapper.openPrinter(printerMacAddress)
//            mNemonicWrapper.print(bm, nPrintWidth, nPaperHeight, nCopies)
//            mNemonicWrapper.closePrinter()
//        } else {
//            Toast.makeText(context, "연결된 프린터가 없습니다.", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        val printString = testResultViewModel.printString.collectAsState().value
//        Box(
//            modifier = Modifier
//                .padding(
//                    start = 40.dp,
//                    top = (GlobalValue.statusBarPadding + 20).dp,
//                    end = 40.dp,
//                    bottom = 20.dp
//                )
//                .fillMaxWidth()
//                .height(40.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(
//                text = StringProvider.getString(R.string.dementia_result_title),
//                fontSize = 24.sp,
//                fontWeight = FontWeight.Medium
//            )
//        }
//        Spacer(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(1.dp)
//                .background(
//                    color = Color(0xffebebeb)
//                )
//        )
//
//        //내용
//
//
//
//
//        Spacer(
//            modifier = Modifier
//                .height(20.dp)
//        )
//        Text(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(start = 40.dp, end = 40.dp),
//            text = buildAnnotatedString {
//                withStyle(
//                    style = SpanStyle(
//                        color = Color(0xff999999),
//                        fontSize = 16.sp
//                    )
//                ) {
//                    append(StringProvider.getString(R.string.test_list_screen_warning1))
//                }
//                withStyle(
//                    style = SpanStyle(
//                        color = Color(0xffff0000),
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                ) {
//                    append(StringProvider.getString(R.string.test_list_screen_warning2))
//                }
//                withStyle(
//                    style = SpanStyle(
//                        color = Color(0xff999999),
//                        fontSize = 16.sp
//                    )
//                ) {
//                    append(StringProvider.getString(R.string.test_list_screen_warning3))
//                }
//            }
//        )
//        Box(
//            modifier = Modifier
//                .padding(bottom = 40.dp)
//                .fillMaxSize(),
//            contentAlignment = Alignment.BottomCenter
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Box(
//                    modifier = Modifier
//                        .padding(start = 40.dp, end = 40.dp, bottom = 20.dp)
//                        .fillMaxWidth()
//                        .clip(
//                            shape = RoundedCornerShape(8.dp)
//                        )
//                        .border(
//                            border = BorderStroke(1.dp, Color(0xffc3c3c3)),
//                            shape = RoundedCornerShape(8.dp)
//                        ),
////                    .clickable {
////                        coroutineScope.launch {
////                            bluetoothAdapter.startDiscovery()
////                            printResult(
////                                testType = testType,
////                                testResult = testResult
////                            )
////                        }
////                    },
//                    contentAlignment = Alignment.Center
//                ) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Image(
//                            modifier = Modifier
//                                .width(28.dp),
//                            painter = painterResource(id = R.drawable.icon_print),
//                            contentDescription = ""
//                        )
//                        Text(
//                            modifier = Modifier
//                                .padding(20.dp),
//                            text = StringProvider.getString(R.string.result_screen_print),
//                            fontSize = 24.sp,
//                            fontWeight = FontWeight.Medium
//                        )
//                    }
//                }
//                Box(
//                    modifier = Modifier
//                        .padding(
//                            start = 40.dp,
//                            end = 40.dp,
//                            bottom = GlobalValue.navigationBarPadding.dp
//                        )
//                        .fillMaxWidth()
//                        .clip(
//                            shape = RoundedCornerShape(8.dp)
//                        )
//                        .border(
//                            border = BorderStroke(1.dp, Color(0xffc3c3c3)),
//                            shape = RoundedCornerShape(8.dp)
//                        )
//                        .clickable {
//                            navController.popBackStack(GlobalConstants.ROUTE_TEST_LIST, false)
//                        },
//                    contentAlignment = Alignment.Center
//                ) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Image(
//                            modifier = Modifier
//                                .width(28.dp),
//                            painter = painterResource(id = R.drawable.icon_back2),
//                            contentDescription = ""
//                        )
//                        Text(
//                            modifier = Modifier
//                                .padding(20.dp),
//                            text = StringProvider.getString(R.string.go_to_first_page),
//                            fontSize = 24.sp,
//                            fontWeight = FontWeight.Medium
//                        )
//                    }
//                }
//            }
//        }
//    }
//}