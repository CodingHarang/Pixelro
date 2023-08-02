package com.pixelro.nenoonkiosk.test.result

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.harang.data.api.NenoonKioskApi
import com.harang.data.model.SendAmslerGridTestResultRequest
import com.harang.data.model.SendMChartTestResultRequest
import com.harang.data.model.SendPresbyopiaTestResultRequest
import com.harang.data.model.SendShortVisualAcuityTestResultRequest
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
import java.io.IOException
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
                        Log.e("presbyopiaRequest", "$request")
                        val response = try {
                            api.sendPresbyopiaTestResult(request)
                        } catch (e: IOException) {
                            e.printStackTrace()
                            null
                        } catch(e: HttpException) {
                            e.printStackTrace()
                            null
                        }
                        Log.e("presbyopiaResponse", "code: ${response?.code()}\nresponse: ${response?.body()}, ${response?.errorBody()}")
                    }
                }
                TestType.ShortDistanceVisualAcuity -> {
                    viewModelScope.launch {
                        testResult as ShortVisualAcuityTestResult
                        val request = SendShortVisualAcuityTestResultRequest(
                            surveyId = surveyId,
                            leftSight = testResult.leftEye,
                            rightSight = testResult.rightEye
                        )
                        Log.e("shortVisualAcuityRequest", "$request")
                        val response = try {
                            api.sendShortVisualAcuityTestResult(request)
                        } catch (e: IOException) {
                            e.printStackTrace()
                            null
                        } catch(e: HttpException) {
                            e.printStackTrace()
                            null
                        }
                        Log.e("shortVisualAcuityResponse", "response: ${response?.body()}, ${response?.errorBody()}")
                    }
                }
                TestType.LongDistanceVisualAcuity -> {

                }
                TestType.ChildrenVisualAcuity -> {

                }
                TestType.AmslerGrid -> {
                    viewModelScope.launch {
                        testResult as AmslerGridTestResult
                        var leftMacularLoc = testResult.leftEyeDisorderType.toString()
                        leftMacularLoc = leftMacularLoc.replace(" ", "").replace("[", "")
                            .replace("]", "").replace("Normal", "n")
                            .replace("Distorted", "d").replace("Blacked", "b")
                            .replace("Whited", "w")
                        var rightMacularLoc = testResult.rightEyeDisorderType.toString()
                        rightMacularLoc = rightMacularLoc.replace(" ", "").replace("[", "")
                            .replace("]", "").replace("Normal", "n")
                            .replace("Distorted", "d").replace("Blacked", "b")
                            .replace("Whited", "w")
                        val request = SendAmslerGridTestResultRequest(
                            surveyId = surveyId,
                            leftMacularLoc = leftMacularLoc,
                            rightMacularLoc = rightMacularLoc
                        )
                        Log.e("amslerGridRequest", "$request")
                        val response = try {
                            api.sendAmslerGridResult(request)
                        } catch (e: IOException) {
                            e.printStackTrace()
                            null
                        } catch(e: HttpException) {
                            e.printStackTrace()
                            null
                        }
                        Log.e("amslerGridResponse", "response: ${response?.body()}, ${response?.errorBody()}")
                    }
                }
                TestType.MChart -> {
                    viewModelScope.launch {
                        testResult as MChartTestResult
                        val request = SendMChartTestResultRequest(
                            surveyId = surveyId,
                            leftEyeVer = testResult.leftEyeVertical,
                            rightEyeVer = testResult.rightEyeVertical,
                            leftEyeHor = testResult.leftEyeHorizontal,
                            rightEyeHor = testResult.rightEyeHorizontal
                        )
                        Log.e("mchartRequest", "$request")
                        val response = try {
                            api.sendMChartTestResult(request)
                        } catch (e: IOException) {
                            e.printStackTrace()
                            null
                        } catch(e: HttpException) {
                            e.printStackTrace()
                            null
                        }
                        Log.e("mchartResultToServer", "response: ${response?.body()}, ${response?.errorBody()}")
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