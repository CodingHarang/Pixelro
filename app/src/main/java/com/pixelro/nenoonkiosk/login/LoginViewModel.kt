package com.pixelro.nenoonkiosk.login

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.harang.data.api.NenoonKioskApi
import com.pixelro.nenoonkiosk.survey.SurveyAge
import com.pixelro.nenoonkiosk.survey.SurveyData
import com.pixelro.nenoonkiosk.survey.SurveyDiabetes
import com.pixelro.nenoonkiosk.survey.SurveyGlass
import com.pixelro.nenoonkiosk.survey.SurveySex
import com.pixelro.nenoonkiosk.survey.SurveySurgery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    // signIn
    private val _inputSignInId = MutableStateFlow("")
    val inputSignInId: StateFlow<String> = _inputSignInId

    private val _text = MutableStateFlow("")
    val text: StateFlow<String> = _text
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    fun updateText(text: String){
        _text.update { text }
    }
    fun updatePassword(text: String){
        _password.update { text }
    }

    fun updateInputSignInId(id: String) {
        _inputSignInId.update { id }
    }

    fun checkLoginIsDone(): Boolean {
        if (text.value == "") {
            Toast.makeText(getApplication(), "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.value == "") {
            Toast.makeText(getApplication(), "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }






}