package com.linkcleaner.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    var inputText by remember { mutableStateOf("") }
    var outputText by remember { mutableStateOf("") }
    val clipboardManager = LocalClipboardManager.current

    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Link Cleaner", style = MaterialTheme.typography.headlineMedium)

            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text("粘贴含表情的链接") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { /* 可选 */ }),
                maxLines = 5
            )

            Button(onClick = {
                outputText = LinkCleaner.clean(inputText)
            }) {
                Text("清洗链接")
            }

            OutlinedTextField(
                value = outputText,
                onValueChange = {},
                readOnly = true,
                label = { Text("清洗结果") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            Button(onClick = {
                if (outputText.isNotBlank()) {
                    clipboardManager.setText(AnnotatedString(outputText))
                }
            }) {
                Text("复制到剪贴板")
            }

            Divider()

            Text("短链补全", style = MaterialTheme.typography.titleSmall)

            var shortInput by remember { mutableStateOf("") }
            OutlinedTextField(
                value = shortInput,
                onValueChange = { shortInput = it },
                label = { Text("粘贴短链（如 s/xxxx）") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    val result = LinkCleaner.completeShortLink(shortInput, LinkCleaner.Platform.BAIDU)
                    if (result != null) outputText = result
                }) {
                    Text("补全百度")
                }
                Button(onClick = {
                    val result = LinkCleaner.completeShortLink(shortInput, LinkCleaner.Platform.QUARK)
                    if (result != null) outputText = result
                }) {
                    Text("补全夸克")
                }
            }
        }
    }
}