package com.linkcleaner.app

import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import java.io.OutputStream

class PlatformActionsImpl : PlatformActions {
    override fun saveImageToGallery(imageName: String) {
        val context = AppContextHolder.context ?: return
        try {
            val inputStream = context.assets.open("wechat_qr.png")
            val bitmap = BitmapFactory.decodeStream(inputStream)

            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "$imageName.png")
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            val uri: Uri? = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            uri?.let {
                val outputStream: OutputStream? = context.contentResolver.openOutputStream(it)
                outputStream?.use { out ->
                    bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, out)
                }
            }
            Toast.makeText(context, "图片已保存到相册", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "保存失败: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun openWeChat() {
        val context = AppContextHolder.context ?: return
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setClassName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(context, intent, null)
        } catch (e: Exception) {
            Toast.makeText(context, "未安装微信", Toast.LENGTH_SHORT).show()
        }
    }
}