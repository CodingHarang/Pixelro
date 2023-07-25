package com.pixelro.nenoonkiosk.test.result

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.harang.data.api.NenoonKioskApi
import com.harang.domain.model.SendAmslerGridTestResultRequest
import com.harang.domain.model.SendAmslerGridTestResultResponse
import com.harang.domain.model.SendMChartTestResultRequest
import com.harang.domain.model.SendMChartTestResultResponse
import com.harang.domain.model.SendPresbyopiaTestResultRequest
import com.harang.domain.model.SendPresbyopiaTestResultResponse
import com.harang.domain.model.SendShortVisualAcuityTestResultRequest
import com.harang.domain.model.SendShortVisualAcuityTestResultResponse
import com.pixelro.nenoonkiosk.data.TestType
import com.pixelro.nenoonkiosk.test.macular.amslergrid.AmslerGridTestResult
import com.pixelro.nenoonkiosk.test.macular.mchart.MChartTestResult
import com.pixelro.nenoonkiosk.test.presbyopia.PresbyopiaTestResult
import com.pixelro.nenoonkiosk.test.visualacuity.shortdistance.ShortVisualAcuityTestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TestResultViewModel @Inject constructor(
    application: Application,
    private val api: NenoonKioskApi
) : AndroidViewModel(application) {

    private val _printerName = MutableStateFlow("")
    val printerName: StateFlow<String> = _printerName
    private val _printerMacAddress = MutableStateFlow("")
    val printerMacAddress: StateFlow<String> = _printerMacAddress
    private val _nemonicList = MutableStateFlow(emptyList<Pair<String, String>>())
    val nemonicList: StateFlow<List<Pair<String, String>>> = _nemonicList
    private val _printString = MutableStateFlow("")
    val printString: StateFlow<String> = _printString

    fun sendResultToServer(surveyId: Long,testType: TestType, testResult: Any?) {
        viewModelScope.launch {
            when(testType) {
                TestType.None -> {
                }
                TestType.Presbyopia -> {
                    viewModelScope.launch {
                        testResult as PresbyopiaTestResult
                        val request = SendPresbyopiaTestResultRequest(
                            surveyId = surveyId,
                            distance1 = testResult.firstDistance.toInt(),
                            distance2 = testResult.secondDistance.toInt(),
                            distance3 = testResult.thirdDistance.toInt(),
                            distanceAvg = testResult.avgDistance.toInt()
                        )
                        val response = try {
                            api.sendPresbyopiaTestResult(request)
                        } catch (e: IOException) {
                            e.printStackTrace()
                            null
                        } catch(e: HttpException) {
                            e.printStackTrace()
                            null
                        }
                        Log.e("sendPresbyopiaResultToServer", "code: ${response?.code()}\nresponse: ${response?.body()}, ${response?.errorBody()}")
                    }
                }
                TestType.ShortDistanceVisualAcuity -> {
                    viewModelScope.launch {
                        testResult as ShortVisualAcuityTestResult
                        val request = SendShortVisualAcuityTestResultRequest(
                            surveyId = surveyId,
                            testType = "",
                            distance = 0,
                            leftSight = testResult.leftEye,
                            rightSight = testResult.rightEye,
                            leftPerspective = "",
                            rightPerspective = ""
                        )
                        val response = try {
                            api.sendShortVisualAcuityTestResult(request)
                        } catch (e: IOException) {
                            e.printStackTrace()
                            null
                        } catch(e: HttpException) {
                            e.printStackTrace()
                            null
                        }
                        Log.e("sendShortDistanceResultToServer", "response: ${response?.body()}, ${response?.errorBody()}")
                    }
                }
                TestType.LongDistanceVisualAcuity -> {

                }
                TestType.ChildrenVisualAcuity -> {

                }
                TestType.AmslerGrid -> {
                    viewModelScope.launch {
                        testResult as AmslerGridTestResult
                        val request = SendAmslerGridTestResultRequest(
                            surveyId = surveyId,
                            distance = 30,
                            leftMacularLoc = testResult.leftEyeDisorderType.toString(),
                            rightMacularLoc = testResult.rightEyeDisorderType.toString()
                        )
                        val response = try {
                            api.sendAmslerGridResult(request)
                        } catch (e: IOException) {
                            e.printStackTrace()
                            null
                        } catch(e: HttpException) {
                            e.printStackTrace()
                            null
                        }
                        Log.e("sendAmslerGridResultToServer", "response: ${response?.body()}, ${response?.errorBody()}")
                    }
                }
                TestType.MChart -> {
                    viewModelScope.launch {
                        testResult as MChartTestResult
                        val request = SendMChartTestResultRequest(
                            surveyId = surveyId,
                            distance = 0,
                            leftEyeVer = testResult.leftEyeVertical,
                            rightEyeVer = testResult.rightEyeVertical,
                            leftEyeHor = testResult.leftEyeHorizontal,
                            rightEyeHor = testResult.rightEyeHorizontal
                        )
                        val response = try {
                            api.sendMChartTestResult(request)
                        } catch (e: IOException) {
                            e.printStackTrace()
                            null
                        } catch(e: HttpException) {
                            e.printStackTrace()
                            null
                        }
                        Log.e("sendMChartResultToServer", "response: ${response?.body()}, ${response?.errorBody()}")
                    }
                }
            }
        }
    }

    fun updatePrinter(name: String, address: String) {
        _printerName.update { name }
        _printerMacAddress.update { address }
    }
}