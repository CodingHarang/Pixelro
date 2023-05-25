package com.pixelro.nenoonkiosk.facedetection

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.PointF
import android.media.ImageReader
import android.os.SystemClock
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark

class MyFaceAnalyzer(
    val updateFaceDetectionData: (PointF, PointF, Float, Float, Float, Float, Float) -> Unit,
    val updateFaceContourData: (List<PointF>, List<PointF>, List<PointF>, List<PointF>, List<PointF>, List<PointF>, List<PointF>, Float, Float) -> Unit,
    val updateInputImageSize: (Float, Float) -> Unit,
    val updateEyeOpenProbability: (Float, Float) -> Unit
) : ImageAnalysis.Analyzer {
    private val realTimeOpts =
        FaceDetectorOptions.Builder().setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.1f)
            .enableTracking()
            .build()
    private var lastAnalysisTime = -1L
    private val detector = com.google.mlkit.vision.face.FaceDetection.getClient(realTimeOpts)

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val now = SystemClock.uptimeMillis()
        if(lastAnalysisTime != -1L && now - lastAnalysisTime < 500f) {
            imageProxy.close()
            return
        }
        lastAnalysisTime = now
        // original image
        val mediaImage = imageProxy.image
        val image =
            mediaImage?.let { InputImage.fromMediaImage(it, imageProxy.imageInfo.rotationDegrees) }

        if (mediaImage != null) {
            updateInputImageSize(mediaImage.width.toFloat(), mediaImage.height.toFloat())
        }

        // resized image
//        val bitmap = imageProxy.toBitmap()
//        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width * 2, bitmap.height * 2, false)
//        val croppedBitmap = Bitmap.createBitmap(resizedBitmap, resizedBitmap.width / 4, resizedBitmap.height / 4, resizedBitmap.width / 2, resizedBitmap.height / 2)
//        val imageReader = ImageReader.newInstance(resizedBitmap.width, resizedBitmap.height, ImageFormat.YUV_420_888, 1)
//        val resizedImage = imageReader.acquireLatestImage()
//        val inputResizedImage = InputImage.fromBitmap(croppedBitmap, imageProxy.imageInfo.rotationDegrees)
//        Log.e("analyze", "analyze")
        if (image != null) {
            detector.process(image).addOnSuccessListener { faces ->
                for (face in faces) {
//                        val bounds = face.boundingBox
                    val rotX = face.headEulerAngleX
                    val rotY = face.headEulerAngleY
                    val rotZ = face.headEulerAngleZ
                    val leftEyePosition = face.getLandmark(FaceLandmark.LEFT_EYE)?.position
                    val rightEyePosition = face.getLandmark(FaceLandmark.RIGHT_EYE)?.position
                    val leftEyeOpenProbability = face.leftEyeOpenProbability
                    val rightEyeOpenProbability = face.rightEyeOpenProbability
//                    Log.e("rotX, rotY, rotZ, rightEyePosition, leftEyePosition", "$rotX $rotY  $rotZ, $rightEyePosition $leftEyePosition ${image.width.toFloat()} ${image.height.toFloat()}")

                    if(leftEyeOpenProbability != null && rightEyeOpenProbability != null) {
                        updateEyeOpenProbability(leftEyeOpenProbability, rightEyeOpenProbability)
//                        Log.e("faceDetection", "$leftEyeOpenProbability, $rightEyeOpenProbability")
                    } else {
//                        Log.e("null", "null")
                    }
                    if (rightEyePosition != null && leftEyePosition != null) {
                        updateFaceDetectionData(rightEyePosition, leftEyePosition, rotX, rotY, rotZ, image.width.toFloat(), image.height.toFloat())
                    }

//                    val leftEyeContour = face.getContour(FaceContour.LEFT_EYE)?.points
//                    val rightEyeContour = face.getContour(FaceContour.RIGHT_EYE)?.points
//                    val upperLipTopContour = face.getContour(FaceContour.UPPER_LIP_TOP)?.points
//                    val upperLipBottomContour = face.getContour(FaceContour.UPPER_LIP_BOTTOM)?.points
//                    val lowerLipTopContour = face.getContour(FaceContour.LOWER_LIP_TOP)?.points
//                    val lowerLipBottomContour = face.getContour(FaceContour.LOWER_LIP_BOTTOM)?.points
//                    val faceContour = face.getContour(FaceContour.FACE)?.points
//                    if (leftEyeContour != null && rightEyeContour != null && upperLipTopContour != null && upperLipBottomContour != null && lowerLipTopContour != null && lowerLipBottomContour != null && faceContour != null) {
//                        updateFaceContourData(leftEyeContour, rightEyeContour, upperLipTopContour, upperLipBottomContour, lowerLipTopContour, lowerLipBottomContour, faceContour, image.width.toFloat(), image.height.toFloat())
//                    }
                }
                imageProxy.close()
            }.addOnFailureListener {
                it.printStackTrace()
                imageProxy.close()
            }.addOnCompleteListener {
                imageProxy.close()
            }
        }
    }
}