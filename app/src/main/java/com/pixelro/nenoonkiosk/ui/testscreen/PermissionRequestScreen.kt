package com.pixelro.nenoonkiosk.ui.testscreen

import android.Manifest
import android.app.Instrumentation.ActivityResult
import android.content.Intent
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
import androidx.compose.foundation.layout.height
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
import com.google.android.gms.common.api.ResolvableApiException
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xff1d71e1)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .border(
                            width = 4.dp,
                            color = Color(0xffffffff),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .width(400.dp)
                        .height(80.dp)
                        .clickable {
                            val intent = Intent(
                                Settings.ACTION_MANAGE_WRITE_SETTINGS,
                                Uri.parse("package:" + context.packageName)
                            )
                            writeSettingPermissionRequestLauncher.launch(intent)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "시스템 설정 변경 권한",
                        color = Color(0xffffffff),
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                }
                Image(
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp),
                    painter = when(viewModel.isWriteSettingsPermissionGranted.collectAsState().value) {
                        true -> painterResource(id = R.drawable.baseline_check_96_green)
                        else -> painterResource(id = R.drawable.close_96_red)
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
                Box(
                    modifier = Modifier
                        .border(
                            width = 4.dp,
                            color = Color(0xffffffff),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .width(400.dp)
                        .height(80.dp)
                        .clickable {
                            permissionRequestLauncher.launch(bluetoothPermissions)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "블루투스 권한",
                        color = Color(0xffffffff),
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                }
                Image(
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp),
                    painter = when(viewModel.isBluetoothPermissionsGranted.collectAsState().value) {
                        true -> painterResource(id = R.drawable.baseline_check_96_green)
                        else -> painterResource(id = R.drawable.close_96_red)
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
                Box(
                    modifier = Modifier
                        .border(
                            width = 4.dp,
                            color = Color(0xffffffff),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .width(400.dp)
                        .height(80.dp)
                        .clickable {
                            permissionRequestLauncher.launch(cameraPermissions)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "카메라 권한",
                        color = Color(0xffffffff),
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                }
                Image(
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp),
                    painter = when(viewModel.isCameraPermissionGranted.collectAsState().value) {
                        true -> painterResource(id = R.drawable.baseline_check_96_green)
                        else -> painterResource(id = R.drawable.close_96_red)
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
                Box(
                    modifier = Modifier
                        .border(
                            width = 4.dp,
                            color = Color(0xffffffff),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .width(400.dp)
                        .height(80.dp)
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
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "위치 서비스",
                        color = Color(0xffffffff),
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                }
                Image(
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp),
                    painter = when(viewModel.isLocationOn.collectAsState().value) {
                        true -> painterResource(id = R.drawable.baseline_check_96_green)
                        else -> painterResource(id = R.drawable.close_96_red)
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
                Box(
                    modifier = Modifier
                        .border(
                            width = 4.dp,
                            color = Color(0xffffffff),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .width(400.dp)
                        .height(80.dp)
                        .clickable {
                            permissionRequestLauncher.launch(cameraPermissions)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "블루투스 서비스",
                        color = Color(0xffffffff),
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                }
                Image(
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp),
                    painter = when(viewModel.isCameraPermissionGranted.collectAsState().value) {
                        true -> painterResource(id = R.drawable.baseline_check_96_green)
                        else -> painterResource(id = R.drawable.close_96_red)
                    },
                    contentDescription = ""
                )
            }
        }
    }
}