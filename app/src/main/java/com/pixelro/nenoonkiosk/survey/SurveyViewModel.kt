package com.pixelro.nenoonkiosk.survey

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.harang.data.api.NenoonKioskApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SurveyViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val _surveyAge = MutableStateFlow(SurveyAge.None)
    val surveyAge: StateFlow<SurveyAge> = _surveyAge
    private val _surveySex = MutableStateFlow(SurveySex.None)
    val surveySex: StateFlow<SurveySex> = _surveySex
    private val _surveyGlass = MutableStateFlow(SurveyGlass.None)
    val surveyGlass: StateFlow<SurveyGlass> = _surveyGlass
    private val _surveySurgery = MutableStateFlow(SurveySurgery.None)
    val surveySurgery: StateFlow<SurveySurgery> = _surveySurgery
    private val _surveyDiabetes = MutableStateFlow(SurveyDiabetes.None)
    val surveyDiabetes: StateFlow<SurveyDiabetes> = _surveyDiabetes

    fun updateSurveyAge(type: SurveyAge) {
        _surveyAge.update { type }
    }

    fun updateSurveySex(type: SurveySex) {
        _surveySex.update { type }
    }

    fun updateSurveyGlass(type: SurveyGlass) {
        _surveyGlass.update { type }
    }

    fun updateSurveySurgery(type: SurveySurgery) {
        _surveySurgery.update { type }
    }

    fun updateSurveyDiabetes(type: SurveyDiabetes) {
        _surveyDiabetes.update { type }
    }

    fun initSurveyData() {
        _surveyAge.update { SurveyAge.None }
        _surveySex.update { SurveySex.None }
        _surveyGlass.update { SurveyGlass.None }
        _surveySurgery.update { SurveySurgery.None }
        _surveyDiabetes.update { SurveyDiabetes.None }
    }

    fun checkSurveyIsDone(): Boolean {
        if (surveyAge.value == SurveyAge.None) {
            Toast.makeText(getApplication(), "나이를 선택해주세요", Toast.LENGTH_SHORT).show()
            return false
        }
        if (surveySex.value == SurveySex.None) {
            Toast.makeText(getApplication(), "성별을 선택해주세요", Toast.LENGTH_SHORT).show()
            return false
        }
        if (surveyGlass.value == SurveyGlass.None) {
            Toast.makeText(getApplication(), "안경 착용 여부를 선택해주세요", Toast.LENGTH_SHORT).show()
            return false
        }
        if (surveySurgery.value == SurveySurgery.None) {
            Toast.makeText(getApplication(), "수술 여부를 선택해주세요", Toast.LENGTH_SHORT).show()
            return false
        }
        if (surveyDiabetes.value == SurveyDiabetes.None) {
            Toast.makeText(getApplication(), "당뇨 여부를 선택해주세요", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun getSurveyData(): SurveyData {
        return SurveyData(
            surveyAge = surveyAge.value,
            surveySex = surveySex.value,
            surveyGlass = surveyGlass.value,
            surveySurgery = surveySurgery.value,
            surveyDiabetes = surveyDiabetes.value
        )
    }

    init {
        initSurveyData()
    }
}