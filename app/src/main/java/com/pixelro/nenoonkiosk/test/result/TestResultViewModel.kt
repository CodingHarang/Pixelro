package com.pixelro.nenoonkiosk.test.result

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.harang.data.api.NenoonKioskApi
import com.harang.domain.model.SendAmslerGridTestResultRequest
import com.harang.domain.model.SendMChartTestResultRequest
import com.harang.domain.model.SendPresbyopiaTestResultRequest
import com.harang.domain.model.SendShortVisualAcuityTestResultRequest
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

    fun sendResultToServer(testType: TestType, testResult: Any?) {
        viewModelScope.launch {
            when(testType) {
                TestType.None -> {
                    // do nothing
                }
                TestType.Presbyopia -> {
                    testResult as PresbyopiaTestResult
                    val request = SendPresbyopiaTestResultRequest(testResult.firstDistance, testResult.secondDistance, testResult.thirdDistance, testResult.avgDistance, testResult.age)
                    val response = api.sendPresbyopiaTestResult(request)
                    Log.e(
                        "response",
                        "code: ${response.code()}\nbody: ${response.body()}\nerrorbody: ${response.errorBody()}\n"
                    )
                }
                TestType.ShortDistanceVisualAcuity -> {
                    testResult as ShortVisualAcuityTestResult
                    val request = SendShortVisualAcuityTestResultRequest(testResult.leftEye, testResult.rightEye)
                    val response = api.sendShortVisualAcuityTestResult(request)
                    Log.e(
                        "response",
                        "code: ${response.code()}\nbody: ${response.body()}\nerrorbody: ${response.errorBody()}\n"
                    )
                }
                TestType.LongDistanceVisualAcuity -> {

                }
                TestType.ChildrenVisualAcuity -> {

                }
                TestType.AmslerGrid -> {
                    testResult as AmslerGridTestResult
                    val request = SendAmslerGridTestResultRequest(testResult.leftEyeDisorderType.toString(), testResult.rightEyeDisorderType.toString())
                    val response = api.sendAmslerGridResult(request)
                    Log.e(
                        "response",
                        "code: ${response.code()}\nbody: ${response.body()}\nerrorbody: ${response.errorBody()}\n"
                    )
                }
                TestType.MChart -> {
                    testResult as MChartTestResult
                    val request = SendMChartTestResultRequest(testResult.leftEyeVertical, testResult.leftEyeHorizontal, testResult.rightEyeVertical, testResult.rightEyeHorizontal)
                    val response = api.sendMChartTestResult(request)
                    Log.e(
                        "response",
                        "code: ${response.code()}\nbody: ${response.body()}\nerrorbody: ${response.errorBody()}\n"
                    )
                }
            }
        }
    }

    fun updatePrinter(name: String, address: String) {
        _printerName.update { name }
        _printerMacAddress.update { address }
    }
}