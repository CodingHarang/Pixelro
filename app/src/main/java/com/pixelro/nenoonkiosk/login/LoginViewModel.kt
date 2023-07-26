package com.pixelro.nenoonkiosk.login

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
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