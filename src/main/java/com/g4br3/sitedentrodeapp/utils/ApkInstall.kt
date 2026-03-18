package com.g4br3.sitedentrodeapp.utils




import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.os.Build

import androidx.core.content.FileProvider
import java.io.File

fun installAPK(context: Context, filePath: String) {
    val file = File(filePath)
    if (file.exists()) {
        val intent = Intent(Intent.ACTION_VIEW)

        // No Android 7.0+, precisamos usar o FileProvider para obter a URI
        val apkUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } else {
            Uri.fromFile(file)
        }

        intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(intent)
    }
}


class ApkInstall {
}

// Overload that accepts a Uri (for DownloadManager localUri)
fun installAPK(context: Context, uri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(uri, "application/vnd.android.package-archive")
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Log.e("ApkInstall", "Erro ao abrir instalador via Uri", e)
    }
}
