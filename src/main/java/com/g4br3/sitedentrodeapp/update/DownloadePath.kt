package com.g4br3.sitedentrodeapp.update

import android.util.Log
import java.io.File

class DownloadePath {
companion object {
   val TAG: String = "DownloadePath"
    fun listFiles(){
        // Aponta para a pasta de Downloads do usuário atual
        val downloadsPath = System.getProperty("user.home") + File.separator + "Downloads"
        val downloadsDir = File(downloadsPath)
        Log.d(TAG, downloadsPath)
        // Verifica se a pasta existe e lista os arquivos
        if (downloadsDir.exists() && downloadsDir.isDirectory) {
            val arquivos = downloadsDir.listFiles()

            arquivos?.forEach { arquivo ->
                Log.d(TAG, arquivo.absolutePath)
                if (arquivo.isFile) {
                    println("Arquivo: ${arquivo.name}")
                } else if (arquivo.isDirectory) {
                    println("Pasta: ${arquivo.name}")
                }
            }
        } else {
            Log.d(TAG,"Pasta Downloads não encontrada!")
        }
    }
}
}