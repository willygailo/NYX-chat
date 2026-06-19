package com.nyx.chat.data.repository

import com.nyx.chat.data.api.ChatRequest
import com.nyx.chat.data.api.ChatResponse
import com.nyx.chat.data.api.GrokApi
import com.nyx.chat.data.api.Message
import com.nyx.chat.data.local.AppDatabase
import com.nyx.chat.data.local.ConversationEntity
import com.nyx.chat.data.local.MessageEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val api: GrokApi,
    private val db: AppDatabase
) {
    private val conversationDao = db.conversationDao()
    private val messageDao = db.messageDao()

    fun observeConversations(): Flow<List<ConversationEntity>> = conversationDao.observeAll()

    fun observeMessages(conversationId: String): Flow<List<MessageEntity>> =
        messageDao.observeByConversation(conversationId)

    suspend fun getConversation(id: String): ConversationEntity? = conversationDao.getById(id)

    suspend fun sendMessage(
        conversationId: String,
        userContent: String,
        apiKey: String
    ): Result<ChatResponse> {
        val userMsg = MessageEntity(
            id = UUID.randomUUID().toString(),
            conversationId = conversationId,
            role = "user",
            content = userContent
        )
        messageDao.insert(userMsg)

        return try {
            val history = messageDao.observeByConversation(conversationId)
            val messages = listOf(
                Message(role = "system", content = "You are Nyx, a helpful AI assistant."),
                Message(role = "user", content = userContent)
            )
            val request = ChatRequest(messages = messages)
            val response = api.createChatCompletion(
                authorization = "Bearer $apiKey",
                request = request
            )

            if (response.isSuccessful) {
                val body = response.body()!!
                val reply = body.choices.first().message.content

                messageDao.insert(
                    MessageEntity(
                        id = UUID.randomUUID().toString(),
                        conversationId = conversationId,
                        role = "assistant",
                        content = reply
                    )
                )

                conversationDao.upsert(
                    conversationDao.getById(conversationId)?.copy(
                        updatedAt = System.currentTimeMillis()
                    ) ?: ConversationEntity(
                        id = conversationId,
                        title = userContent.take(80)
                    )
                )

                Result.success(body)
            } else {
                val error = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Exception(error))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createConversation(): String {
        val id = UUID.randomUUID().toString()
        conversationDao.upsert(
            ConversationEntity(id = id, title = "New chat")
        )
        return id
    }

    suspend fun deleteConversation(id: String) {
        conversationDao.deleteById(id)
    }
}
