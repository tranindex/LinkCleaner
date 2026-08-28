package com.linkcleaner.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 注入 Context 和平台动作实现
        AppContextHolder.context = this
        PlatformActionProvider.actions = PlatformActionsImpl()  // 注入 Android 实现
        setContent {
            App()
        }
    }
}