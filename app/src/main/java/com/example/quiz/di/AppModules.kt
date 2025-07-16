package com.example.quiz.di

import com.example.quiz.network.QuestionAPI
import com.example.quiz.network.QuestionNetworkAPI
import com.example.quiz.repository.QuestionNetworkRepository
import com.example.quiz.repository.QuestionNetworkRepositoryImpl
import com.example.quiz.repository.QuestionRepository
import com.example.quiz.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModules {


    @Singleton
    @Provides
    fun provideQuestionRepository(api: QuestionAPI) = QuestionRepository(api)

    @Singleton
    @Provides
    fun provideQuesAPI(): QuestionAPI {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuestionAPI::class.java)
    }


    // Ktor
    @Singleton
    @Provides
    fun provideNetworking(): HttpClient {
        return HttpClient(Android) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }

    @Singleton
    @Provides
    fun provideAPI(
        client: HttpClient
    ): QuestionNetworkAPI {
        return QuestionNetworkAPI(client)
    }


    @Singleton
    @Provides
    fun provideRepository(
        api: QuestionNetworkAPI
    ): QuestionNetworkRepository {
        return QuestionNetworkRepositoryImpl(api)
    }

}