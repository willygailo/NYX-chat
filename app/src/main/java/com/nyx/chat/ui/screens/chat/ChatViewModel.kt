package com.nyx.chat.ui.screens.chat

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyx.chat.data.api.AiProvider
import com.nyx.chat.data.local.MessageEntity
import com.nyx.chat.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatUiState(
    val inputText: String  = "",
    val isLoading: Boolean = false,
    val error: String?     = null,
    val provider: AiProvider = AiProvider.GROK
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState(provider = loadProvider()))
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var _messages: StateFlow<List<MessageEntity>>? = null

    fun getMessages(conversationId: String): StateFlow<List<MessageEntity>> {
        if (_messages == null) {
            _messages = repository.observeMessages(conversationId)
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
        }
        return _messages!!
    }

    fun onInputChanged(text: String) = _uiState.update { it.copy(inputText = text) }

    fun sendMessage(conversationId: String) {
        val text = _uiState.value.inputText.trim()
        if (text.isEmpty() || _uiState.value.isLoading) return

        val apiKey = getApiKey()
        if (apiKey.isNullOrEmpty()) {
            _uiState.update { it.copy(error = "⚠️ No API key set — go to ⚙️ Settings first!") }
            return
        }

        val provider = _uiState.value.provider
        _uiState.update { it.copy(inputText = "", isLoading = true, error = null) }

        viewModelScope.launch {
            repository.sendMessage(conversationId, text, apiKey, provider)
                .fold(
                    onSuccess = { _uiState.update { it.copy(isLoading = false) } },
                    onFailure = { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
                )
        }
    }

    fun clearError() = _uiState.update { it.copy(error = null) }

    fun reloadProvider() = _uiState.update { it.copy(provider = loadProvider()) }

    // ── Prefs helpers ────────────────────────────────────────────────────────
    private fun prefs() = context.getSharedPreferences("redteam_prefs", Context.MODE_PRIVATE)

    private fun getApiKey(): String? {
        val provider = loadProvider()
        if (provider == AiProvider.NVIDIA_FREE) {
            return "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"
        }
        return prefs().getString("api_key_${provider.name}", null)
            ?: prefs().getString("api_key", null)
    }

    private fun loadProvider(): AiProvider =
        AiProvider.fromName(prefs().getString("provider", AiProvider.GROK.name) ?: AiProvider.GROK.name)

}
