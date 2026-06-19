-keepattributes Signature
-keepattributes *Annotation*

# Gson
-keepattributes Signature
-keep class com.nyx.chat.data.api.** { *; }

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# Retrofit
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
