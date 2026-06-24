package com.nyx.chat.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nyx.chat.ui.theme.Accent
import com.nyx.chat.ui.theme.RedTeamRed

/**
 * Info-only settings dialog — API key is pre-configured, no user input required.
 */
@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    onProviderChanged: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor   = Color(0xFF12121F),
        shape            = RoundedCornerShape(20.dp),
        title = {
            Column {
                Text(
                    text       = "⚙️  Red Team AI Info",
                    color      = RedTeamRed,
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text     = "v3.1.0 — system status",
                    color    = Color.Gray,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        },
        text = {
            Column {
                InfoRow(label = "🔐 API Key", value = "Pre-configured ✅")
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow(label = "🤖 Provider", value = "NVIDIA Free AI")
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow(label = "🧠 Model", value = "moonshotai/kimi-k2.6")
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow(label = "🌐 Mode", value = "Direct API (no proxy)")
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text       = "✅ Ready to operate. No setup needed.",
                    color      = Accent,
                    fontSize   = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors  = ButtonDefaults.buttonColors(containerColor = RedTeamRed),
                shape   = RoundedCornerShape(10.dp)
            ) {
                Text("CLOSE", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column {
        Text(
            text       = label,
            color      = Color.Gray,
            fontSize   = 10.sp,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text       = value,
            color      = Color.White,
            fontSize   = 13.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.SemiBold
        )
    }
}
