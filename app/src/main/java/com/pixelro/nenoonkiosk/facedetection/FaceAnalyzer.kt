package com.pixelro.nenoonkiosk.facedetection

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.Rect
import android.media.ImageReader
import android.os.SystemClock
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark

class MyFaceAnalyzer(
    val updateFaceDetectionData: (Rect, PointF?, PointF?, Float, Float, Float, Float?, Float?) -> Unit,
    val updateFaceContourData: (List<PointF>, List<PointF>, List<PointF>, List<PointF>, List<PointF>, List<PointF>, List<PointF>, Float, Float) -> Unit,
    val updateInputImageSize: (Float, Float) -> Unit
//    val updateBitmap: (Bitmap) -> Unit,
) : ImageAnalysis.Analyzer {

    private val realTimeOpts =
        FaceDetectorOptions.Builder().setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.2f)
            .enableTracking()
            .build()
    private var lastAnalysisTime = -1L
    private val detector = com.google.mlkit.vision.face.FaceDetection.getClient(realTimeOpts)

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val now = SystemClock.uptimeMillis()
        if(lastAnalysisTime != -1L && now - lastAnalysisTime < 200f) {
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

//        val bitmap = imageProxy.toBitmap()
//        val matrix = Matrix()
//        matrix.setScale(-1f, 1f)
//        matrix.postRotate(90f)
//        val rotatedImage = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width - 300, bitmap.height, matrix, true)
//
//        updateBitmap(rotatedImage)
        // resized image
//        val bitmap = imageProxy.toBitmap()
//        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width * 2, bitmap.height * 2, false)
//        val croppedBitmap = Bitmap.createBitmap(resizedBitmap, resizedBitmap.width / 4, resizedBitmap.height / 4, resizedBitmap.width / 2, resizedBitmap.height / 2)
//        val imageReader = ImageReader.newInstance(resizedBitmap.width, resizedBitmap.height, ImageFormat.YUV_420_888, 1)
//        val resizedImage = imageReader.acquireLatestImage()
//        val inputResizedImage = InputImage.fromBitmap(croppedBitmap, imageProxy.imageInfo.rotationDegrees)

        if (image != null) {
            detector.process(image).addOnSuccessListener { faces ->
                for (face in faces) {
                    val boundingBox = face.boundingBox
                    val rotX = face.headEulerAngleX
                    val rotY = face.headEulerAngleY
                    val rotZ = face.headEulerAngleZ
                    val leftEyePosition = face.getLandmark(FaceLandmark.LEFT_EYE)?.position
                    val rightEyePosition = face.getLandmark(FaceLandmark.RIGHT_EYE)?.position
                    val leftEyeOpenProbability = face.leftEyeOpenProbability
                    val rightEyeOpenProbability = face.rightEyeOpenProbability

                    updateFaceDetectionData(
                        boundingBox,
                        leftEyePosition,
                        rightEyePosition,
                        rotX,
                        rotY,
                        rotZ,
                        leftEyeOpenProbability,
                        rightEyeOpenProbability
                    )

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
            }.addOnFailureListener {
                it.printStackTrace()
            }.addOnCompleteListener {
                imageProxy.close()
            }
        }
    }
}