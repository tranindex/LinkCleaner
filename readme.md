# LinkCleaner 🧹

> 一个基于 Jetpack Compose 开发的轻量级安卓链接清理与防屏蔽工具。

[![Stars](https://img.shields.io/github/stars/tranindex/LinkCleaner?style=social)](https://github.com/tranindex/LinkCleaner/stargazers)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Bilibili](https://img.shields.io/badge/Bilibili-关注-blue?logo=bilibili)](https://space.bilibili.com/383671900)

---

## 📖 简介

在B站、贴吧等日常分享中，由于网盘链接容易被识别，常常会被插入各种表情符号或删除前缀等，要获得完整链接较为繁琐。
LinkCleaner 是一款专为安卓打造的轻量工具，旨在帮助用户**快速清理和整理链接**，并提供**链接防屏蔽修饰**能力，带来简洁现代的 UI 体验。

## ✨ 功能特性

- 🚀 基于 Jetpack Compose 构建，流畅的现代 UI 体验
- 🎨 支持 Material 3 动态主题，界面随系统个性变色
- 🧹 **链接修复**：快速清洗链接中的冗余字符与表情，支持短链补全
- 🛡️ **链接防屏蔽**：在链接中插入表情或自定义字符，支持多位置随机插入，躲避平台屏蔽
- 🔢 可调节插入次数（1~10 次），支持手动二次编辑输出结果
- 📱 轻量无广告，完全本地运行，保护隐私
- 🌐 支持百度网盘、夸克网盘等短链智能补全

## 📱 截图

| 链接修复 | 链接防屏蔽 |
| -------- | ---------- |
| ![image-20260829012131101](D:\githubproject\LinkCleaner\readme.assets\image-20260829012131101.png) | ![image-20260829012140948](D:\githubproject\LinkCleaner\readme.assets\image-20260829012140948.png) |

## 🛠 技术栈

- Kotlin
- Jetpack Compose (Material 3)
- Kotlin Multiplatform
- Android Studio

## ⬇️ 下载安装

前往 [Releases](https://github.com/tranindex/LinkCleaner/releases) 页面下载最新版本的 APK。

1. 下载`LinkCleaner-signed.apk`
2. 在手机上允许「安装未知来源应用」
3. 点击 APK 完成安装

## 🚀 使用指南

### 链接修复

1. 粘贴含有表情或冗余字符的链接
2. 点击「清洗链接」按钮
3. 结果自动生成，点击「复制到剪切板」即可使用
4. 支持百度 / 夸克短链一键补全

### 链接防屏蔽

1. 输入需要修饰的网址（当作普通文本处理，不要求保留 http 结构）
2. 选择一个表情，或输入自定义文本 / 符号
   - ⚠️ 不建议插入纯字母 / 纯数字等内容，以免清洗时无法识别
3. 调节「插入次数」滑块，控制随机插入的数量
4. 点击「生成防屏蔽链接」，结果可在输出框中手动微调
5. 复制到剪切板，发送给你想发送的人


## 🗺️ Roadmap

- [ ] 批量链接处理
- [ ] 规则模板保存与复用
- [ ] Windows 桌面版（Compose Desktop）
- [ ] 更多短链平台支持

## 🤝 贡献

欢迎各种形式的贡献！

- 🐛 提交 Issue 报告 Bug 或建议新功能
- 🔧 提交 Pull Request 参与代码贡献
- ⭐ **如果这款小工具帮到了你，不妨点个 Star 支持一下**，你的鼓励是我持续迭代的动力！
- 📺 **关注我的 B 站空间**：[行云烟客 的 B 站空间](https://space.bilibili.com/383671900)

## 📄 开源协议

本项目基于 [MIT License](LICENSE) 开源。

---

Made with ❤️ and Compose
