package com.pixelro.nenoonkiosk.ui.testscreen

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavHostController
import com.pixelro.nenoonkiosk.NenoonViewModel
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

    fun printResult(result: String) {
        val mNemonicWrapper = nemonicWrapper(context)
        mNemonicWrapper.enableLastPageCut(true)
        mNemonicWrapper.setTimeoutConstant(500)
        mNemonicWrapper.setBrightnessLevel(200)
        mNemonicWrapper.setContrastLevel(0)
        val nCopies = 1

        val bm = textAsBitmap(result,40f, Color.parseColor("#FF000000"))
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

    Column() {
        Button(
            onClick = {
                composableScope.launch {
                    bluetoothAdapter.startDiscovery()
                    Log.e("onClick", "${bluetoothAdapter.hashCode()}, ${bluetoothAdapter.isEnabled} ${bluetoothAdapter.isDiscovering} ${bluetoothAdapter.state}")
                    printResult("hello\nworld!\nhello\nworld")
                }
            }
        ) {
            Text(
                text = "Print"
            )
        }
        when (viewModel.selectedTestType.collectAsState().value) {
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
    }
}

fun textAsBitmap(text: String?, textSize: Float, textColor: Int): Bitmap {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    paint.textSize = textSize
    paint.color = textColor
    paint.textAlign = Paint.Align.LEFT
    val baseline = -paint.ascent() // ascent() is negative
    val width = 576
    val height = (baseline + paint.descent() + 0.5f).toInt()
    val image = Bitmap.createBitmap(width, 400, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(image)
    canvas.drawColor(android.graphics.Color.parseColor("#FFFFFFFF"))
    canvas.drawText(text!!, 0f, baseline, paint)
    canvas.drawText("text\ntext\ntext", 0f,  baseline + 40, paint)
    return image!!
}