package com.nyx.chat.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nyx.chat.ui.screens.chat.ChatScreen
import com.nyx.chat.ui.screens.conversationlist.ConversationListScreen

object Routes {
    const val CONVERSATIONS = "conversations"
    const val CHAT = "chat/{conversationId}"

    fun chat(conversationId: String) = "chat/$conversationId"
}

@Composable
fun NyxNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.CONVERSATIONS
    ) {
        composable(Routes.CONVERSATIONS) {
            ConversationListScreen(
                onChatClick = { id -> navController.navigate(Routes.chat(id)) }
            )
        }
        composable(Routes.CHAT) { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getString("conversationId") ?: return@composable
            ChatScreen(
                conversationId = conversationId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
