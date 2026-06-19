# 🔴 Red Team AI v2.1.0.0

<p align="center">
  <img src="https://img.shields.io/badge/kotlin-2.1.0-%237F52FF?logo=kotlin" alt="Kotlin 2.1.0"/>
  <img src="https://img.shields.io/badge/compile_sdk-35-3DDC84?logo=android" alt="Compile SDK 35"/>
  <img src="https://img.shields.io/badge/min_sdk-26-3DDC84?logo=android" alt="Min SDK 26"/>
  <img src="https://img.shields.io/badge/license-MIT-yellow" alt="License MIT"/>
  <img src="https://img.shields.io/badge/build-passing-brightgreen" alt="Build Passing"/>
</p>

<p align="center">
  <b>Red Team AI</b> — a premium, native Android conversation client designed for offensive security researchers and cybersecurity enthusiasts. 
  Supports 8 AI providers with independent API key storage, a hacker-terminal aesthetic, and automatic multi-language response matching.
</p>

---

## ✨ Features

- **8 Core AI Providers:** Connect dynamically to xAI Grok, OpenAI, OpenRouter, Groq, Mistral AI, Together AI, DeepSeek, and Perplexity.
- **Independent API Key Storage:** Save keys for multiple providers simultaneously without overwriting them. Stored locally via `SharedPreferences`.
- **Hacker Terminal Aesthetic:** Pitch black backgrounds, hot warning reds, and console-green accents with monospace fonts for titles/logs.
- **Offline Mission Log:** Every conversation is saved securely in a local Room database (SQLite) so you can review reconnaissance files anytime.
- **Auto Language Matching:** The AI automatically detects your input language (English, Filipino/Tagalog, Spanish, Japanese, etc.) and responds in the exact same tongue.
- **Offensive Security System Prompt:** Custom persona geared towards exploit development, CTFs, vulnerability scanning guidance, OSINT, and reverse engineering.

---

## 🧱 Architecture

```
com.nyx.chat/
├── data/
│   ├── api/              # Retrofit API definitions & data models
│   │   ├── AiProvider.kt     # Enum defining all 8 providers
│   │   ├── ChatRequest.kt
│   │   ├── ChatResponse.kt
│   │   └── RedTeamApi.kt     # Unified endpoint interface
│   ├── local/            # Room database, entities & DAOs
│   │   ├── AppDatabase.kt
│   │   ├── ConversationDao.kt
│   │   ├── ConversationEntity.kt
│   │   ├── MessageDao.kt
│   │   └── MessageEntity.kt
│   └── repository/       # Single source of truth & dynamic Retrofit builder
│       └── ChatRepository.kt
├── di/                   # Dagger Hilt Modules
│   └── AppModule.kt
├── ui/
│   ├── navigation/       # Jetpack Compose navigation graph
│   │   └── NavGraph.kt
│   ├── screens/
│   │   ├── chat/         # Terminal-style chat & context loader
│   │   ├── conversationlist/ # Card-based operation threads
│   │   └── settings/     # Multi-key settings dialog
│   └── theme/            # Pitch-black dark theme & monospace typography
├── MainActivity.kt       # Single activity host
└── NyxApp.kt             # Application controller
```

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Ladybug (2024.3+) or newer
- JDK 17 (pre-configured in `gradle.properties` via `org.gradle.java.home`)
- Android SDK 35

### Clone & Build

```bash
git clone https://github.com/willygailo/Cline.git
cd Cline/nyx-chat

# Compile and package debug APK
./gradlew assembleDebug

# Install on device or emulator
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## 🛠 Tech Stack

- **UI:** Jetpack Compose + Material 3
- **DI:** Dagger Hilt
- **Persistence:** Room (SQLite)
- **Networking:** Retrofit + OkHttp (dynamic switching)
- **Serialization:** Gson
- **Markdown:** Markwon
