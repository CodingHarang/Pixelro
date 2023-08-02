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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    application: Application,
    private val api: NenoonKioskApi,
    private val signInRepository: SignInRepository
) : AndroidViewModel(application) {

    // signIn
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
        updateIsSignedIn: () -> Unit,
        updateScreenSaverInfo: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = signInRepository.getSignInResult(_id.value, _password.value)
            if (result != null) {
                Log.e("result", "data = ${result.data}\ncreateAt = ${result.createAt}\nresponseId = ${result.responseId}")
                for (data in result.data) {
                    if (data.value != null) {
                        Log.e("dataType", "data: ${data.value}\ntype: ${data.value.javaClass}")
                    }
                }
                if (result.data["success"] as Boolean) {
                    SharedPreferencesManager.putInt("locationId", (result.data["pid"] as Double).toInt())
                    updateIsSignedIn()
                } else {
                    Toast.makeText(getApplication(), "아이디와 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(getApplication(), "와이파이가 연결되어있는지 확인해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun checkLoginIsDone(): Boolean {
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

}