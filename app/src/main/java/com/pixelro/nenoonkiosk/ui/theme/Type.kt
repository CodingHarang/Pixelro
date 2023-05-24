package com.pixelro.nenoonkiosk.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import com.pixelro.nenoonkiosk.R


val Typography = Typography(
    defaultFontFamily = FontFamily(
        Font(R.font.notosanskr_thin, FontWeight.Thin),
        Font(R.font.notosanskr_light, FontWeight.Light),
        Font(R.font.notosanskr_regular, FontWeight.Normal),
        Font(R.font.notosanskr_medium, FontWeight.Medium),
        Font(R.font.notosanskr_bold, FontWeight.Bold),
        Font(R.font.notosanskr_black, FontWeight.Black)
    ),
    body1 = TextStyle(
        platformStyle = PlatformTextStyle(
            includeFontPadding = false,
        ),
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None
        )
    )
)