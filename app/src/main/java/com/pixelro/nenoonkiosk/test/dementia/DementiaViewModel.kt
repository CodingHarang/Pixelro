package com.pixelro.nenoonkiosk.test.dementia

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DementiaViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {

    private val _dementiaScores = MutableStateFlow(
        listOf(
            DementiaAnswer.None,
            DementiaAnswer.None,
            DementiaAnswer.None,
            DementiaAnswer.None,
            DementiaAnswer.None,
            DementiaAnswer.None,
            DementiaAnswer.None,
            DementiaAnswer.None,
            DementiaAnswer.None,
            DementiaAnswer.None,
            DementiaAnswer.None,
            DementiaAnswer.None,
            DementiaAnswer.None,
            DementiaAnswer.None
        )
    )
    val dementiaScores: StateFlow<List<DementiaAnswer>> = _dementiaScores

    fun updateDementiaScore(idx: Int, selected: DementiaAnswer) {
        _dementiaScores.update { scores ->
            val list = scores.toMutableList()
            for (i in 0..13) {
                if (i == idx) {
                    list[i] = selected
                }
            }
            list.toList()
        }
    }



    fun checkDementiaIsDone(): Boolean {
        for (i in 0..13) {
            if (_dementiaScores.value[i] == DementiaAnswer.None) {
                Toast.makeText(getApplication(), "설문에 응해주세요.", Toast.LENGTH_SHORT).show()
                return false
                //선택 넘기기 위해
//                return true
            }
        }
        return true
    }

    fun getDementiaData(): DementiaTestResult {
        return DementiaTestResult(_dementiaScores.value)
    }

    fun init(): Map<DementiaScore, Boolean> {
        return DementiaScore.values().associateWith { false }
    }

    enum class DementiaAnswer {
        Yes, No, None
    }
}
