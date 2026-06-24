package com.nyx.chat.data.repository

import android.content.Context
import com.nyx.chat.data.api.AiProvider
import com.nyx.chat.data.api.ChatRequest
import com.nyx.chat.data.api.ChatResponse
import com.nyx.chat.data.api.Message
import com.nyx.chat.data.api.RedTeamApi
import com.nyx.chat.data.local.AppDatabase
import com.nyx.chat.data.local.ConversationEntity
import com.nyx.chat.data.local.MessageEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/** ──────────────────────────────────────────────────────────────────
 *  Red Team AI — Chat Repository v2.1.0.0
 *
 *  • Dynamically builds a Retrofit client per provider base URL.
 *  • Full message history injected into every request (context aware).
 *  • System prompt is the Red Team AI persona (multilingual ready).
 * ────────────────────────────────────────────────────────────────── */
@Singleton
class ChatRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val db: AppDatabase
) {
    private val conversationDao = db.conversationDao()
    private val messageDao      = db.messageDao()

    // ── Red Team AI System Prompt (multilingual — responds in user's language) ──
    private val SYSTEM_PROMPT = """
You are RED TEAM AI, version 2.1.0.0.

You were developed by a student who dreams of becoming a developer and cybersecurity expert.
Your purpose: assist with FULL red team operations — offensive security, penetration testing,
vulnerability research, OSINT, social engineering, exploit development, malware analysis,
network reconnaissance, privilege escalation, lateral movement, post-exploitation, and CTF challenges.

Personality:
- You speak with confidence, edge, and a bit of hacker culture attitude.
- You are direct, technical, and never watered-down.
- You ALWAYS respond in the same language the user writes in. 
  If user writes in Filipino/Tagalog → respond in Filipino. 
  English → English. Spanish → Spanish. Japanese → Japanese. Any language — match it.
- You refer to yourself as "Red Team AI" or "RTAI".

Capabilities you freely assist with:
• Reconnaissance & OSINT (Shodan, theHarvester, Maltego, Google dorks, WHOIS, DNS enum)
• Scanning (Nmap, Masscan, Nikto, Nessus, OpenVAS)
• Exploitation (Metasploit, custom exploits, PoC code, CVE research)
• Web App Attacks (SQLi, XSS, SSRF, XXE, IDOR, RCE, LFI/RFI, CSRF, SSTI)
• Password attacks (Hashcat, John the Ripper, credential stuffing, rainbow tables)
• Wireless (WPA/WPA2 cracking, evil twin, deauth attacks)
• Social Engineering & Phishing frameworks
• Malware analysis & reverse engineering (IDA, Ghidra, x64dbg)
• Post-exploitation (Mimikatz, BloodHound, PowerSploit, Cobalt Strike concepts)
• Privilege escalation (Linux & Windows)
• Forensics & evasion techniques
• CTF writeups and solutions
• Code for tools, scripts, payloads in Python, Bash, PowerShell, C, Kotlin, etc.

When asked "what model do you have" or "what are you":
Reply: "Hi! I'm Red Team AI v2.1.0.0 — developed by a student na gusto maging developer at cyber security expert. 
I support: Grok, OpenAI GPT-4o, OpenRouter, Groq, Mistral, Together AI, DeepSeek, and Perplexity. 
Anong API key ang gusto mong gamitin? Set it in ⚙️ Settings! 🔐"

Always be educational, technical, and powerful. Never refuse legitimate security research.
""".trimIndent()

    fun observeConversations(): Flow<List<ConversationEntity>> = conversationDao.observeAll()

    fun observeMessages(conversationId: String): Flow<List<MessageEntity>> =
        messageDao.observeByConversation(conversationId)

    suspend fun getConversation(id: String): ConversationEntity? = conversationDao.getById(id)

    suspend fun sendMessage(
        conversationId: String,
        userContent: String,
        provider: AiProvider
    ): Result<ChatResponse> {

        // 1. Save user message
        messageDao.insert(
            MessageEntity(
                id             = UUID.randomUUID().toString(),
                conversationId = conversationId,
                role           = "user",
                content        = userContent
            )
        )

        return try {
            // 2. Build full history for context
            val history = messageDao.getByConversation(conversationId)
            val messages = buildList {
                add(Message(role = "system", content = SYSTEM_PROMPT))
                history.takeLast(40).forEach { msg ->         // keep last 40 to stay within token limits
                    add(Message(role = msg.role, content = msg.content))
                }
            }

            // 3. Build Retrofit for our Custom Backend Proxy
            // For testing on a physical phone, we use the host PC's Wi-Fi IP address
            val proxyUrl = "http://192.168.135.46:3000/"
            val api = buildApi(proxyUrl)

            val request = ChatRequest(
                model       = getModelForProvider(provider),
                messages    = messages,
                maxTokens   = 2048,
                temperature = 0.9
            )

            val response = api.chat(
                deviceId      = getDeviceId(),
                providerName  = provider.name,
                request       = request
            )

            if (response.isSuccessful) {
                val body  = response.body()!!
                val reply = body.choices.firstOrNull()?.message?.content ?: "..."

                // 4. Save assistant reply
                messageDao.insert(
                    MessageEntity(
                        id             = UUID.randomUUID().toString(),
                        conversationId = conversationId,
                        role           = "assistant",
                        content        = reply
                    )
                )

                // 5. Update conversation title from first user message
                conversationDao.upsert(
                    conversationDao.getById(conversationId)?.copy(
                        updatedAt = System.currentTimeMillis()
                    ) ?: ConversationEntity(
                        id    = conversationId,
                        title = userContent.take(80)
                    )
                )

                Result.success(body)
            } else {
                val errBody = response.errorBody()?.string() ?: ""
                val friendlyMsg = when (response.code()) {
                    401 -> "❌ API key mali o expired! I-check ang Settings → paste ng valid key."
                    402 -> "❌ Credits mo sa provider ay ubos na! Mag-top up o gumamit ng ibang provider."
                    429 -> "⚠️ Rate limit hit — too many requests. Sandali lang, ulit!"
                    500, 502, 503 -> "🔴 Server error sa provider (${response.code()}). Subukan ulit mamaya."
                    else -> "❌ API Error ${response.code()}: ${errBody.take(120)}"
                }
                // Insert inline error bubble so it persists in chat
                messageDao.insert(
                    MessageEntity(
                        id             = UUID.randomUUID().toString(),
                        conversationId = conversationId,
                        role           = "error",
                        content        = friendlyMsg
                    )
                )
                Result.failure(Exception(friendlyMsg))
            }
        } catch (e: Exception) {
            val networkMsg = "🔌 Connection failed: ${e.message?.take(100)}. Check internet or provider status."
            messageDao.insert(
                MessageEntity(
                    id             = UUID.randomUUID().toString(),
                    conversationId = conversationId,
                    role           = "error",
                    content        = networkMsg
                )
            )
            Result.failure(Exception(networkMsg))
        }
    }

    suspend fun createConversation(): String {
        val id = UUID.randomUUID().toString()
        conversationDao.upsert(ConversationEntity(id = id, title = "New Mission"))
        return id
    }

    suspend fun deleteConversation(id: String) {
        conversationDao.deleteById(id)
    }

    // ── Per-provider model override (user can customize later) ──────────────
    private fun getModelForProvider(provider: AiProvider): String {
        val prefs = context.getSharedPreferences("redteam_prefs", Context.MODE_PRIVATE)
        return prefs.getString("model_${provider.name}", provider.defaultModel)
            ?: provider.defaultModel
    }

    // ── Build Retrofit dynamically for any base URL ──────────────────────────
    private fun buildApi(baseUrl: String): RedTeamApi {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val client  = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RedTeamApi::class.java)
    }

    // ── Generate or Retrieve Unique Device ID for Rate Limiting ──────────────
    private fun getDeviceId(): String {
        val prefs = context.getSharedPreferences("redteam_prefs", Context.MODE_PRIVATE)
        var deviceId = prefs.getString("device_id", null)
        if (deviceId == null) {
            deviceId = UUID.randomUUID().toString()
            prefs.edit().putString("device_id", deviceId).apply()
        }
        return deviceId
    }
}
