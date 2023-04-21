package com.pixelro.nenoonkiosk.ui.testresultcontent

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.pixelro.nenoonkiosk.NenoonViewModel
import mangoslab.nemonicsdk.nemonicWrapper

@Composable
fun PrintComposable(
    viewModel: NenoonViewModel
) {
    Log.e("PrintComposable", "PrintComposable")
    val context = LocalContext.current
    val macAddr = viewModel.nemonicList.collectAsState().value[0].second
    Column(
        modifier = Modifier
            .background(Color.White)
    ) {
        val bm = textAsBitmap("Text",40f, android.graphics.Color.parseColor("#FF000000"))
        Image(
            bitmap = bm.asImageBitmap(),
            contentDescription = ""
        )
    }
    DisposableEffect(true) {
        val mNemonicWrapper = nemonicWrapper(context)
        mNemonicWrapper.enableLastPageCut(true)
        mNemonicWrapper.setTimeoutConstant(500)
        mNemonicWrapper.setBrightnessLevel(200)
        mNemonicWrapper.setContrastLevel(0)
        val nCopies = 1

        val bm = textAsBitmap("Text",40f, android.graphics.Color.parseColor("#FF000000"))
        val nResult = 0
        val nPrintWidth = 576
        val nPaperHeight = ((bm.height.toFloat() / bm.width.toFloat()) * 576).toInt()
        Log.e("DisposableEffect", "${bm.height}, ${bm.width}, $nPaperHeight")

        mNemonicWrapper.openPrinter(macAddr)
        mNemonicWrapper.print(bm, nPrintWidth, nPaperHeight, nCopies)
        onDispose() {
            mNemonicWrapper.closePrinter()
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
    val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(image)
    canvas.drawColor(android.graphics.Color.parseColor("#FFFFFFFF"))
    canvas.drawText(text!!, 0f, baseline, paint)
    return image!!
}