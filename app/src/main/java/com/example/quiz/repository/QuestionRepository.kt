package com.example.quiz.repository

import com.example.quiz.data.DataOrException
import com.example.quiz.model.QuestionsItem
import com.example.quiz.network.QuestionAPI
import javax.inject.Inject

class QuestionRepository @Inject constructor(
    private val api: QuestionAPI
) {

    private val dataOrException = DataOrException<ArrayList<QuestionsItem>, Boolean, Exception>()

    suspend fun getAllQuestions(): DataOrException<ArrayList<QuestionsItem>, Boolean, Exception> {
        try {
            dataOrException.loading = true
            dataOrException.data = api.getQuestions()
            if (dataOrException.data.toString().isNotEmpty()) dataOrException.loading = false
        } catch (exception: Exception) {
            dataOrException.e = exception
            // Add Log D to check for messages what's exception
        }
        return dataOrException
    }

}