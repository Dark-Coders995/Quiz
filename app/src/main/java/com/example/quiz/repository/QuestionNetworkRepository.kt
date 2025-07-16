package com.example.quiz.repository

import android.util.Log
import com.example.quiz.data.DataOrException
import com.example.quiz.model.Question

import com.example.quiz.network.QuestionNetworkAPI
import javax.inject.Inject

interface QuestionNetworkRepository {
    suspend fun getQuestions(): DataOrException<List<Question>, Boolean, Exception>
}


class QuestionNetworkRepositoryImpl @Inject constructor(
    private val api: QuestionNetworkAPI
) : QuestionNetworkRepository {

    private val dataOrException = DataOrException<List<Question>, Boolean, Exception>()
    override suspend fun getQuestions(): DataOrException<List<Question>, Boolean, Exception> {
        try {
            dataOrException.loading = true
            dataOrException.data = api.getQuestionsList()
            if (dataOrException.data.toString().isNotEmpty()) dataOrException.loading = false

        } catch (exception: Exception) {
            dataOrException.e = exception
            // Add Log D to check for messages what's exception
            Log.e("Error in Data", "${dataOrException.e!!.localizedMessage}")
        }
        return dataOrException
    }
}