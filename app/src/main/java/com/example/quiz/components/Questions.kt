package com.example.quiz.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quiz.model.Question
import com.example.quiz.screen.viewmodel.QuestionViewModel
import com.example.quiz.util.AppColors


@Composable
fun Question(viewModel: QuestionViewModel) {
    val questions = viewModel.ktorData.value.data?.toMutableList()
    val questionIndex = remember {
        mutableIntStateOf(0)
    }
    //Log.d("Size", "Questions: ${questions?.size} ")
    if (viewModel.ktorData.value.loading == true)
        CircularProgressIndicator()
    else {
        val question = try {
            questions?.get(questionIndex.intValue)
        } catch (_: Exception) {
            null
        }
        if (questions != null) {
            QuestionDisplay(
                question = question!!,
                questionIndex = questionIndex,
                viewModel = viewModel,
            ) {
                questionIndex.intValue = questionIndex.intValue + 1
            }
        } else {
            Log.e("Error", "APi must not work")
        }


    }
}


//@Preview
@Composable
fun QuestionDisplay(
    question: Question,
    questionIndex: MutableState<Int>,
    viewModel: QuestionViewModel,
    onNextClicked: (Int) -> Unit = {},
) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    val choicesState = remember(question) {
        question.choices.toMutableList()
    }
    val answerState = remember(question) {
        mutableStateOf<Int?>(null)
    }
    val correctAnswerState = remember(question) {
        mutableStateOf<Boolean?>(null)
    }
    val updateAnswer: (Int) -> Unit = remember(question) {
        {
            answerState.value = it
            correctAnswerState.value = choicesState[it] == question.answer
        }
    }
    Scaffold(
        containerColor = AppColors.mDarkPurple
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            if (questionIndex.value >= 3) {
                ShowProgress(
                    score = questionIndex.value
                )
            }
            QuestionTracker(
                counter = questionIndex.value,
                outOf = viewModel.getKtorTotalQuestionCount()

            )
            DrawDottedLine(pathEffect = pathEffect)
            Column {
                Text(
                    modifier = Modifier
                        .padding(6.dp)
                        .align(Alignment.Start)
                        .fillMaxHeight(
                            0.3f
                        ),
                    text = question.question,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp,
                    color = AppColors.mOffWhite
                )
                // Choices
                choicesState.forEachIndexed { index, answer ->
                    Row(
                        modifier = Modifier
                            .padding(3.dp)
                            .fillMaxWidth()
                            .height(45.dp)
                            .border(
                                width = 4.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        AppColors.mOffDarkPurple,
                                        AppColors.mOffDarkPurple
                                    )
                                ),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .clip(
                                RoundedCornerShape(
                                    topStartPercent = 50,
                                    topEndPercent = 50,
                                    bottomEndPercent = 50,
                                    bottomStartPercent = 50
                                )
                            )
                            .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically,

                        ) {
                        RadioButton(
                            selected = answerState.value == index,
                            onClick = {
                                updateAnswer(index)
                            },
                            modifier = Modifier
                                .padding(
                                    start = 16.dp,
                                ),
                            colors = RadioButtonDefaults.colors(
                                selectedColor = if (answerState.value == index && correctAnswerState.value == true) {
                                    Color.Green
                                } else {
                                    Color.Red
                                }
                            )
                        )
                        val annotatedString = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Light,
                                    fontSize = 17.sp,
                                    color = if (answerState.value == index && correctAnswerState.value == true) {
                                        Color.Green
                                    } else if (answerState.value == index && correctAnswerState.value == false) {
                                        Color.Red
                                    } else {
                                        AppColors.mOffWhite
                                    }
                                )
                            ) {
                                append(answer)
                            }
                        }
                        Text(
                            text = annotatedString,
                            modifier = Modifier
                                .padding(6.dp)
                        )

                    }
                }
                Button(
                    onClick = {
                        onNextClicked(questionIndex.value)
                    },
                    modifier = Modifier
                        .padding(3.dp)
                        .align(
                            alignment = Alignment.CenterHorizontally
                        ),
                    shape = RoundedCornerShape(34.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.mLightBlue
                    )
                ) {
                    Text(
                        text = "Next",
                        modifier = Modifier
                            .padding(4.dp),
                        color = AppColors.mOffWhite,
                        fontSize = 17.sp
                    )
                }
            }
        }
    }
}

//@Preview
@Composable
fun QuestionTracker(
    counter: Int = 10,
    outOf: Int = 100
) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = ParagraphStyle(
                    textIndent = TextIndent.None
                )
            ) {
                withStyle(
                    style = SpanStyle(
                        color = AppColors.mLightGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 27.sp
                    )
                ) {
                    append("Question $counter/")
                    withStyle(
                        style = SpanStyle(
                            color = AppColors.mLightGray,
                            fontWeight = FontWeight.Light,
                            fontSize = 14.sp
                        )
                    ) {
                        append("$outOf")
                    }
                }
            }
        },
        modifier = Modifier
            .padding(20.dp)
    )
}

@Composable
fun DrawDottedLine(
    pathEffect: PathEffect
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        drawLine(
            color = AppColors.mLightGray,
            start = Offset(0f, 0f),
            end = Offset(size.width, 1f),
            pathEffect = pathEffect
        )
    }
}

@Preview
@Composable
fun ShowProgress(
    score: Int = 12
) {
    val gradient = Brush.linearGradient(
        listOf(
            Color(0xFFF95075),
            Color(0xFFBE6BE5)
        )
    )
    var progressFactor by remember(score) {
        mutableFloatStateOf(score * 0.002f)
    }
    Row(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .height(45.dp)
            .border(
                width = 4.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        AppColors.mOffDarkPurple,
                        AppColors.mOffDarkPurple
                    )
                ),
                shape = RoundedCornerShape(34.dp)
            )
            .clip(
                RoundedCornerShape(
                    topStartPercent = 50,
                    topEndPercent = 50,
                    bottomEndPercent = 50,
                    bottomStartPercent = 50
                )
            )
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progressFactor)
                .fillMaxHeight()
                .background(
                    brush = gradient
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = (score * 10).toString(),
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(23.dp)
                        )
                        .fillMaxHeight(0.87f)
                        .fillMaxWidth()
                        .padding(6.dp),
                    color = AppColors.mOffWhite,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
