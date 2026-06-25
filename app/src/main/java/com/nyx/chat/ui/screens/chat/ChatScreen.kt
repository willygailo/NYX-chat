package com.nyx.chat.ui.screens.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nyx.chat.data.local.MessageEntity
import com.nyx.chat.ui.theme.Accent
import com.nyx.chat.ui.theme.BotBubble
import com.nyx.chat.ui.theme.DarkBackground
import com.nyx.chat.ui.theme.RedTeamRed
import com.nyx.chat.ui.theme.TerminalGreen
import com.nyx.chat.ui.theme.UserBubble

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    conversationId: String,
    onBack: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val messages        by viewModel.getMessages(conversationId).collectAsState()
    val uiState         by viewModel.uiState.collectAsState()
    val listState        = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1)
    }

    // Scroll to bottom when thinking bubble appears
    LaunchedEffect(uiState.isLoading) {
        if (uiState.isLoading && messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text       = "🔴 RED TEAM AI",
                            color      = RedTeamRed,
                            fontSize   = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text       = "v3.1.2 · ${uiState.provider.displayName}",
                            color      = Accent,
                            fontSize   = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = RedTeamRed
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0A0A12)
                )
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData    = data,
                    containerColor  = Color(0xFF1A0000),
                    contentColor    = RedTeamRed
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(DarkBackground)
        ) {
            // ── Message list ─────────────────────────────────────────────
            LazyColumn(
                state    = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                // Welcome banner if empty
                if (messages.isEmpty()) {
                    item {
                        WelcomeBanner()
                    }
                }

                items(messages, key = { it.id }) { message ->
                    AnimatedVisibility(
                        visible = true,
                        enter   = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
                    ) {
                        MessageBubble(message = message)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // ── Thinking bubble (shown while AI is loading) ───────────
                if (uiState.isLoading) {
                    item(key = "thinking") {
                        AnimatedVisibility(
                            visible = true,
                            enter   = fadeIn()
                        ) {
                            ThinkingBubble()
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            // ── Input bar ────────────────────────────────────────────────
            InputBar(
                text        = uiState.inputText,
                isLoading   = uiState.isLoading,
                onTextChange = viewModel::onInputChanged,
                onSend      = { viewModel.sendMessage(conversationId) }
            )
        }
    }
}

// ── Welcome banner (shown on empty chat) ──────────────────────────────────────
@Composable
private fun WelcomeBanner() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue  = 1f,
        animationSpec = infiniteRepeatable(
            animation  = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alphaAnim"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text       = "🔴",
                fontSize   = 48.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text       = "RED TEAM AI v3.1.2",
                color      = RedTeamRed.copy(alpha = alpha),
                fontSize   = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text       = "Full offensive security AI",
                color      = Accent,
                fontSize   = 12.sp,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text       = "Recon · Exploit · Post-Exploitation · CTF",
                color      = Color.Gray,
                fontSize   = 10.sp,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text       = "Type anything — responds in your language 🌐",
                color      = TerminalGreen.copy(alpha = 0.7f),
                fontSize   = 11.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

// ── Message bubble ────────────────────────────────────────────────────────────
@Composable
private fun MessageBubble(message: MessageEntity) {
    val isUser  = message.role == "user"
    val isError = message.role == "error"

    Box(
        modifier          = Modifier.fillMaxWidth(),
        contentAlignment  = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            // role label
            Text(
                text       = when {
                    isUser  -> "YOU"
                    isError -> "⚠ SYSTEM"
                    else    -> "RTAI"
                },
                color      = when {
                    isUser  -> UserBubble
                    isError -> Color(0xFFFF4444)
                    else    -> RedTeamRed
                },
                fontSize   = 9.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                modifier   = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )

            val bubbleGradient = when {
                isUser  -> Brush.horizontalGradient(listOf(Color(0xFF7C3AED), Color(0xFF9333EA)))
                isError -> Brush.horizontalGradient(listOf(Color(0xFF2A0000), Color(0xFF3A0A0A)))
                else    -> Brush.horizontalGradient(listOf(Color(0xFF0F0F1A), Color(0xFF1A1A2E)))
            }

            Card(
                colors   = CardDefaults.cardColors(containerColor = Color.Transparent),
                shape    = RoundedCornerShape(
                    topStart    = 16.dp,
                    topEnd      = 16.dp,
                    bottomStart = if (isUser) 16.dp else 4.dp,
                    bottomEnd   = if (isUser) 4.dp else 16.dp
                ),
                modifier = Modifier
                    .widthIn(max = 320.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(bubbleGradient)
            ) {
                Text(
                    text     = message.content,
                    style    = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize   = 13.sp,
                        lineHeight = 20.sp
                    ),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    color    = when {
                        isUser  -> Color.White
                        isError -> Color(0xFFFF6B6B)
                        else    -> Accent
                    }
                )
            }
        }
    }
}

// ── Input bar ─────────────────────────────────────────────────────────────────
@Composable
private fun InputBar(
    text: String,
    isLoading: Boolean,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0A0A12))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value           = text,
            onValueChange   = onTextChange,
            placeholder     = {
                Text(
                    "Mission input...",
                    color      = Color.DarkGray,
                    fontFamily = FontFamily.Monospace,
                    fontSize   = 13.sp
                )
            },
            modifier        = Modifier.weight(1f),
            shape           = RoundedCornerShape(16.dp),
            maxLines        = 5,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { if (!isLoading) onSend() }),
            colors          = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = RedTeamRed,
                unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                focusedTextColor     = Color.White,
                unfocusedTextColor   = Color.LightGray,
                cursorColor          = RedTeamRed
            ),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = FontFamily.Monospace,
                fontSize   = 13.sp
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick  = onSend,
            enabled  = !isLoading && text.isNotBlank(),
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (text.isNotBlank() && !isLoading) RedTeamRed else Color(0xFF2A0000))
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier    = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color       = RedTeamRed
                )
            } else {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ── Thinking bubble (animated terminal cursor while AI processes) ──────────────
@Composable
private fun ThinkingBubble() {
    val infiniteTransition = rememberInfiniteTransition(label = "cursor_blink")
    val cursorAlpha by infiniteTransition.animateFloat(
        initialValue  = 1f,
        targetValue   = 0f,
        animationSpec = infiniteRepeatable(
            animation  = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursorAlpha"
    )

    Box(
        modifier         = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text       = "RTAI",
                color      = RedTeamRed,
                fontSize   = 9.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                modifier   = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 4.dp))
                    .background(
                        Brush.horizontalGradient(listOf(Color(0xFF0F0F1A), Color(0xFF1A1A2E)))
                    )
                    .padding(horizontal = 18.dp, vertical = 12.dp)
            ) {
                Text(
                    text       = "▌",
                    color      = TerminalGreen.copy(alpha = cursorAlpha),
                    fontSize   = 18.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
