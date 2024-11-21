package com.example.maintainmate.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class ChatRequest(
    val messages: List<Message>,
    val model: String,
    val max_tokens: Int,
    val temperature: Double
)

data class Message(
    val role: String,
    val content: String
)

data class ChatResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: MessageContent
)

data class MessageContent(
    val role: String,
    val content: String
)

interface GPTApi {
    @Headers(
        "Content-Type: application/json",
        "x-rapidapi-host: cheapest-gpt-4-turbo-gpt-4-vision-chatgpt-openai-ai-api.p.rapidapi.com",
        "x-rapidapi-key: 46992b30ecmsh3522d13b22ec6a1p1b078fjsnad3293458f44"
    )
    @POST("chat/completions")
    fun getChatResponse(
        @Body request: ChatRequest
    ): Call<ChatResponse>
}
