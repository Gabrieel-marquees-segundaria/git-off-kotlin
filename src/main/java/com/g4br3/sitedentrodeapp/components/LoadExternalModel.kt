package com.g4br3.sitedentrodeapp.components

import android.net.Uri
import android.util.Log
import android.webkit.WebView
import androidx.documentfile.provider.DocumentFile
import java.io.BufferedReader
import java.io.InputStreamReader


class LoadExternalModel(fileManager: FileManager, uri: Uri?){
    private var TAG: String = "loadModelsExterno"
    val stringBuilder = StringBuilder()

    init {
        if(uri==null) {
            Log.d(TAG, "uri invalido type: ${uri}...")
        }
        try {
            val docFile = DocumentFile.fromTreeUri(fileManager.context, uri)

            if (docFile != null && docFile.isDirectory) {
                Log.d(TAG, "Pasta válida encontrada, listando arquivos...")
                val arquivos = docFile.listFiles()

                for (arquivo in arquivos) {
                    val nomeArquivo = arquivo.name ?: "Nome desconhecido"
                    Log.d(TAG, "Arquivo encontrado: $nomeArquivo")
                   println( arquivo.uri)

                    fileManager.context.contentResolver.openInputStream(arquivo.uri).use{inputStrinm ->
                        BufferedReader(InputStreamReader(inputStrinm)).use{reader->
                            var line: String?
                            while (reader.readLine().also { line = it } != null) {
                                stringBuilder.append(line).append("\n")
                            }
                        }

                        stringBuilder.append("\n\n\n")
                    }

                }
//
            } else {
                Log.w(TAG, "DocumentFile inválido ou não é um diretório")
            }

        }catch  (e: Exception) {
            Log.e(TAG, "Erro ao ler arquivos de ", e)

        }

    }
    fun inject(webView: WebView){
        webView.post {
                 Log.d(TAG, "Executando JavaScript:")
                   webView.evaluateJavascript(stringBuilder.toString(), null)
             }
    }
}