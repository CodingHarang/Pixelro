package com.pixelro.nenoonkiosk.test.dementia

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.GlobalValue
import com.pixelro.nenoonkiosk.data.StringProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DementiaTestContent(
    toBackScreen: () -> Unit,
    toResultScreen: (DementiaTestResult) -> Unit,
    dementiaViewModel: DementiaViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    BackHandler(enabled = true) {
        Log.e("backhandler", "backhandler")
        toBackScreen()
    }

    val currentQuestion = remember { mutableStateOf(0) }
    LaunchedEffect(true) {
        dementiaViewModel.init()
    }

    @Composable
    fun questions(
        dementiaViewModel: DementiaViewModel = hiltViewModel(),
        questionIndex: Int,
        answer: DementiaViewModel.DementiaAnswer,
    ) {

        Box(
            modifier = Modifier
                .padding(start = if (answer == DementiaViewModel.DementiaAnswer.Yes) 0.dp else 20.dp)
                .clip(
                    shape = RoundedCornerShape(8.dp)
                )
                .width(350.dp)
                .fillMaxHeight(0.9f)
                .border(
                    border = BorderStroke(
                        width = 4.dp,
                        color = Color(0xff1d71e1)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                .background(
                    color = when (dementiaViewModel.dementiaScores.collectAsState().value[questionIndex]) {
                        answer -> Color(0xff1d71e1)
                        else -> Color(0xffffffff)
                    }
                )
                .clickable {
                    coroutineScope.launch {
                        dementiaViewModel.updateDementiaScore(
                            questionIndex,
                            answer
                        )
                        delay(1000)
                        if (currentQuestion.value < 13) {
                            currentQuestion.value++
                        } else {
                            toResultScreen(dementiaViewModel.getDementiaData())
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = StringProvider.getString(if (answer == DementiaViewModel.DementiaAnswer.Yes) R.string.yes else R.string.no),
                fontSize = 60.sp,
                color = when (dementiaViewModel.dementiaScores.collectAsState().value[questionIndex]) {
                    answer -> Color(0xffffffff)
                    else -> Color(0xff1d71e1)
                },
                fontWeight = FontWeight.ExtraBold
            )
        }
    }

    @Composable
    fun questionBox(
        dementiaViewModel: DementiaViewModel,
        i: Int,
    ) {
        Box(

        ) {
            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .height(300.dp)
                    .align(Alignment.Center),
                text = when (i) {
                    0 -> "1. " + StringProvider.getString(R.string.dementia_survey_question0)
                    1 -> "2. " + StringProvider.getString(R.string.dementia_survey_question1)
                    2 -> "3. " + StringProvider.getString(R.string.dementia_survey_question2)
                    3 -> "4. " + StringProvider.getString(R.string.dementia_survey_question3)
                    4 -> "5. " + StringProvider.getString(R.string.dementia_survey_question4)
                    5 -> "6. " + StringProvider.getString(R.string.dementia_survey_question5)
                    6 -> "7. " + StringProvider.getString(R.string.dementia_survey_question6)
                    7 -> "8. " + StringProvider.getString(R.string.dementia_survey_question7)
                    8 -> "9. " + StringProvider.getString(R.string.dementia_survey_question8)
                    9 -> "10. " + StringProvider.getString(R.string.dementia_survey_question9)
                    10 -> "11. " + StringProvider.getString(R.string.dementia_survey_question10)
                    11 -> "12. " + StringProvider.getString(R.string.dementia_survey_question11)
                    12 -> "13. " + StringProvider.getString(R.string.dementia_survey_question12)
                    13 -> "14. " + StringProvider.getString(R.string.dementia_survey_question13)

                    else -> ""
                },
                fontSize = 50.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        ) {
            Spacer(Modifier.weight(1f))
            questions(
                dementiaViewModel,
                questionIndex = i,
                answer = DementiaViewModel.DementiaAnswer.Yes,
            )
            questions(
                dementiaViewModel,
                questionIndex = i,
                answer = DementiaViewModel.DementiaAnswer.No,
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xffffffff)
            )
            .padding(40.dp)

    ) {
        questionBox(dementiaViewModel, i = currentQuestion.value)
    }
}
