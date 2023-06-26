package com.pixelro.nenoonkiosk.test.result

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.Log
import com.pixelro.nenoonkiosk.data.TestType
import com.pixelro.nenoonkiosk.test.macular.amslergrid.AmslerGridTestResult
import com.pixelro.nenoonkiosk.test.macular.amslergrid.MacularDisorderType
import com.pixelro.nenoonkiosk.test.macular.mchart.MChartTestResult
import com.pixelro.nenoonkiosk.test.presbyopia.PresbyopiaTestResult
import com.pixelro.nenoonkiosk.test.visualacuity.shortdistance.ShortVisualAcuityTestResult

object TestResultUtil {
    fun textAsBitmap(
        testType: TestType,
        testResult: Any?,
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
                testResult as PresbyopiaTestResult
                val image = Bitmap.createBitmap(width, 400, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(image)
                canvas.drawARGB(255, 255, 255, 255)

                canvas.drawBitmap(logoImg, 360f, 320f, null)

                canvas.drawText("조절력 검사", 300f, baseline, paint)

                paint.typeface = Typeface.DEFAULT_BOLD
                canvas.drawText("안구 나이: " + (testResult.age - 2).toString()  + " ~ " + (testResult.age + 2).toString() + "세", 300f, baseline + 160f, paint)
                canvas.drawText("조절근점: " + String.format("%.1f", testResult.avgDistance) + "cm", 300f, baseline + 200f, paint)
                paint.typeface = Typeface.DEFAULT

                paint.textAlign = Paint.Align.LEFT
//            canvas.drawText(printName, 0f, baseline + 320f, paint)
//            canvas.drawText("☎0000-0000", 0f, baseline + 360f, paint)
                return image!!
            }
            TestType.ShortDistanceVisualAcuity -> {
                testResult as ShortVisualAcuityTestResult
                val image = Bitmap.createBitmap(width, 400, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(image)
                canvas.drawARGB(255, 255, 255, 255)

                canvas.drawBitmap(logoImg, 360f, 320f, null)

                canvas.drawText("근거리 시력 검사", 300f, baseline, paint)

                canvas.drawText("좌안", 150f, baseline + 60f, paint)
                canvas.drawText("우안", 450f, baseline + 60f, paint)

                paint.typeface = Typeface.DEFAULT_BOLD
                canvas.drawText((testResult.leftEye / 10f).toString(), 150f, baseline + 190f, paint)
                canvas.drawText((testResult.rightEye / 10f).toString(), 450f, baseline + 190f, paint)
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
                testResult as AmslerGridTestResult
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
                Log.e("", testResult.leftEyeDisorderType[0].toString())
                if(testResult.leftEyeDisorderType[0] != MacularDisorderType.Normal) {
                    canvas.drawText("이상", 70f, baseline + 160f, paint)
                }
                if(testResult.leftEyeDisorderType[1] != MacularDisorderType.Normal) {
                    canvas.drawText("이상", 150f, baseline + 160f, paint)
                }
                if(testResult.leftEyeDisorderType[2] != MacularDisorderType.Normal) {
                    canvas.drawText("이상", 230f, baseline + 160f, paint)
                }
                if(testResult.leftEyeDisorderType[3] != MacularDisorderType.Normal) {
                    canvas.drawText("이상", 70f, baseline + 240f, paint)
                }
                if(testResult.leftEyeDisorderType[4] != MacularDisorderType.Normal) {
                    canvas.drawText("이상", 150f, baseline + 240f, paint)
                }
                if(testResult.leftEyeDisorderType[5] != MacularDisorderType.Normal) {
                    canvas.drawText("이상", 230f, baseline + 240f, paint)
                }
                if(testResult.leftEyeDisorderType[6] != MacularDisorderType.Normal) {
                    canvas.drawText("이상", 70f, baseline + 320f, paint)
                }
                if(testResult.leftEyeDisorderType[7] != MacularDisorderType.Normal) {
                    canvas.drawText("이상", 150f, baseline + 320f, paint)
                }
                if(testResult.leftEyeDisorderType[8] != MacularDisorderType.Normal) {
                    canvas.drawText("이상", 230f, baseline + 320f, paint)
                }

                if(testResult.rightEyeDisorderType[0] != MacularDisorderType.Normal) {
                    canvas.drawText("이상", 370f, baseline + 160f, paint)
                }
                if(testResult.rightEyeDisorderType[1] != MacularDisorderType.Normal) {
                    canvas.drawText("이상", 450f, baseline + 160f, paint)
                }
                if(testResult.rightEyeDisorderType[2] != MacularDisorderType.Normal) {
                    canvas.drawText("이상", 530f, baseline + 160f, paint)
                }
                if(testResult.rightEyeDisorderType[3] != MacularDisorderType.Normal) {
                    canvas.drawText("이상", 370f, baseline + 240f, paint)
                }
                if(testResult.rightEyeDisorderType[4] != MacularDisorderType.Normal) {
                    canvas.drawText("이상", 450f, baseline + 240f, paint)
                }
                if(testResult.rightEyeDisorderType[5] != MacularDisorderType.Normal) {
                    canvas.drawText("이상", 530f, baseline + 240f, paint)
                }
                if(testResult.rightEyeDisorderType[6] != MacularDisorderType.Normal) {
                    canvas.drawText("이상", 370f, baseline + 320f, paint)
                }
                if(testResult.rightEyeDisorderType[7] != MacularDisorderType.Normal) {
                    canvas.drawText("이상", 450f, baseline + 320f, paint)
                }
                if(testResult.rightEyeDisorderType[8] != MacularDisorderType.Normal) {
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
                testResult as MChartTestResult
                val image = Bitmap.createBitmap(width, 400, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(image)
                canvas.drawARGB(255, 255, 255, 255)

                canvas.drawBitmap(logoImg, 360f, 320f, null)

                canvas.drawText("엠식 변형시 검사", 300f, baseline, paint)

                canvas.drawText("좌안", 150f, baseline + 60f, paint)
                canvas.drawText("우안", 450f, baseline + 60f, paint)

                paint.typeface = Typeface.DEFAULT_BOLD
                canvas.drawText(when(testResult.leftEyeVertical) {
                    0 -> "수직: 정상"
                    else -> "수직: 이상"
                }, 150f, baseline + 170f, paint)
                canvas.drawText(when(testResult.leftEyeHorizontal) {
                    0 -> "수평: 정상"
                    else -> "수평: 이상"
                }, 150f, baseline + 210f, paint)
                canvas.drawText(when(testResult.rightEyeVertical) {
                    0 -> "수직: 정상"
                    else -> "수직: 이상"
                }, 450f, baseline + 170f, paint)
                canvas.drawText(when(testResult.rightEyeHorizontal) {
                    0 -> "수평: 정상"
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
}