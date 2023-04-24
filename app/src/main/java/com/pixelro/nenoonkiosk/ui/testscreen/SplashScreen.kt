package com.pixelro.nenoonkiosk.ui.testscreen

import android.Manifest
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R

@Composable
fun SplashScreen(
    viewModel: NenoonViewModel
) {
    val context = LocalContext.current
    if(ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        viewModel.updateIsBluetoothPermissionsGranted(true)
    }
    if(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
        viewModel.updateIsCameraPermissionGranted(true)
    }
    if(Settings.System.canWrite(context)) {
        viewModel.updateIsWriteSettingsPermissionGranted(true)
    }
    viewModel.checkIfAllPermissionsGranted()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xff1d71e1)
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                modifier = Modifier
                    .height(100.dp)
                    .padding(bottom = 40.dp),
                painter = painterResource(id = R.drawable.pixelro_logo),
                contentDescription = ""
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .height(300.dp),
                painter = painterResource(id = R.drawable.nenoon_logo),
                contentDescription = ""
            )
            Spacer(
                modifier = Modifier
                    .height(40.dp)
            )
            Text(
                text = "내눈",
                color = Color(0xffffffff),
                fontSize = 96.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}