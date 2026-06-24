# 🌌 NYX Chat
### 🔴 Red Team AI v3.1.0

<p align="center">
  <a href="https://github.com/willygailo/NYX-chat/stargazers">
    <img src="https://img.shields.io/github/stars/willygailo/NYX-chat?style=for-the-badge&color=ff5555&logo=github" alt="Stars"/>
  </a>
  <a href="https://github.com/willygailo/NYX-chat/network/members">
    <img src="https://img.shields.io/github/forks/willygailo/NYX-chat?style=for-the-badge&color=50fa7b&logo=github" alt="Forks"/>
  </a>
  <a href="https://github.com/willygailo">
    <img src="https://img.shields.io/badge/Author-willygailo-BD93F9?style=for-the-badge&logo=github&logoColor=white" alt="Author"/>
  </a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-2.1.0-%237F52FF?style=flat-square&logo=kotlin&logoColor=white" alt="Kotlin 2.1.0"/>
  <img src="https://img.shields.io/badge/Android%20Compile%20SDK-35-3DDC84?style=flat-square&logo=android&logoColor=white" alt="Compile SDK 35"/>
  <img src="https://img.shields.io/badge/Android%20Min%20SDK-26-3DDC84?style=flat-square&logo=android&logoColor=white" alt="Min SDK 26"/>
  <img src="https://img.shields.io/badge/Model-Kimi%20K2-FF5555?style=flat-square&logo=nvidia&logoColor=white" alt="Model Kimi K2"/>
  <img src="https://img.shields.io/badge/License-MIT-yellow?style=flat-square" alt="License MIT"/>
</p>

---

<p align="center">
  <b>NYX Chat</b> (powered by <i>Red Team AI</i>) is a premium, native Android conversation client designed specifically for offensive security researchers, penetration testers, and cybersecurity enthusiasts.
  <br/><br/>
  Featuring a hot-warning, pitch-black hacker terminal aesthetic, it connects <b>directly</b> to the NVIDIA AI API (Kimi K2 model) — offering instant access out-of-the-box with automated multi-language response matching and <b>zero setup required</b>.
</p>

---

## ⭐️ Support the Project & Feedback

Thank you so much for using **NYX Chat**! If you liked the application or want to request new features, feel free to open an issue or share your feedback.

If you find this client helpful for your operations, exploit research, or daily workflows, please consider starring the repository! A little support goes a long way and helps keep this project active and growing.

<p align="center">
  <a href="https://github.com/willygailo/NYX-chat">
    <img src="https://img.shields.io/badge/⭐-STAR%20THIS%20REPO-FFB86C?style=for-the-badge&logo=github" alt="Star NYX Chat"/>
  </a>
</p>

---

## ✨ Key Features

- **Direct NVIDIA Free AI Integration:** Ready-to-run with a pre-configured API key — calls go straight to `integrate.api.nvidia.com`. No proxy, no backend, no setup.
- **Zero Server Setup:** No local backend server or proxy configuration needed. Install the APK and it works immediately.
- **Model: moonshotai/kimi-k2.6:** Powered by the Kimi K2 large language model via NVIDIA's free inference endpoint.
- **Hacker Terminal Aesthetic:** Sleek, pitch-black dark theme with warning-red highlights and console-green accents using monospace typography.
- **Offline Mission Log:** All operations and conversation history are saved securely in a local Room database (SQLite) for instant retrieval.
- **Full Message Context:** Sends up to the last 40 messages as history on every request for coherent, context-aware responses.
- **Auto Language Matching:** The AI automatically detects your input language (English, Tagalog, Spanish, etc.) and responds in the exact same tongue.
- **Offensive Security System Prompt:** Custom Red Team AI persona tailored for exploit development, CTFs, OSINT, vulnerability scanning, and reverse engineering.

---

## 🧱 Architecture

