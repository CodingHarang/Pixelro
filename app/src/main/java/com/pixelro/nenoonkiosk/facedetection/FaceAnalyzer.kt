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
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executor

class MyFaceAnalyzer(
    val updateFaceDetectionData: (Rect, PointF?, PointF?, Float, Float, Float, Float?, Float?) -> Unit,
    val updateFaceContourData: (List<PointF>, List<PointF>, List<PointF>, List<PointF>, List<PointF>, List<PointF>, List<PointF>, Float, Float) -> Unit,
    val updateInputImageSize: (Float, Float) -> Unit,
    val updateTextRecognitionData: (Rect?) -> Unit,
    val updateIsFaceDetected: (Boolean) -> Unit,
    val updateIsNenoonTextDetected: (Boolean) -> Unit,
    private val executor: Executor
//    val updateBitmap: (Bitmap) -> Unit,
) : ImageAnalysis.Analyzer {

    private var lastAnalysisTime = -1L

    private val realTimeOpts =
        FaceDetectorOptions.Builder().setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.3f)
            .enableTracking()
            .build()

    private val detector = com.google.mlkit.vision.face.FaceDetection.getClient(realTimeOpts)
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private var noFaceCount = 0

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val now = SystemClock.uptimeMillis()
        if (lastAnalysisTime != -1L && now - lastAnalysisTime < 200f) {
            imageProxy.close()
            return
        }
        var isNenoonTextDetected = false
        lastAnalysisTime = now
        // original image
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            Log.e("imageSize", "imagewidth : ${image.width}, image height : ${image.height}")
//            updateInputImageSize(mediaImage.width.toFloat(), mediaImage.height.toFloat())
            // Text Recognition
            recognizer.process(image).addOnSuccessListener(executor) { result ->

//                Log.e("analysisprocess1", Thread.currentThread().name)

                if (result.textBlocks.size == 0) {
                    updateIsNenoonTextDetected(false)
                }
                for (block in result.textBlocks) {
                    for (line in block.lines) {
                        if (line.text == "NENOON" || line.text == "NE NOON") {
                            isNenoonTextDetected = true
                            updateTextRecognitionData(line.boundingBox)
                        }
                    }
                    if (isNenoonTextDetected) {
                        updateIsNenoonTextDetected(true)
                        break
                    } else {
                        updateIsNenoonTextDetected(false)
                    }
                }
            }.addOnFailureListener {
                updateIsNenoonTextDetected(false)
                it.printStackTrace()
            }.addOnCompleteListener {
                imageProxy.close()
            }


            // Face Detection
            detector.process(image).addOnSuccessListener(executor) { faces ->

//                Log.e("analysisprocess2", Thread.currentThread().name)

//                Log.e("eyeface", faces.size.toString())
//                if (faces.size == 0) {
//                    noFaceCount++
//                    if(noFaceCount > 10) {
//                        updateIsFaceDetected(false)
//                    }
//                    Log.e("faceNum", "no face detected")
//                    return@addOnSuccessListener
//                } else {
//                    noFaceCount = 0
//                    Log.e("faceNum", "face detected")
//                }
                var centerFace: Face? = null
                for (face in faces) {
                    val leftEyePosition = face.getLandmark(FaceLandmark.LEFT_EYE)?.position
                    val rightEyePosition = face.getLandmark(FaceLandmark.RIGHT_EYE)?.position
//                    Log.e(
//                        "eyePosition",
//                        "leftEyePosition: ${leftEyePosition?.x}\nrightEyePosition: ${rightEyePosition?.x}"
//                    )
                    if (leftEyePosition != null && rightEyePosition != null) {
                        if (leftEyePosition.x > 500f && leftEyePosition.x < 960f && rightEyePosition.x < 1420f && rightEyePosition.x > 960f && leftEyePosition.y > 700f && rightEyePosition.y > 700f && rightEyePosition.x - leftEyePosition.x > 150f) {
                            centerFace = face
                            break
                        }
                    }
                }

                if(centerFace != null) {

                    noFaceCount = 0
                    updateIsFaceDetected(true)
                    val boundingBox = centerFace.boundingBox
                    val rotX = centerFace.headEulerAngleX
                    val rotY = centerFace.headEulerAngleY
                    val rotZ = centerFace.headEulerAngleZ
                    val leftEyePosition = centerFace.getLandmark(FaceLandmark.LEFT_EYE)?.position
                    val rightEyePosition = centerFace.getLandmark(FaceLandmark.RIGHT_EYE)?.position
                    val leftEyeOpenProbability = centerFace.leftEyeOpenProbability
                    val rightEyeOpenProbability = centerFace.rightEyeOpenProbability

//                    Log.e(
//                        "eyePosition",
//                        "leftEyePosition: $leftEyePosition\nrightEyePosition: $rightEyePosition"
//                    )
                    if (leftEyePosition != null && rightEyePosition != null) {
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
//                        Log.e("faceNum", "${centerFace.trackingId}, ${leftEyePosition.x}, ${leftEyePosition.y} ${rightEyePosition.x}, ${rightEyePosition.y}")
                        return@addOnSuccessListener
                    } else {
                        updateIsFaceDetected(false)
                    }
                } else {
                    noFaceCount++
                    if(noFaceCount > 6) {
                        updateIsFaceDetected(false)
                    }
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
            }.addOnFailureListener {
                it.printStackTrace()
            }.addOnCompleteListener {
                imageProxy.close()
            }
        }
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
}