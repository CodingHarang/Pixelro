package com.pixelro.nenoonkiosk.survey

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.harang.data.model.SendSurveyDataRequest
import com.harang.data.repository.SurveyRepository
import com.pixelro.nenoonkiosk.survey.datatype.SurveyAge
import com.pixelro.nenoonkiosk.survey.datatype.SurveyData
import com.pixelro.nenoonkiosk.survey.datatype.SurveyDiabetes
import com.pixelro.nenoonkiosk.survey.datatype.SurveyGlass
import com.pixelro.nenoonkiosk.survey.datatype.SurveySex
import com.pixelro.nenoonkiosk.survey.datatype.SurveySurgery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SurveyViewModel @Inject constructor(
    application: Application,
    private val surveyRepository: SurveyRepository
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
    private val _questionType = MutableStateFlow(QuestionType.Age)
    val questionType: StateFlow<QuestionType> = _questionType

    fun updateQuestionType(type: QuestionType) {
        viewModelScope.launch {
            delay(1000)
            _questionType.update { type }
        }
    }

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
        _questionType.update { QuestionType.Age }
        _surveyAge.update { SurveyAge.None }
        _surveySex.update { SurveySex.None }
        _surveyGlass.update { SurveyGlass.None }
        _surveySurgery.update { SurveySurgery.None }
        _surveyDiabetes.update { SurveyDiabetes.None }
    }

    fun getSurveyId(
        toTestListScreen: (Long) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = surveyRepository.sendSurveyData(
                SendSurveyDataRequest(
                    age = when (_surveyAge.value) {
                        SurveyAge.First -> 1
                        SurveyAge.Second -> 2
                        SurveyAge.Third -> 4
                        SurveyAge.Fourth -> 5
                        SurveyAge.Fifth -> 6
                        SurveyAge.Sixth -> 7
                        SurveyAge.Seventh -> 8
                        else -> 9
                    },
                    gender = when (_surveySex.value) {
                        SurveySex.Man -> "M"
                        else -> "W"
                    },
                    glasses = when (_surveyGlass.value) {
                        SurveyGlass.Yes -> true
                        else -> false
                    },
                    surgery = when (_surveySurgery.value) {
                        SurveySurgery.Normal -> "normal"
                        SurveySurgery.LASIK -> "correction"
                        SurveySurgery.Cataract -> "cataract"
                        else -> "etc"
                    },
                    diabetes = when (_surveyDiabetes.value) {
                        SurveyDiabetes.Yes -> true
                        else -> false
                    }
                )
            )
            if (response != null) {
                Log.e("surveyId", "${((response.data?.get("tid") ?: 0) as Double).toLong()}")
                withContext(Dispatchers.Main) {
                    toTestListScreen(((response.data?.get("tid") ?: 0) as Double).toLong())
                }
            } else {
                Log.e("surveyId", "result is null")
                withContext(Dispatchers.Main) {
                    toTestListScreen(0L)
                }
            }
        }
    }

    enum class QuestionType {
        Age, Sex, Glass, Surgery, Diabetes
    }

    init {
        initSurveyData()
    }
}