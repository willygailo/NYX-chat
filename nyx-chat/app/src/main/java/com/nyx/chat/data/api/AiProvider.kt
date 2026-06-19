package com.nyx.chat.data.api

/**
 * Red Team AI v2.1.0.0
 * Supported AI providers — all use OpenAI-compatible /v1/chat/completions endpoint
 */
enum class AiProvider(
    val displayName: String,
    val baseUrl: String,
    val defaultModel: String,
    val keyHint: String
) {
    GROK(
        displayName   = "xAI Grok",
        baseUrl       = "https://api.x.ai/",
        defaultModel  = "grok-3",
        keyHint       = "xai-..."
    ),
    OPENAI(
        displayName   = "OpenAI",
        baseUrl       = "https://api.openai.com/",
        defaultModel  = "gpt-4o",
        keyHint       = "sk-..."
    ),
    OPENROUTER(
        displayName   = "OpenRouter",
        baseUrl       = "https://openrouter.ai/api/",
        defaultModel  = "meta-llama/llama-3.3-70b-instruct",
        keyHint       = "sk-or-..."
    ),
    GROQ(
        displayName   = "Groq",
        baseUrl       = "https://api.groq.com/openai/",
        defaultModel  = "llama-3.3-70b-versatile",
        keyHint       = "gsk_..."
    ),
    MISTRAL(
        displayName   = "Mistral AI",
        baseUrl       = "https://api.mistral.ai/",
        defaultModel  = "mistral-large-latest",
        keyHint       = "..."
    ),
    TOGETHER(
        displayName   = "Together AI",
        baseUrl       = "https://api.together.xyz/",
        defaultModel  = "meta-llama/Llama-3-70b-chat-hf",
        keyHint       = "..."
    ),
    DEEPSEEK(
        displayName   = "DeepSeek",
        baseUrl       = "https://api.deepseek.com/",
        defaultModel  = "deepseek-chat",
        keyHint       = "sk-..."
    ),
    PERPLEXITY(
        displayName   = "Perplexity",
        baseUrl       = "https://api.perplexity.ai/",
        defaultModel  = "llama-3.1-sonar-large-128k-online",
        keyHint       = "pplx-..."
    );

    companion object {
        fun fromName(name: String): AiProvider =
            entries.firstOrNull { it.name == name } ?: GROK
    }
}
