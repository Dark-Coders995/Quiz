package com.example.quiz.model


import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val answer: String,
    val category: String,
    val choices: List<String>,
    val question: String
)


// FOR KTOR