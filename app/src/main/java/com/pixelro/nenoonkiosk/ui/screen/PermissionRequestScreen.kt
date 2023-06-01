package com.pixelro.nenoonkiosk.ui.screen

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R

@Composable
fun PermissionRequestScreen(
    viewModel: NenoonViewModel
) {
    val context = LocalContext.current
    val bluetoothPermissions = arrayOf(
        Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val cameraPermissions = arrayOf(
        Manifest.permission.CAMERA
    )

    val isLocationOn = viewModel.isLocationOn.collectAsState().value
    val locationServiceResolvableApiException = viewModel.resolvableApiException.collectAsState().value

    val writeSettingPermissionRequestLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(Settings.System.canWrite(context)) {
//            viewModel.updateIsWriteSettingsPermissionGranted(true)
            Toast.makeText(context, "시스템 설정 변경 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
//            viewModel.updateIsWriteSettingsPermissionGranted(false)
            Toast.makeText(context, "시스템 설정 변경 권한이 허용되지 않았습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    val permissionRequestLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        result ->
        if(result.keys.contains(Manifest.permission.BLUETOOTH_SCAN)) {
            if(result.entries.contains(mapOf(Manifest.permission.BLUETOOTH_SCAN to true).entries.first())
                && result.entries.contains(mapOf(Manifest.permission.BLUETOOTH_CONNECT to true).entries.first())
                && result.entries.contains(mapOf(Manifest.permission.BLUETOOTH to true).entries.first())
                && result.entries.contains(mapOf(Manifest.permission.BLUETOOTH_ADMIN to true).entries.first())
                && result.entries.contains(mapOf(Manifest.permission.ACCESS_FINE_LOCATION to true).entries.first())
                && result.entries.contains(mapOf(Manifest.permission.ACCESS_COARSE_LOCATION to true).entries.first())) {
//                viewModel.updateIsBluetoothPermissionsGranted(true)
                Toast.makeText(context, "블루투스 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
//                viewModel.updateIsBluetoothPermissionsGranted(false)
                Toast.makeText(context, "블루투스 권한이 허용되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        if(result.keys.contains(Manifest.permission.CAMERA)) {
            if(result.entries.contains(mapOf(Manifest.permission.CAMERA to true).entries.first())) {
//                viewModel.updateIsCameraPermissionGranted(true)
                Toast.makeText(context, "카메라 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
//                viewModel.updateIsCameraPermissionGranted(false)
                Toast.makeText(context, "카메라 권한이 허용되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val locationServiceRequestLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) {
        activityResult ->
        if (activityResult.resultCode == ComponentActivity.RESULT_OK)
            Log.d("locationServiceRequest", "location service accepted")
        else {
            Log.d("locationServiceRequest", "location service denied")
        }
    }

    val bluetoothServiceRequestLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        activityResult ->
        if (activityResult.resultCode == ComponentActivity.RESULT_OK)
            Log.d("bluetoothServiceRequest", "bluetooth service accepted")
        else {
            Log.d("bluetoothServiceRequest", "bluetooth service denied")
        }
    }

    val manuallySetupLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        result ->
        if (result.resultCode == ComponentActivity.RESULT_OK)
            Log.d("ok", "ok")
        else {
            Log.d("not ok", "not ok")
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 40.dp)
    ) {
        Spacer(
            modifier = Modifier
                .height((viewModel.statusBarPadding.collectAsState().value + 152).dp)
        )
        Text(
            text = "앱 사용을 위해\n접근 권한 허용이 필요해요",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .padding(top = 20.dp),
            text = "권한이 모두 설정되어있어야 다음 화면으로 넘어갑니다",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xff878787)
        )
        Spacer(
            modifier = Modifier
                .height(80.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .width(32.dp)
                    .height(32.dp),
                painter = painterResource(id = R.drawable.icon_settings),
                contentDescription = ""
            )
            Box(
                modifier = Modifier
                    .width(220.dp)
                    .height(80.dp)
                    .padding(start = 32.dp)
                    .clickable {
                        val intent = Intent(
                            Settings.ACTION_MANAGE_WRITE_SETTINGS,
                            Uri.parse("package:" + context.packageName)
                        )
                        writeSettingPermissionRequestLauncher.launch(intent)
                    },
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "시스템 설정 변경",
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )
            }
            Image(
                modifier = Modifier
                    .width(32.dp)
                    .height(32.dp),
                painter = when(viewModel.isWriteSettingsPermissionGranted.collectAsState().value) {
                    true -> painterResource(id = R.drawable.baseline_check_48_on)
                    else -> painterResource(id = R.drawable.baseline_check_48_off)
                },
                contentDescription = ""
            )
        }
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .width(32.dp)
                    .height(32.dp),
                painter = painterResource(id = R.drawable.icon_camera),
                contentDescription = ""
            )
            Box(
                modifier = Modifier
                    .width(220.dp)
                    .height(80.dp)
                    .padding(start = 32.dp)
                    .clickable {
                        permissionRequestLauncher.launch(cameraPermissions)
                    },
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "카메라 권한",
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )
            }
            Image(
                modifier = Modifier
                    .width(32.dp)
                    .height(32.dp),
                painter = when(viewModel.isCameraPermissionGranted.collectAsState().value) {
                    true -> painterResource(id = R.drawable.baseline_check_48_on)
                    else -> painterResource(id = R.drawable.baseline_check_48_off)
                },
                contentDescription = ""
            )
        }
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .width(32.dp)
                    .height(32.dp),
                painter = painterResource(id = R.drawable.icon_bluetooth),
                contentDescription = ""
            )
            Box(
                modifier = Modifier
                    .width(220.dp)
                    .height(80.dp)
                    .padding(start = 32.dp)
                    .clickable {
                        permissionRequestLauncher.launch(bluetoothPermissions)
                    },
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "블루투스 권한",
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )
            }
            Image(
                modifier = Modifier
                    .width(32.dp)
                    .height(32.dp),
                painter = when(viewModel.isBluetoothPermissionsGranted.collectAsState().value) {
                    true -> painterResource(id = R.drawable.baseline_check_48_on)
                    else -> painterResource(id = R.drawable.baseline_check_48_off)
                },
                contentDescription = ""
            )
        }
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .width(32.dp)
                    .height(32.dp),
                painter = painterResource(id = R.drawable.icon_bluetoothon),
                contentDescription = ""
            )
            Box(
                modifier = Modifier
                    .width(220.dp)
                    .height(80.dp)
                    .padding(start = 32.dp)
                    .clickable {
                        val bluetoothAdapter =
                            (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.BLUETOOTH_SCAN
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            permissionRequestLauncher.launch(bluetoothPermissions)
                            return@clickable
                        }
//                            bluetoothAdapter.startDiscovery()
                        bluetoothServiceRequestLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
                    },
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "블루투스 서비스",
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )
            }
            Image(
                modifier = Modifier
                    .width(32.dp)
                    .height(32.dp),
                painter = when(viewModel.isBlueToothOn.collectAsState().value) {
                    true -> painterResource(id = R.drawable.baseline_check_48_on)
                    else -> painterResource(id = R.drawable.baseline_check_48_off)
                },
                contentDescription = ""
            )
        }
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .width(32.dp)
                    .height(32.dp),
                painter = painterResource(id = R.drawable.icon_location),
                contentDescription = ""
            )
            Box(
                modifier = Modifier
                    .width(220.dp)
                    .height(80.dp)
                    .padding(start = 32.dp)
                    .clickable {
                        if (!isLocationOn) {
                            val locationServiceIntentSenderRequest = IntentSenderRequest
                                .Builder(locationServiceResolvableApiException.resolution)
                                .build()
                            locationServiceRequestLauncher.launch(
                                locationServiceIntentSenderRequest
                            )
                        }
                    },
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "위치 서비스",
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )
            }
            Image(
                modifier = Modifier
                    .width(32.dp)
                    .height(32.dp),
                painter = when(viewModel.isLocationOn.collectAsState().value) {
                    true -> painterResource(id = R.drawable.baseline_check_48_on)
                    else -> painterResource(id = R.drawable.baseline_check_48_off)
                },
                contentDescription = ""
            )
        }
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        Text(
            modifier = Modifier
                .clickable {
                    manuallySetupLauncher.launch(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + context.packageName)))
                },
            text = "권한 직접 설정하기",
            color = Color(0xffffffff),
            fontSize = 30.sp
        )
    }
}