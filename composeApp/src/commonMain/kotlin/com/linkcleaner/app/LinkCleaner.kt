package com.linkcleaner.app

object LinkCleaner {

    // 清洗链接：移除 Unicode 原生表情 和 QQ方括号表情（如 [笑哭]）
    fun clean(input: String): String {
        return try {
            // 匹配原生 Emoji
            val emojiRegex = Regex(
                "[\\u2190-\\u21FF\\u2600-\\u26FF\\u2700-\\u27BF" +
                "\\u3000-\\u303F\\uD83C\\uDF00-\\uD83D\\uDEFF" +
                "\\uD83D\\uDE00-\\uD83D\\uDE4F\\uD83D\\uDE80-\\uD83D\\uDEFF" +
                "\\uD83C\\uDDE6-\\uD83C\\uDDFF]"
            )
            // 匹配 QQ 表情标签 [笑哭] 等
            val qqFaceRegex = Regex("\\[[^\\]]+\\]")
            
            // 先移除原生表情，再移除方括号表情
            val withoutEmojis = input.replace(emojiRegex, "").replace(qqFaceRegex, "")
            // 去除链接中间及前后的多余空格
            withoutEmojis.replace(Regex("(?<=://|\\?|&|=|\\.)\\s+|\\s+(?=://|\\?|&|=|\\.)"), "").trim()
        } catch (e: Throwable) {
            "清洗出错: ${e::class.simpleName} - ${e.message}"
        }
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