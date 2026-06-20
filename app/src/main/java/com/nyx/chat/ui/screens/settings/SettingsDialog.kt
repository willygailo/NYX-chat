package com.nyx.chat.ui.screens.settings

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nyx.chat.data.api.AiProvider
import com.nyx.chat.ui.theme.Accent
import com.nyx.chat.ui.theme.DarkCard
import com.nyx.chat.ui.theme.RedTeamRed

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    onProviderChanged: () -> Unit = {}
) {
    val context = LocalContext.current
    val prefs   = remember { context.getSharedPreferences("redteam_prefs", Context.MODE_PRIVATE) }

    var selectedProv by remember {
        mutableStateOf(AiProvider.fromName(prefs.getString("provider", AiProvider.GROK.name) ?: AiProvider.GROK.name))
    }
    
    val initialKeys = remember {
        AiProvider.entries.associateWith { prov ->
            prefs.getString("api_key_${prov.name}", null)
                ?: (if (prov.name == prefs.getString("provider", null)) prefs.getString("api_key", "") else "")
                ?: ""
        }.toMutableMap()
    }
    var keysState by remember { mutableStateOf(initialKeys.toMap()) }
    var showKey   by remember { mutableStateOf(false) }


    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor   = Color(0xFF12121F),
        shape            = RoundedCornerShape(20.dp),
        title            = {
            Column {
                Text(
                    text       = "⚙️  Red Team AI Settings",
                    color      = RedTeamRed,
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text     = "v2.1.0.0 — select provider & paste API key",
                    color    = Color.Gray,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            ) {
                // ── Provider grid ─────────────────────────────────────────
                Text(
                    text       = "AI PROVIDER",
                    color      = Accent,
                    fontSize   = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier   = Modifier.padding(bottom = 8.dp)
                )

                AiProvider.entries.chunked(2).forEach { row ->
                    Row(
                        modifier            = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row.forEach { provider ->
                            val isSelected = provider == selectedProv
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) RedTeamRed.copy(alpha = 0.15f) else DarkCard)
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) RedTeamRed else Color.White.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { selectedProv = provider }
                                    .padding(horizontal = 10.dp, vertical = 10.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    if (isSelected) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                            tint   = RedTeamRed,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text       = provider.displayName,
                                            color      = if (isSelected) Color.White else Color.LightGray,
                                            fontSize   = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            fontFamily = FontFamily.Monospace
                                        )
                                        Text(
                                            text       = provider.defaultModel,
                                            color      = Color.Gray,
                                            fontSize   = 9.sp,
                                            fontFamily = FontFamily.Monospace,
                                            maxLines   = 1
                                        )
                                    }
                                }
                            }
                        }
                        // pad odd row
                        if (row.size == 1) Box(modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))

                // ── API Key field ─────────────────────────────────────────
                Text(
                    text       = "API KEY — ${selectedProv.displayName}",
                    color      = Accent,
                    fontSize   = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier   = Modifier.padding(bottom = 6.dp)
                )

                val isFreeProv = selectedProv == AiProvider.NVIDIA_FREE
                val displayValue = if (isFreeProv) "Free Access (Pre-configured)" else (keysState[selectedProv] ?: "")
                OutlinedTextField(
                    value            = displayValue,
                    onValueChange    = { newKey ->
                        if (!isFreeProv) {
                            keysState = keysState.toMutableMap().apply { put(selectedProv, newKey) }
                        }
                    },
                    enabled          = !isFreeProv,
                    label            = {
                        Text(
                            text = if (isFreeProv) "API Key (Provided)" else "API Key",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    },
                    placeholder      = {
                        Text(
                            selectedProv.keyHint,
                            color      = Color.DarkGray,
                            fontFamily = FontFamily.Monospace,
                            fontSize   = 12.sp
                        )
                    },
                    singleLine       = true,
                    modifier         = Modifier.fillMaxWidth(),
                    shape            = RoundedCornerShape(12.dp),
                    visualTransformation = if (isFreeProv) VisualTransformation.None else (if (showKey) VisualTransformation.None else PasswordVisualTransformation()),
                    keyboardOptions  = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon     = {
                        if (!isFreeProv) {
                            Icon(
                                if (showKey) Icons.Default.Warning else Icons.Default.Lock,
                                contentDescription = "toggle visibility",
                                tint     = Accent,
                                modifier = Modifier.clickable { showKey = !showKey }
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = RedTeamRed,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                        disabledBorderColor  = Color.White.copy(alpha = 0.1f),
                        focusedTextColor     = Color.White,
                        unfocusedTextColor   = Color.LightGray,
                        disabledTextColor    = Color.Gray,
                        cursorColor          = RedTeamRed
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text       = "🔒 Stored locally. Never transmitted except to the selected provider.",
                    color      = Color.Gray,
                    fontSize   = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val editor = prefs.edit()
                    keysState.forEach { (prov, key) ->
                        editor.putString("api_key_${prov.name}", key.trim())
                    }
                    editor.putString("provider", selectedProv.name)
                    editor.putString("api_key", (keysState[selectedProv] ?: "").trim())
                    editor.apply()
                    onProviderChanged()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = RedTeamRed),
                shape  = RoundedCornerShape(10.dp)
            ) {
                Text("SAVE", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = Color.Gray, fontFamily = FontFamily.Monospace)
            }
        }
    )
}
