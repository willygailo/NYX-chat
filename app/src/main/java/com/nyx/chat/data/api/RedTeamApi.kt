package com.nyx.chat.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * OpenAI-compatible endpoint — works with every provider in AiProvider enum.
 * Base URL is swapped at runtime via a dedicated OkHttpClient interceptor.
 */
interface RedTeamApi {

    @POST("v1/chat/completions")
    suspend fun chat(
        @Header("x-device-id") deviceId: String,
        @Header("x-ai-provider") providerName: String,
        @Body request: ChatRequest
    ): Response<ChatResponse>
}
