package com.example.quiz.screen.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiz.data.DataOrException
import com.example.quiz.model.Question
import com.example.quiz.model.QuestionsItem
import com.example.quiz.repository.QuestionNetworkRepository
import com.example.quiz.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val ktorRepository: QuestionNetworkRepository
) : ViewModel() {

    val data: MutableState<DataOrException<ArrayList<QuestionsItem>, Boolean, Exception>> =
        mutableStateOf(
            DataOrException(
                null, true, Exception("")
            )
        )
    val ktorData: MutableState<DataOrException<List<Question>, Boolean, Exception>> =
        mutableStateOf(
            DataOrException(
                null, true, Exception("")
            )
        )

    init {
        //getAllQuestions()
        getQuestions()
    }

    private fun getAllQuestions() {
        viewModelScope.launch {
            data.value.loading = true
            data.value = questionRepository.getAllQuestions()

            if (data.value.data.toString().isNotEmpty()) {
                data.value.loading = false
            }
        }

    }

    fun getTotalQuestionCount(): Int {
        return data.value.data?.toMutableList()?.size!!
    }


    private fun getQuestions() {
        viewModelScope.launch {
            ktorData.value.loading = true
            ktorData.value = ktorRepository.getQuestions()

            if (ktorData.value.data.toString().isNotEmpty()) {
                ktorData.value.loading = false
            }
        }
    }

    fun getKtorTotalQuestionCount(): Int {
        return ktorData.value.data?.toMutableList()?.size!!
    }
}