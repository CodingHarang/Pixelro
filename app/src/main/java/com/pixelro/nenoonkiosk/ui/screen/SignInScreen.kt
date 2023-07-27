package com.pixelro.nenoonkiosk.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.login.LoginViewModel

@Composable
fun SignInScreen(
    toSurveyScreen: () -> Unit,
    toSurveyScreen_Guest: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val text = loginViewModel.text.collectAsState().value
    val password = loginViewModel.password.collectAsState().value
    val colors = TextFieldDefaults.textFieldColors(
        backgroundColor = Color.White
    )

    Box() {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //로고
            Image(
                modifier = Modifier
                    .width(300.dp)
                    .height(200.dp),
                painter = painterResource(id = R.drawable.nenoon_login_logo),
                contentDescription = ""
            )

            //아이디 창
            BasicTextField(
                value = text,
                onValueChange = { newText ->
                    loginViewModel.updateText(newText)
                },
                textStyle = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 30.sp
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .width(500.dp)
                            .height(60.dp)
                            .border(
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = Color(0xffc3c3c3)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                text = "아이디를 입력해주세요",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.LightGray,
                            )
                        }
                        innerTextField()
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            )

            //비밀번호 창
            BasicTextField(
                value = password,
                onValueChange = { newText ->
                    loginViewModel.updatePassword(newText)
                },
                visualTransformation = PasswordVisualTransformation(),
                textStyle = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 30.sp
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .width(500.dp)
                            .height(60.dp)
                            .border(
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = Color(0xffc3c3c3)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                text = "비밀번호를 입력해주세요",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.LightGray,
                            )
                        }
                        innerTextField()
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                )
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            )

            Text(
                text = "로그인없이 시작하기",
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable {
                        toSurveyScreen_Guest()
                    },
            )
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
                    .clickable {
                        if (!loginViewModel.checkLoginIsDone()) return@clickable
                        loginViewModel.signIn()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "로그인",
                    fontSize = 40.sp,
                    color = Color(0xffffffff),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
