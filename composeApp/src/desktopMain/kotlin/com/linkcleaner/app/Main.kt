package com.linkcleaner.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlin.system.exitProcess

fun main() = application {
    Window(
        onCloseRequest = { exitProcess(0) },
        title = "Link Cleaner"
    ) {
        App()
    }
}