package com.g4br3.sitedentrodeapp.components

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.WebView
import androidx.documentfile.provider.DocumentFile
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

class LoadExternalModel(fileManager: FileManager, uri: Uri?){
    private var TAG: String = "loadModelsExterno"
    var nfileManager: FileManager = fileManager
    var nuri = uri
    val stringBuilder = StringBuilder()
    private var filereadScope: CoroutineScope = CoroutineScope(Dispatchers.Main.immediate+ Job())

    init {
        if(uri==null) {
            Log.d(TAG, "uri invalido type: ${uri}...")
        }
        try {
            val docFile = DocumentFile.fromTreeUri(nfileManager.context, nuri)

            if (docFile != null && docFile.isDirectory) {
                Log.d(TAG, "Pasta válida encontrada, listando arquivos...")
                val arquivos = docFile.listFiles()
                    loop(arquivos)

//                for (arquivo in arquivos) {
//                    val nomeArquivo = arquivo.name ?: "Nome desconhecido"
//                    Log.d(TAG, "Arquivo encontrado: $nomeArquivo")
//                   println( arquivo.uri)
//                    if ( arquivo.name == null) continue
//                    if (!arquivo.name.endsWith(".js")) continue
//                    if (!arquivo.isFile()) continue
//                    fileManager.context.contentResolver.openInputStream(arquivo.uri).use{inputStrinm ->
//                        BufferedReader(InputStreamReader(inputStrinm)).use{reader->
//                            var line: String?
//                            while (reader.readLine().also { line = it } != null) {
//                                stringBuilder.append(line).append("\n")
//                            }
//                        }
//
//                        stringBuilder.append("\n\n\n")
//                    }
//
//                }
//
            } else {
                Log.w(TAG, "DocumentFile inválido ou não é um diretório")
            }

        }catch  (e: Exception) {
            Log.e(TAG, "Erro ao ler arquivos de ", e)

        }

    }
    fun loop(arquivos: Array<DocumentFile>){
        for (arquivo in arquivos) {
            val nomeArquivo = arquivo.name ?: "Nome desconhecido"
            Log.d(TAG, "Arquivo encontrado: $nomeArquivo")
            println( arquivo.uri)
            if (arquivo.isDirectory) {
                val docFile = DocumentFile.fromTreeUri(nfileManager.context, arquivo.uri)
                Log.d(TAG, "diretorio encontrado: $nomeArquivo")
                if (docFile != null) {
                    loop(docFile.listFiles())
                }
                continue
            }
            if ( arquivo.name == null) continue

            if (!arquivo.name.endsWith(".js")) continue

            if (!arquivo.isFile) continue

            nfileManager.context.contentResolver.openInputStream(arquivo.uri).use{inputStrinm ->
                BufferedReader(InputStreamReader(inputStrinm)).use{reader->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        stringBuilder.append(line).append("\n")
                    }
                }

                stringBuilder.append("\n\n\n")
            }
    }}
   private fun loopAsyncSetup(arquivos: Array<DocumentFile>){
        var context: Context =nfileManager.context
       filereadScope.launch{
            try {
                loopAsync(arquivos)
            }catch (err: Exception) {
                println(err)
            }
        }
    }
    private suspend fun loopAsync(arquivos: Array<DocumentFile>) = withContext(
        Dispatchers.IO) {
            for (arquivo in arquivos) {
                val nomeArquivo = arquivo.name ?: "Nome desconhecido"
                Log.d(TAG, "Arquivo encontrado: $nomeArquivo")
                println( arquivo.uri)
                if (arquivo.isDirectory) {
                    val docFile = DocumentFile.fromTreeUri(nfileManager.context, arquivo.uri)
                    Log.d(TAG, "diretorio encontrado: $nomeArquivo")
                    if (docFile != null) {
                        loop(docFile.listFiles())
                    }
                    continue
                }
                if ( arquivo.name == null) continue

                if (!arquivo.name.endsWith(".js")) continue

                if (!arquivo.isFile) continue

                nfileManager.context.contentResolver.openInputStream(arquivo.uri).use{inputStrinm ->
                    BufferedReader(InputStreamReader(inputStrinm)).use{reader->
                        var line: String?
                        while (reader.readLine().also { line = it } != null) {
                            stringBuilder.append(line).append("\n")
                        }
                    }

                    stringBuilder.append("\n\n\n")
                }
            }}

    fun cancelAllReads(){
        filereadScope.cancel()
    }

    fun inject(webView: WebView){
        webView.post {
                 Log.d(TAG, "Executando JavaScript:")
                   webView.evaluateJavascript(stringBuilder.toString(), null)
             }
    }
}