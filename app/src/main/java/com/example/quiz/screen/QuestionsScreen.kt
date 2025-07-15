package com.example.quiz.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quiz.components.Question
import com.example.quiz.screen.viewmodel.QuestionViewModel


@Composable
fun QuizApp(viewModel: QuestionViewModel = hiltViewModel()) {
    Question(viewModel)

}