```
com.nyx.chat/
├── data/
│   ├── api/                  # Retrofit API definitions & data models
│   │   ├── AiProvider.kt     # Enum defining all supported providers & base URLs
│   │   ├── ChatRequest.kt    # OpenAI-compatible request body
│   │   ├── ChatResponse.kt   # OpenAI-compatible response body
│   │   └── RedTeamApi.kt     # Retrofit interface (direct API mode)
│   ├── local/                # Room database, entities & DAOs
│   │   ├── AppDatabase.kt
│   │   ├── ConversationDao.kt
│   │   ├── ConversationEntity.kt
│   │   ├── MessageDao.kt
│   │   └── MessageEntity.kt
│   └── repository/           # Single source of truth
│       └── ChatRepository.kt # Builds Retrofit per provider, sends chat requests
├── di/                       # Dagger Hilt Modules
│   └── NetworkModule.kt      # (intentionally minimal — network built dynamically)
├── ui/
│   ├── navigation/           # Jetpack Compose navigation graph
│   │   └── NavGraph.kt       # Auto-launches into a new chat on startup
│   ├── screens/
│   │   ├── chat/             # Terminal-style chat screen & ViewModel
│   │   ├── conversationlist/ # Card-based operation history screen
│   │   └── settings/         # Info dialog (API pre-configured, read-only)
│   └── theme/                # Pitch-black dark theme & monospace typography
├── MainActivity.kt           # Single activity host
└── NyxApp.kt                 # Application controller (@HiltAndroidApp)
```

---

## 🚀 Getting Started

### 📥 Download Latest APK
Install NYX Chat instantly by downloading the latest pre-compiled APK from the Releases page:

- **[Download NYX-chat-v3.1.0.apk](https://github.com/willygailo/NYX-chat/releases/latest)**

> **No API key needed.** Just install and start chatting — the app connects directly to NVIDIA AI out-of-the-box.

*(Optional) If you prefer to build from source, follow the instructions below:*

---

### 🛠 Build from Source

#### Prerequisites

| Tool | Version |
|------|---------|
| Android Studio | Ladybug (2024.3+) or newer |
| JDK | 17 |
| Android SDK | 35 |

#### Clone & Build

```bash
# Clone the repository
git clone https://github.com/willygailo/NYX-chat.git
cd NYX-chat

# Compile and package debug APK
./gradlew clean assembleDebug

# Install on connected device via ADB
adb install app/build/outputs/apk/debug/app-debug.apk
```

#### Install Without ADB

If you don't have ADB set up, transfer the APK to your phone manually:

1. Copy `app/build/outputs/apk/debug/app-debug.apk` to your phone (via USB file transfer, Google Drive, etc.)
2. On your phone: **Settings → Apps → Special App Access → Install Unknown Apps** → allow your file manager
3. Tap the APK file to install

---

## 🌐 API Configuration

| Setting | Value |
|---------|-------|
| Provider | NVIDIA Free AI |
| Endpoint | `https://integrate.api.nvidia.com/v1/chat/completions` |
| Model | `moonshotai/kimi-k2.6` |
| Auth | Pre-configured Bearer token |
| Mode | Direct API (no proxy, no backend server) |
| Max History | Last 40 messages per request |
| Max Tokens | 2048 per response |

> **Want to use your own NVIDIA API key?** Get a free key at [build.nvidia.com](https://build.nvidia.com), then update `NVIDIA_API_KEY` in [`ChatRepository.kt`](app/src/main/java/com/nyx/chat/data/repository/ChatRepository.kt).

---

## 🛠 Tech Stack

| Layer | Library |
|-------|---------|
| UI | Jetpack Compose + Material 3 |
| DI | Dagger Hilt |
| Persistence | Room (SQLite) |
| Networking | Retrofit 2 + OkHttp 4 |
| Serialization | Gson |
| Markdown | Markwon |

---

## 📋 Changelog

### v3.1.0 *(Current)*
- ✅ **Removed local proxy server dependency** — app now calls NVIDIA API directly
- ✅ **Hardcoded NVIDIA API key** — zero configuration required on install
- ✅ **Auto-launch** — app opens directly into a new chat on startup
- ✅ **Settings simplified** — read-only info dialog (no more key input needed)
- ✅ **System prompt upgraded** — Red Team AI v3.1.0 persona with full capability list
- ✅ **Kimi K2 model** — upgraded to `moonshotai/kimi-k2.6`

### v2.1.0.0
- Required a local Node.js proxy server running on the same Wi-Fi network
- Supported multiple providers (Grok, OpenAI, OpenRouter, Groq, Mistral, etc.) with user-entered API keys
- Settings dialog allowed provider selection and API key input per provider

---

<p align="center">
  Created with ❤️ by <a href="https://github.com/willygailo"><b>willygailo</b></a>
</p>
