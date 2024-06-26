package com.pixelro.nenoonkiosk.login

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.harang.data.api.NenoonKioskApi
import com.harang.data.repository.SignInRepository
import com.pixelro.nenoonkiosk.data.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    application: Application,
    private val signInRepository: SignInRepository
) : AndroidViewModel(application) {

    private val _id = MutableStateFlow("")
    val id: StateFlow<String> = _id
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    fun updateId(text: String){
        _id.update { text }
    }
    fun updatePassword(text: String){
        _password.update { text }
    }

    fun signIn(
        updateIsSignedIn: (Boolean) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            /*
            * 로그인 시도
            */
            val result = signInRepository.getSignInResult(_id.value, _password.value)

            if (result != null) {
                /*
                * 서버와 연결이 제대로 이루어졌을 때
                */

                if (result.data["success"] as Boolean) {
                    /*
                    * 로그인 성공
                    */
                    signInRepository.updateLocationId((result.data["pid"] as Double).toInt())
                    signInRepository.updateScreenSaverVideoURI(result.data["video"] as String)
                    withContext(Dispatchers.Main) {
                        updateIsSignedIn(true)
                    }
                } else {
                    /*
                    * 로그인 실패
                    */
                    withContext(Dispatchers.Main) {
                        Toast.makeText(getApplication(), "아이디와 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                /*
                * 네트워크 연결이 되어있지 않거나 서버와의 통신에 문제가 있을 때
                */
                withContext(Dispatchers.Main) {
                    Toast.makeText(getApplication(), "와이파이가 연결되어있는지 확인해주세요", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    fun checkIsTextFilled(): Boolean {
        if (_id.value == "") {
            Toast.makeText(getApplication(), "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }
        if (_password.value == "") {
            Toast.makeText(getApplication(), "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun signOut() {
        viewModelScope.launch {
            signInRepository.signOut()
        }
    }
}