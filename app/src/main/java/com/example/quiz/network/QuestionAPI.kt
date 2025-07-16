package com.example.quiz.network

import com.example.quiz.model.Questions
import retrofit2.http.GET
import javax.inject.Singleton


@Singleton
interface QuestionAPI {

    @GET("movies.json")
    suspend fun getQuestions(): Questions
}

// RETROFIT