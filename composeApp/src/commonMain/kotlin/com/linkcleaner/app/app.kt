package com.linkcleaner.app

// ========== 新增导入 ==========
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.style.TextAlign
// ==============================

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider

// 原有导入保持不变
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import com.linkcleaner.app.R

// 原有接口和 Provider 保持不变
interface PlatformActions {
    fun saveImageToGallery(imageName: String)
    fun openWeChat()
}

object PlatformActionProvider {
    lateinit var actions: PlatformActions
}

// ========== 修改：主界面重构，添加 TabRow 和 Scaffold ==========
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("链接文本修复", "链接文本防屏蔽")

    // 赞赏弹窗状态提升到顶层，实现全局显示
    var showDonateDialog by remember { mutableStateOf(false) }

    MaterialTheme {
        // 全局赞赏弹窗（保持不变）
        if (showDonateDialog) {
            AlertDialog(
                onDismissRequest = { showDonateDialog = false },
                title = { Text("鼓励作者") },
                text = {
                    Image(
                        painter = painterResource(id = R.drawable.wechat_qr),
                        contentDescription = "微信赞赏码",
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextButton(onClick = {
                            PlatformActionProvider.actions.saveImageToGallery("wechat_qr")
                        }) { Text("保存图片") }
                        TextButton(onClick = {
                            PlatformActionProvider.actions.openWeChat()
                        }) { Text("跳转至微信") }
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDonateDialog = false }) { Text("关闭") }
                }
            )
        }

        // 新增：Scaffold 包含 TopBar 和 BottomBar
        Scaffold(
            topBar = {
                TabRow(selectedTabIndex = selectedTab) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }
            },
            bottomBar = {
                // 新增：全局底部“鼓励作者”入口
                Text(
                    text = "鼓励作者",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDonateDialog = true }
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        ) { paddingValues ->
            // 根据选项卡切换内容
            when (selectedTab) {
                0 -> LinkFixTab(modifier = Modifier.padding(paddingValues))
                1 -> AntiBlockTab(modifier = Modifier.padding(paddingValues))
            }
        }
    }
}

// ========== 新增：链接修复页面（提取自原 App 内容） ==========
@Composable
fun LinkFixTab(modifier: Modifier = Modifier) {
    var inputText by remember { mutableStateOf("") }
    var shortInput by remember { mutableStateOf("") }
    var outputText by remember { mutableStateOf("") }
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Link Fix", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("粘贴含表情的链接") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            minLines = 2,
            maxLines = 4
        )
        Button(onClick = { outputText = LinkCleaner.clean(inputText) }) {
            Text("清洗链接")
        }

        HorizontalDivider(thickness = 1.dp)
        Text("短链补全", style = MaterialTheme.typography.titleSmall)
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
            }) { Text("补全百度") }
            Button(onClick = {
                val result = LinkCleaner.completeShortLink(shortInput, LinkCleaner.Platform.QUARK)
                if (result != null) outputText = result
            }) { Text("补全夸克") }
        }

        HorizontalDivider(thickness = 1.dp)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = outputText,
            onValueChange = {},
            readOnly = true,
            label = { Text("输出结果") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            maxLines = 3
        )
        Button(onClick = {
            if (outputText.isNotBlank()) clipboardManager.setText(AnnotatedString(outputText))
        }) { Text("复制到剪切板") }

        // 底部留空，防止被 BottomBar 遮挡
        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ========== 链接防屏蔽页面 ==========
@Composable
fun AntiBlockTab(modifier: Modifier = Modifier) {
    var url by remember { mutableStateOf("") }
    var customText by remember { mutableStateOf("") }
    var selectedChar by remember { mutableStateOf("😊") }
    var insertCount by remember { mutableIntStateOf(1) }
    var result by remember { mutableStateOf("") }
    var showHintDialog by remember { mutableStateOf(false) }

    val emojis = listOf("😊", "👍", "🔥", "🎉", "❤️", "⭐", "💡", "✨")
    val clipboardManager = LocalClipboardManager.current

    // 提示弹窗
    if (showHintDialog) {
        AlertDialog(
            onDismissRequest = { showHintDialog = false },
            title = { Text("提示") },
            text = { Text("不建议插入纯字母/纯数字等内容，以免清洗时无法识别。建议使用表情或特殊符号喵。") },
            confirmButton = {
                TextButton(onClick = { showHintDialog = false }) { Text("知道了") }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Link Masker", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = url,
            onValueChange = { url = it },
            label = { Text("输入需要修饰的网址或文本") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )

        Text("选择插入的表情：", style = MaterialTheme.typography.titleSmall)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(emojis) { emoji ->
                FilterChip(
                    selected = selectedChar == emoji,
                    onClick = { selectedChar = emoji },
                    label = { Text(emoji) }
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = customText,
                onValueChange = { customText = it },
                label = { Text("或输入自定义文本/符号") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            IconButton(onClick = { showHintDialog = true }) {
                Icon(Icons.Default.Info, contentDescription = "提示")
            }
        }

        // 插入次数选择
        Text("插入次数：$insertCount", style = MaterialTheme.typography.bodySmall)
        Slider(
            value = insertCount.toFloat(),
            onValueChange = { insertCount = it.toInt() },
            valueRange = 1f..10f,
            steps = 8,
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            val charToInsert = if (customText.isNotBlank()) customText else selectedChar
            result = insertEmojiIntoUrl(url, charToInsert, count = insertCount)
        }) {
            Text("生成防屏蔽链接")
        }

        OutlinedTextField(
            value = result,
            onValueChange = { result = it },   // 可编辑
            label = { Text("输出结果（可手动编辑）") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )

        Button(onClick = {
            if (result.isNotBlank()) clipboardManager.setText(AnnotatedString(result))
        }) {
            Text("复制到剪切板")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ========== 新增：插入表情的工具函数（建议后续移到 LinkCleaner 对象中） ==========
/**
 * 在字符串中随机选择 count 个位置插入 char（允许破坏协议头，达到防屏蔽目的）
 * @param text 原始文本（可以是任意字符串）
 * @param char 要插入的字符
 * @param count 插入次数（默认1次，最大不超过 text.length）
 * @return 插入后的字符串
 */
fun insertEmojiIntoUrl(text: String, char: String, count: Int = 1): String {
    if (text.isBlank() || char.isBlank()) return text
    val length = text.length
    val actualCount = count.coerceIn(1, length)
    // 生成 actualCount 个不重复的随机位置（0..length）
    val positions = (0 until length).shuffled().take(actualCount).sorted()
    val sb = StringBuilder(text)
    // 从后往前插入，避免索引偏移
    for (pos in positions.reversed()) {
        sb.insert(pos, char)
    }
    return sb.toString()
}