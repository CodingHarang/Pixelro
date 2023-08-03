package com.pixelro.nenoonkiosk.ui.screen

import android.app.Activity
import android.view.KeyEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.pixelro.nenoonkiosk.NenoonKioskApplication
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.GlobalValue
import com.pixelro.nenoonkiosk.data.SharedPreferencesManager
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.login.SignInViewModel
import java.util.Locale

@Composable
fun SettingsScreen(
    viewModel: NenoonViewModel,
    isSignedIn: Boolean,
    toSignInScreen: () -> Unit,
    signInViewModel: SignInViewModel = hiltViewModel(),

) {
    val isLanguageSelectDialogShowing = viewModel.isLanguageSelectDialogShowing
    val context = LocalContext.current

    if(isLanguageSelectDialogShowing.collectAsState().value) {
        LanguageSelectDialog(
            updateLanguage = {
                viewModel.updateLanguage(it)
            }
        ) {
            viewModel.updateIsLanguageSelectDialogShowing(false)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .padding(top = (GlobalValue.statusBarPadding + 20).dp, bottom = 20.dp)
                .fillMaxWidth()
                .height(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        (context as Activity).dispatchKeyEvent(
                            KeyEvent(
                                KeyEvent.ACTION_DOWN,
                                KeyEvent.KEYCODE_BACK
                            )
                        )
                        context.dispatchKeyEvent(
                            KeyEvent(
                                KeyEvent.ACTION_UP,
                                KeyEvent.KEYCODE_BACK
                            )
                        )
                    },
                contentAlignment = Alignment.CenterStart
            ) {
                Image(
                    modifier = Modifier
                        .padding(start = 40.dp, top = 4.dp)
                        .width(28.dp),
                    painter = painterResource(id = R.drawable.icon_back_black),
                    contentDescription = ""
                )
            }
            Text(
                textAlign = TextAlign.Center,
                text = StringProvider.getString(R.string.settings_title),
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
        }


        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    color = Color(0xffdddddd)
                )
        )
        Box(
            modifier = Modifier
                .clickable {
                    viewModel.updateIsLanguageSelectDialogShowing(true)
                }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp, top = 10.dp, bottom = 10.dp),
                text = StringProvider.getString(R.string.settings_language),
                fontSize = 30.sp
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    color = Color(0xffdddddd)
                )
        )
        Box(
            modifier = Modifier
                .clickable {

                }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp, top = 10.dp, bottom = 10.dp),
                text = "이용 약관",
                fontSize = 30.sp
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    color = Color(0xffdddddd)
                )
        )
        Box(
            modifier = Modifier
                .clickable {
                    if (isSignedIn) {
                        signInViewModel.signOut()
                    }
                    toSignInScreen()
                }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp, top = 10.dp, bottom = 10.dp),
                text = when (isSignedIn) {
                    true -> "로그아웃"
                    false -> "로그인"
                },
                fontSize = 30.sp,
                color = when (isSignedIn) {
                    true -> Color(0xFFFF0000)
                    false -> Color(0xFF000000)
                }
            )
        }
    }
}

@Composable
fun LanguageSelectDialog(
    updateLanguage: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties()
    ) {
        Column(
            modifier = Modifier
                .width(600.dp)
                .height(1000.dp)
                .background(
                    color = Color(0xffffffff),
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 20.dp),
                textAlign = TextAlign.Center,
                text = "언어",
                fontSize = 30.sp
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(
                        color = Color(0xffdddddd)
                    )
            )
            Box(
                modifier = Modifier
                    .clickable {
                        updateLanguage("ko")
                    }
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, top = 10.dp, bottom = 10.dp),
                    text = "한국어",
                    fontSize = 30.sp
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        color = Color(0xffdddddd)
                    )
            )
            Box(
                modifier = Modifier
                    .clickable {
                        updateLanguage("en")
                    }
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, top = 10.dp, bottom = 10.dp),
                    text = "English",
                    fontSize = 30.sp
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        color = Color(0xffdddddd)
                    )
            )
        }
    }
}