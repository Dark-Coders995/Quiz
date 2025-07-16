package com.example.quiz.network


import com.example.quiz.model.Question
import com.example.quiz.util.Constants
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class QuestionNetworkAPI(
    private val client: HttpClient
) {
    suspend fun getQuestionsList(): List<Question> {
        val response = client.get("${Constants.BASE_URL}movies.json")
            .bodyAsText()
        return Json.decodeFromString(response)
    }
}

// KTOR