package com.pixelro.nenoonkiosk.login

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.harang.data.api.NenoonKioskApi
import com.harang.domain.model.SendSignInDataResponse
import com.pixelro.nenoonkiosk.data.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val api: NenoonKioskApi
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

    fun signIn() {
        viewModelScope.launch {
            val response = try {
                api.sendSignInData(
                    id = _text.value,
                    pw = _password.value
                )
            } catch (e: Exception) {
                e.printStackTrace()
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                null
            }
            Log.e("signInResponseCode", "${response?.code()}")
            Log.e("signInResponseBody", "${response?.body()}")
            Log.e("signInResponseErrorBody", "${response?.errorBody()}")
        }
    }

    fun checkLoginIsDone(): Boolean {
        if (_text.value == "") {
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