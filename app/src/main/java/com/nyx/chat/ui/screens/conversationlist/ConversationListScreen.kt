package com.nyx.chat.ui.screens.conversationlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nyx.chat.data.local.ConversationEntity
import com.nyx.chat.ui.screens.settings.SettingsDialog
import com.nyx.chat.ui.theme.Accent
import com.nyx.chat.ui.theme.DarkBackground
import com.nyx.chat.ui.theme.DarkCard
import com.nyx.chat.ui.theme.RedTeamRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationListScreen(
    onChatClick: (String) -> Unit,
    viewModel: ConversationListViewModel = hiltViewModel()
) {
    val conversations   by viewModel.conversations.collectAsState()
    var showDeleteDialog by remember { mutableStateOf<ConversationEntity?>(null) }
    var showSettings     by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text       = "🔴 RED TEAM AI",
                            color      = RedTeamRed,
                            fontSize   = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text       = "v3.1.0 · Offensive Security AI",
                            color      = Accent,
                            fontSize   = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0A0A12)
                ),
                actions = {
                    IconButton(onClick = { showSettings = true }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Accent)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick          = { viewModel.createNewChat(onChatClick) },
                containerColor   = RedTeamRed,
                contentColor     = Color.White,
                shape            = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "New Mission", modifier = Modifier.size(28.dp))
            }
        }
    ) { padding ->
        if (conversations.isEmpty()) {
            Box(
                modifier         = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🔴", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text       = "NO MISSIONS YET",
                        color      = RedTeamRed,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace,
                        fontSize   = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text       = "Tap + to launch a new operation",
                        color      = Color.Gray,
                        fontFamily = FontFamily.Monospace,
                        fontSize   = 12.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text       = "🔴 All systems operational — ready to execute",
                        color      = Accent.copy(alpha = 0.7f),
                        fontFamily = FontFamily.Monospace,
                        fontSize   = 11.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                items(conversations, key = { it.id }) { conversation ->
                    ConversationItem(
                        conversation = conversation,
                        onClick      = { onChatClick(conversation.id) },
                        onDelete     = { showDeleteDialog = conversation }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

    if (showSettings) {
        SettingsDialog(onDismiss = { showSettings = false })
    }

    showDeleteDialog?.let { conversation ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            containerColor   = Color(0xFF12121F),
            title            = {
                Text("Delete Mission?", color = RedTeamRed, fontFamily = FontFamily.Monospace)
            },
            text             = {
                Text(
                    "This operation log will be wiped permanently.",
                    color      = Color.LightGray,
                    fontFamily = FontFamily.Monospace,
                    fontSize   = 13.sp
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteConversation(conversation.id)
                    showDeleteDialog = null
                }) {
                    Text("DELETE", color = RedTeamRed, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text("ABORT", color = Color.Gray, fontFamily = FontFamily.Monospace)
                }
            }
        )
    }
}

@Composable
private fun ConversationItem(
    conversation: ConversationEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM d · h:mm a", Locale.getDefault()) }

    Card(
        colors   = CardDefaults.cardColors(containerColor = DarkCard),
        shape    = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Terminal-style icon
            Box(
                modifier         = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF1A0000)),
                contentAlignment = Alignment.Center
            ) {
                Text("🔴", fontSize = 18.sp)
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text       = conversation.title,
                    style      = MaterialTheme.typography.titleSmall,
                    color      = Color.White,
                    fontFamily = FontFamily.Monospace,
                    maxLines   = 1,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text       = dateFormat.format(Date(conversation.updatedAt)),
                    style      = MaterialTheme.typography.labelSmall,
                    color      = Accent.copy(alpha = 0.6f),
                    fontFamily = FontFamily.Monospace
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = RedTeamRed.copy(alpha = 0.5f)
                )
            }
        }
    }
}
