package com.g4br3.sitedentrodeapp.file

import android.util.Log
import java.io.File
import java.nio.charset.Charset


class TXTFileManager : IFileContentManager() {
    companion object {
        val   extd = "txt"
    }
    fun getName(filePath: String): String {
        return filePath.split("/")[filePath.split("/").size - 1]
    }

    fun getFile(filePath: String, charset: Charset= Charsets.UTF_8): String {
        return  File(filePath).readText(charset)
    }

    fun formateContent(file: String): List<String> {

        var conteudo = getFile(file) ?: "not found"
        var conteudoScapado = conteudo
            .replace("\\", "\\\\")
            .replace("'", "\\'")
            .replace("\n", "\\n")
            .replace("\r", "")
        Log.d("WebView", "file conteudo: $conteudoScapado")
        val name  = getName(file)
        return listOf(conteudoScapado , name)
    }
}


