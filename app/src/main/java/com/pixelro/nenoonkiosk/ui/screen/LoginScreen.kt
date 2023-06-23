package com.pixelro.nenoonkiosk.ui.screen

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.pixelro.nenoonkiosk.NenoonViewModel

@Composable
fun LoginScreen(
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(start = 20.dp, end = 20.dp)
                .background(
                    color = Color(0xffbbffbb),
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp),
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
    }
}