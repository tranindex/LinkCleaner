package com.linkcleaner.app

object LinkCleaner {

    // 清洗链接：移除所有 Unicode 表情符号（包括 [笑哭] 等）
    fun clean(input: String): String {
        val withoutEmojis = input.replace(Regex("[\\u2190-\\u21FF\\u2600-\\u26FF\\u2700-\\u27BF\\u3000-\\u303F\\u1F300-\\u1F64F\\u1F680-\\u1F6FF\\u{1F600}-\\u{1F64F}\\u{1F300}-\\u{1F5FF}\\u{1F680}-\\u{1F6FF}\\u{1F1E0}-\\u{1F1FF}]"), "")
        return withoutEmojis.replace(Regex("(?<=://|\\?|&|=|\\.)\\s+|\\s+(?=://|\\?|&|=|\\.)"), "")
    }

    // 补全短链前缀
    fun completeShortLink(short: String, platform: Platform): String? {
        val cleaned = short.trim().removePrefix("https://").removePrefix("http://")
        val prefix = when (platform) {
            Platform.BAIDU -> "https://pan.baidu.com/"
            Platform.QUARK -> "https://pan.quark.cn/"
        }
        return if (cleaned.startsWith("s/")) "$prefix$cleaned" else null
    }

    enum class Platform {
        BAIDU, QUARK
    }
}