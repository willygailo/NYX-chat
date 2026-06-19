package com.nyx.chat.ui.screens.conversationlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyx.chat.data.local.ConversationEntity
import com.nyx.chat.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationListViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    val conversations: StateFlow<List<ConversationEntity>> = repository.observeConversations()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun createNewChat(onCreated: (String) -> Unit) {
        viewModelScope.launch {
            val id = repository.createConversation()
            onCreated(id)
        }
    }

    fun deleteConversation(id: String) {
        viewModelScope.launch {
            repository.deleteConversation(id)
        }
    }
}
