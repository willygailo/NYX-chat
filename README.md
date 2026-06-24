# 🌌 NYX Chat
### 🔴 Red Team AI v3.0.0

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
  <img src="https://img.shields.io/badge/License-MIT-yellow?style=flat-square" alt="License MIT"/>
</p>

---

<p align="center">
  <b>NYX Chat</b> (powered by <i>Red Team AI</i>) is a premium, native Android conversation client designed specifically for offensive security researchers, penetration testers, and cybersecurity enthusiasts. 
  <br/><br/>
  Featuring a hot-warning, pitch-black hacker terminal aesthetic, it comes pre-configured with a direct NVIDIA API key, offering instant access out-of-the-box with automated multi-language response matching and zero setup required.
</p>

---

## ⭐️ Support the Project

If you find this client helpful for your operations, exploit research, or daily workflows, please consider starring the repository! It helps show support and keeps the project growing.

<p align="center">
  <a href="https://github.com/willygailo/NYX-chat">
    <img src="https://img.shields.io/badge/⭐-STAR%20THIS%20REPO-FFB86C?style=for-the-badge&logo=github" alt="Star NYX Chat"/>
  </a>
</p>

---

## ✨ Key Features

- **Direct NVIDIA Free AI Integration:** Ready-to-run with a pre-configured, hard-coded API key for immediate operation.
- **Zero Server Setup:** No local backend server or proxy configuration needed! The app makes direct, secure calls out-of-the-box.
- **Hacker Terminal Aesthetic:** Designed with a sleek, pitch-black dark theme, warning-red highlights, and console-green accents using monospace typography.
- **Offline Mission Log:** All operations and conversation history are saved securely in a local Room database (SQLite) for instant retrieval.
- **Auto Language Matching:** The AI automatically detects your input language (English, Tagalog, Spanish, Japanese, etc.) and responds in the exact same tongue.
- **Offensive Security System Prompt:** Custom persona tailored for exploit development, CTFs, vulnerability scanning, OSINT guidance, and reverse engineering.

---

## 🧱 Architecture

```
com.nyx.chat/
├── data/
│   ├── api/              # Retrofit API definitions & data models
│   │   ├── AiProvider.kt     # Enum defining all 8 providers
│   │   ├── ChatRequest.kt
│   │   ├── ChatResponse.kt
│   │   └── RedTeamApi.kt     # Unified proxy endpoint interface
│   ├── local/            # Room database, entities & DAOs
│   │   ├── AppDatabase.kt
│   │   ├── ConversationDao.kt
│   │   ├── ConversationEntity.kt
│   │   ├── MessageDao.kt
│   │   └── MessageEntity.kt
│   └── repository/       # Single source of truth & dynamic Retrofit builder
│       └── ChatRepository.kt # Handles Device UUID & Backend Proxy Routing
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

### 📥 Download Latest APK
You can easily install NYX Chat by downloading the latest pre-compiled APK directly from our Releases page:
- **[Download NYX-chat-v3.0.0.apk](https://github.com/willygailo/NYX-chat/releases/latest)**

*(Optional) If you prefer to build from source, follow the instructions below:*

### Prerequisites

- **Android Studio Ladybug (2024.3+)** or newer
- **JDK 17** (configured in `gradle.properties` via `org.gradle.java.home`)
- **Android SDK 35**

### Clone & Build

```bash
# Clone the repository
git clone https://github.com/willygailo/NYX-chat.git
cd NYX-chat

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

---

<p align="center">
  Created with ❤️ by <a href="https://github.com/willygailo"><b>willygailo</b></a>
</p>
