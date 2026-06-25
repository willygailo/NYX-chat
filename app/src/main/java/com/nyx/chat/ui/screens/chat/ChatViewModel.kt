package com.nyx.chat.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyx.chat.data.api.AiProvider
import com.nyx.chat.data.local.MessageEntity
import com.nyx.chat.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
    val provider: AiProvider = AiProvider.NVIDIA_FREE
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var _messages: StateFlow<List<MessageEntity>>? = null

    fun getMessages(conversationId: String): StateFlow<List<MessageEntity>> {
        if (_messages == null) {
            _messages = repository.observeMessages(conversationId)
                // Eagerly keeps the flow alive even during loading/recomposition —
                // WhileSubscribed(5_000) was causing messages to wipe mid-request.
                .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        }
        return _messages!!
    }

    fun onInputChanged(text: String) = _uiState.update { it.copy(inputText = text) }

    fun sendMessage(conversationId: String) {
        val text = _uiState.value.inputText.trim()
        if (text.isEmpty() || _uiState.value.isLoading) return

        val provider = _uiState.value.provider
        _uiState.update { it.copy(inputText = "", isLoading = true, error = null) }

        viewModelScope.launch {
            repository.sendMessage(conversationId, text, provider)
                .fold(
                    onSuccess = { _uiState.update { it.copy(isLoading = false) } },
                    onFailure = { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
                )
        }
    }

    fun clearError() = _uiState.update { it.copy(error = null) }
}
