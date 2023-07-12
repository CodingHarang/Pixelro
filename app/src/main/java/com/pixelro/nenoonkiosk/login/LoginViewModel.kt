package com.pixelro.nenoonkiosk.login

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.survey.SurveyAge
import com.pixelro.nenoonkiosk.login.LoginData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {

//    private val _id = MutableStateFlow(LoginId.None)
//    val id: StateFlow<LoginId> = _id
//    private val _password = MutableStateFlow(LoginPassword.None)
//    val password: StateFlow<LoginPassword> = _password
//    private val _inputSignInId = mutableStateOf("")
//    val inputSignInId: StateFlow<String> = _inputSignInId
//    private val _inputSignInPassword = mutableStateOf("")
//    val inputSignInPassword: StateFlow<String> = _inputSignInPassword


//    fun updateInputSignInId(text: LoginId) {
//        _inputSignInId.update { text }
//    }
//
//    fun updateInputSignInPassword(id: LoginPassword) {
//        _inputSignInPassword.update { id }
    }

//    fun updateId(type: LoginId) {
//        _id.update { type }
//    }
//
//    fun updatePassword(type: LoginPassword) {
//        _password.update { type }
//    }


//    fun initLogin() {
//        _inputSignInId.update { LoginId.None }
//        _inputSignInPassword.update { LoginPassword.None }
//    }

//    init {
//        initLogin()
//    }
//}

