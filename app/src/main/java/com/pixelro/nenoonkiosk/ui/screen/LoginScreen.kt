package com.pixelro.nenoonkiosk.ui.screen

import android.view.View.VISIBLE
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
//import com.pixelro.nenoonkiosk.login.LogInEmailViewModel
import com.pixelro.nenoonkiosk.login.LogInEvent
import com.pixelro.nenoonkiosk.login.LoginData
import com.pixelro.nenoonkiosk.survey.SurveyData
import com.pixelro.nenoonkiosk.login.LoginViewModel
import com.pixelro.nenoonkiosk.survey.SurveyAge
import com.pixelro.nenoonkiosk.survey.SurveyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    toSurveyScreen: () -> Unit,
//    loginViewModel: LoginViewModel = hiltViewModel(),
    viewModel: NenoonViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        val idHintTransition = updateTransition(viewModel.inputSignInId.collectAsState().value, label = "")
        val idHintUp by idHintTransition.animateDp(label = "") {
            when(it) {
                "" -> 0.dp
                else -> (-15).dp
            }
        }
        val passwordHintTransition = updateTransition(viewModel.inputSignInPassword.collectAsState().value, label = "")
        val passwordHintUp by passwordHintTransition.animateDp(label = "") {
            when(it) {
                "" -> 0.dp
                else -> (-15).dp
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 500.dp),
        )   {

            Image(
                modifier = Modifier
                    .height(100.dp)
                    .padding(start = 250.dp),
                painter = painterResource(id = R.drawable.pixelro_logo_black),
                contentDescription = ""
                )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(start = 40.dp, end = 40.dp)
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = Color(0xffc3c3c3)
                        ),
                        shape = RoundedCornerShape(50)
                    )
                    .background(
                        color = Color(0x00000000),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = viewModel.inputSignInId.collectAsState().value,
                    onValueChange = { viewModel.updateInputSignInId(it) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                )
                Text(
                    modifier = Modifier
                        .offset(0.dp, idHintUp)
                        .padding(start = 20.dp),
                    text = "Id",
                    color = Color(0xff666666)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(start = 40.dp, end = 40.dp)
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = Color(0xffc3c3c3)
                        ),
                        shape = RoundedCornerShape(50)
                    )
                    .background(
                        color = Color(0x00000000),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = viewModel.inputSignInPassword.collectAsState().value,
                    onValueChange = { viewModel.updateInputSignInPassword(it) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                )
                Text(
                    modifier = Modifier
                        .offset(0.dp, idHintUp)
                        .padding(start = 20.dp),
                    text = "Password",
                    color = Color(0xff666666)
                )
            }
        }

        //로그인 버튼
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .padding(
                        start = 40.dp,
                        end = 40.dp,
                        bottom = 40.dp
                    )
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(
                        color = Color(0xff1d71e1),
                        shape = RoundedCornerShape(8.dp),
                    )
//                    .border(
//                        border = BorderStroke(
//                            width = 1.dp,
//                            color = Color(0xffc3c3c3)
//                        ),
//                        shape = RoundedCornerShape(50)
//                    )
                    .clickable {
                        if (!viewModel.checkLoginIsDone()) return@clickable
                        toSurveyScreen()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "LOGIN",
                    fontSize = 40.sp,
                    color = Color(0xffffffff),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

