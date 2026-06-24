package com.nyx.chat.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nyx.chat.ui.screens.chat.ChatScreen
import com.nyx.chat.ui.screens.conversationlist.ConversationListScreen
import com.nyx.chat.ui.screens.conversationlist.ConversationListViewModel

object Routes {
    const val LAUNCHER     = "launcher"
    const val CONVERSATIONS = "conversations"
    const val CHAT = "chat/{conversationId}"

    fun chat(conversationId: String) = "chat/$conversationId"
}

@Composable
fun NyxNavGraph(navController: NavHostController) {
    NavHost(
        navController    = navController,
        startDestination = Routes.LAUNCHER
    ) {
        // ── Auto-launcher: creates a new chat and jumps straight into it ──────
        composable(Routes.LAUNCHER) {
            val viewModel: ConversationListViewModel = hiltViewModel()
            val conversations by viewModel.conversations.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.createNewChat { newId ->
                    navController.navigate(Routes.chat(newId)) {
                        // Remove the launcher from back-stack so Back goes to conversation list
                        popUpTo(Routes.LAUNCHER) { inclusive = true }
                    }
                }
            }
        }

        // ── Conversation list (accessible via back from chat) ─────────────────
        composable(Routes.CONVERSATIONS) {
            ConversationListScreen(
                onChatClick = { id ->
                    navController.navigate(Routes.chat(id))
                }
            )
        }

        // ── Chat screen ───────────────────────────────────────────────────────
        composable(Routes.CHAT) { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getString("conversationId") ?: return@composable
            ChatScreen(
                conversationId = conversationId,
                onBack = {
                    // Navigate to conversation list on back-press from chat
                    navController.navigate(Routes.CONVERSATIONS) {
                        popUpTo(Routes.CONVERSATIONS) { inclusive = true }
                    }
                }
            )
        }
    }
}
