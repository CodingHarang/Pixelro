package com.pixelro.nenoonkiosk.test.result

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.harang.data.model.SendAmslerGridTestResultRequest
import com.harang.data.model.SendMChartTestResultRequest
import com.harang.data.model.SendPresbyopiaTestResultRequest
import com.harang.data.model.SendShortVisualAcuityTestResultRequest
import com.harang.data.repository.TestResultRepository
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
import javax.inject.Inject

@HiltViewModel
class TestResultViewModel @Inject constructor(
    application: Application,
    private val testResultRepository: TestResultRepository
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
                        val response = testResultRepository.sendPresbyopiaTestResult(request)
                        Log.e("presbyopiaRequest", "$request")
                        if (response != null) {
                            Log.e("presbyopiaResponse", "responseId: ${response.responseId}\ncreateAt: ${response.createAt}\ndata: ${response.data}")
                        }
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
                        val response = testResultRepository.sendVisualAcuityTestResult(request)
                        Log.e("visualAcuityRequest", "$request")
                        if (response != null) {
                            Log.e("visualAcuityResponse", "responseId: ${response.responseId}\ncreateAt: ${response.createAt}\ndata: ${response.data}")
                        }
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
                        val response = testResultRepository.sendAmslerGridTestResult(request)
                        Log.e("amslerGridRequest", "$request")
                        if (response != null) {
                            Log.e("amslerGridResponse", "responseId: ${response.responseId}\ncreateAt: ${response.createAt}\ndata: ${response.data}")
                        }
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
                        val response = testResultRepository.sendMChartTestResult(request)
                        Log.e("MChartRequest", "$request")
                        if (response != null) {
                            Log.e("MChartResponse", "responseId: ${response.responseId}\ncreateAt: ${response.createAt}\ndata: ${response.data}")
                        }
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