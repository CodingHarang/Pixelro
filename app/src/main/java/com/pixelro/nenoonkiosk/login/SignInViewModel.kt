package com.pixelro.nenoonkiosk.login

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.harang.data.api.NenoonKioskApi
import com.harang.data.repository.SignInResultRepository
import com.pixelro.nenoonkiosk.NenoonKioskApplication
import com.pixelro.nenoonkiosk.data.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    application: Application,
    private val api: NenoonKioskApi,
    private val signInResultRepository: SignInResultRepository
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

    fun signIn(
        updateIsSignedIn: () -> Unit,
        updateScreenSaverInfo: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = signInResultRepository.getSignInResult(_text.value, _password.value)
            if (result != null) {
                Log.e("result", "data = ${result.data}\ncreateAt = ${result.createAt}\nresponseId = ${result.responseId}")
                for (data in result.data) {
                    if (data.value != null) {
                        Log.e("dataType", "data: ${data.value}\ntype: ${data.value.javaClass}")
                    }
                }
                if (result.data["success"] as Boolean) {
                    SharedPreferencesManager.putInt("locationId", (result.data["pid"] as Double).toInt())
                    updateScreenSaverInfo(result.data["video"] as String)
                    updateIsSignedIn()
                }
            }
//            val response = try {
//                api.sendSignInData(
//                    id = _text.value,
//                    pw = _password.value
//                )
//            } catch (e: Exception) {
//                e.printStackTrace()
//                null
//            } catch (e: HttpException) {
//                e.printStackTrace()
//                null
//            }
//            Log.e("signInResponseCode", "${response?.code()}")
//            Log.e("signInResponseBody", "${response?.body()}")
//            Log.e("signInResponseErrorBody", "${response?.errorBody()}")
//            if (response != null) {
//                if (response.body()?.data?.get("success") as Boolean) {
//                    Log.e("response", response.body()?.data?.get("video").toString())
//                    updateScreenSaverInfo(response.body()?.data?.get("video").toString())
////                    SharedPreferencesManager.putString(GlobalConstants.PREFERENCE_VIDEO_URI, response.body()!!.data["video"].toString())
//                    updateIsSignedIn()
//                } else {
//                    Toast.makeText(NenoonKioskApplication.applicationContext(), "아이디와 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                Toast.makeText(NenoonKioskApplication.applicationContext(), "네트워크 설정을 확인해주세요", Toast.LENGTH_SHORT).show()
//            }

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