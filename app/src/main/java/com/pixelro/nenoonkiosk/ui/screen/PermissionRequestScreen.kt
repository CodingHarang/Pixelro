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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.GlobalValue
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.ui.theme.nanumSquareNeoFamily

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
            Toast.makeText(context, "시스템 설정 변경 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
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
                Toast.makeText(context, "블루투스 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "블루투스 권한이 허용되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        if(result.keys.contains(Manifest.permission.CAMERA)) {
            if(result.entries.contains(mapOf(Manifest.permission.CAMERA to true).entries.first())) {
                Toast.makeText(context, "카메라 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
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
            .padding(start = 40.dp, end = 40.dp)
    ) {
        Spacer(
            modifier = Modifier
                .height((GlobalValue.statusBarPadding + 152).dp)
        )
        Text(
            text = StringProvider.getString(R.string.permission_requirement),
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = nanumSquareNeoFamily
        )
        Text(
            modifier = Modifier
                .padding(top = 80.dp),
            text = StringProvider.getString(R.string.permission_nextpage),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xff878787)
        )
        Spacer(
            modifier = Modifier
                .height(80.dp)
        )
        for (idx in 1..5) {
            Row(
                modifier = Modifier
                    .height(120.dp)
                    .clip(
                        RoundedCornerShape(8.dp)
                    )
                    .background(
                        color = Color(0xFFEEEEEE),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        when (idx) {
                            1 -> {
                                val intent = Intent(
                                    Settings.ACTION_MANAGE_WRITE_SETTINGS,
                                    Uri.parse("package:" + context.packageName)
                                )
                                writeSettingPermissionRequestLauncher.launch(intent)
                            }

                            2 -> {
                                permissionRequestLauncher.launch(cameraPermissions)
                            }

                            3 -> {
                                permissionRequestLauncher.launch(bluetoothPermissions)
                            }

                            4 -> {
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
                            }

                            5 -> {
                                if (!isLocationOn) {
                                    val locationServiceIntentSenderRequest = IntentSenderRequest
                                        .Builder(locationServiceResolvableApiException.resolution)
                                        .build()
                                    locationServiceRequestLauncher.launch(
                                        locationServiceIntentSenderRequest
                                    )
                                }
                            }
                        }
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .padding(start = 40.dp)
                        .width(40.dp)
                        .height(40.dp),
                    painter = painterResource(
                        id = when (idx) {
                            1 -> R.drawable.icon_settings
                            2 -> R.drawable.icon_camera
                            3 -> R.drawable.icon_bluetooth
                            4 -> R.drawable.icon_bluetoothon
                            else -> R.drawable.icon_location
                        }
                    ),
                    contentDescription = null
                )
                Column(
                    modifier = Modifier
                        .padding(start = 40.dp)
                ) {
                    Text(
                        text = when (idx) {
                            1 -> StringProvider.getString(R.string.permission_setting_change)
                            2 -> StringProvider.getString(R.string.permission_camera)
                            3 -> StringProvider.getString(R.string.permission_bluetooth)
                            4 -> StringProvider.getString(R.string.permission_bluetooth_service)
                            else -> StringProvider.getString(R.string.permission_location)
                        },
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp
                    )
                    Text(
                        text = when (idx) {
                            1 -> StringProvider.getString(R.string.permission_setting_change_detail)
                            2 -> StringProvider.getString(R.string.permission_camera_detail)
                            3 -> StringProvider.getString(R.string.permission_bluetooth_detail)
                            4 -> StringProvider.getString(R.string.permission_bluetooth_service_detail)
                            else -> StringProvider.getString(R.string.permission_location_detail)
                        },
                        color = Color(0xff878787)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Image(
                        modifier = Modifier
                            .padding(end = 40.dp)
                            .width(32.dp)
                            .height(32.dp),
                        painter = when (
                            when (idx) {
                                1 -> viewModel.isWriteSettingsPermissionGranted.collectAsState().value
                                2 -> viewModel.isCameraPermissionGranted.collectAsState().value
                                3 -> viewModel.isBluetoothPermissionsGranted.collectAsState().value
                                4 -> viewModel.isBlueToothOn.collectAsState().value
                                else -> viewModel.isLocationOn.collectAsState().value
                            }
                        ) {
                            true -> painterResource(id = R.drawable.baseline_check_48_on)
                            else -> painterResource(id = R.drawable.baseline_check_48_off)
                        },
                        contentDescription = null
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )
        }
    }
}