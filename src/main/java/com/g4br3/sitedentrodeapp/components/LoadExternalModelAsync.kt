/*


// Uso com suspend function
class MyActivity {
    private lateinit var loader: LoadExternalModel

    fun setupLoader() {
        loader = LoadExternalModel(fileManager, uri)

        // Com callbacks
        loader.onLoadComplete = { content ->
            println("Carregamento completo: ${content.length} caracteres")
        }

        loader.onLoadError = { error ->
            println("Erro: ${error.message}")
        }
    }

    // Em uma corrotina
    suspend fun loadAndInject() {
        try {
            val content = loader.loadAsync()
            loader.injectAsync(webView)
            println("Injeção completa!")
        } catch (e: Exception) {
            println("Erro: ${e.message}")
        }
    }

    // Ou em background
    fun loadInBackground() {
        loader.loadInBackground()
    }
}

 */


package com.g4br3.sitedentrodeapp.components


import android.net.Uri
import android.util.Log
import android.webkit.WebView
import androidx.documentfile.provider.DocumentFile
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlin.coroutines.coroutineContext

class LoadExternalModelAsync(private val fileManager: FileManager,
                                     private val uri: Uri?){


        private val TAG: String = "loadModelsExterno"
        private val stringBuilder = StringBuilder()
        private val fileReadScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

        // Callback para notificar quando o carregamento estiver completo
        var onLoadComplete: ((String) -> Unit)? = null
        var onLoadError: ((Exception) -> Unit)? = null

        // Propriedade para verificar se o carregamento foi concluído
        var isLoadingComplete: Boolean = false
            private set

        // Método para iniciar o carregamento assíncrono
        suspend fun loadAsync(): String = withContext(Dispatchers.IO) {
            if (uri == null) {
                Log.d(TAG, "URI inválido: $uri")
                throw IllegalArgumentException("URI não pode ser nulo")
            }

            try {
                val docFile = DocumentFile.fromTreeUri(fileManager.context, uri)

                if (docFile != null && docFile.isDirectory) {
                    Log.d(TAG, "Pasta válida encontrada, listando arquivos...")
                    val arquivos = docFile.listFiles()
                    processFilesAsync(arquivos)
                    isLoadingComplete = true

                    val result = stringBuilder.toString()
                    withContext(Dispatchers.Main) {
                        onLoadComplete?.invoke(result)
                    }
                    return@withContext result
                } else {
                    val error = IllegalStateException("DocumentFile inválido ou não é um diretório")
                    withContext(Dispatchers.Main) {
                        onLoadError?.invoke(error)
                    }
                    throw error
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao ler arquivos", e)
                withContext(Dispatchers.Main) {
                    onLoadError?.invoke(e)
                }
                throw e
            }
        }

        // Método para carregar em background com callback
        fun loadInBackground() {
            fileReadScope.launch {
                try {
                    loadAsync()
                } catch (e: Exception) {
                    Log.e(TAG, "Erro no carregamento em background", e)
                }
            }
        }

        private suspend fun processFilesAsync(arquivos: Array<DocumentFile>) {

            withContext(Dispatchers.IO) {
          _processFilesAsync(arquivos)
        }}
    private suspend fun _processFilesAsync(arquivos: Array<DocumentFile>) {
        for (arquivo in arquivos) {
            if (!coroutineContext.isActive) break // Verifica se a corrotina foi cancelada

            val nomeArquivo = arquivo.name ?: "Nome desconhecido"
            Log.d(TAG, "Arquivo encontrado: $nomeArquivo")

            if (arquivo.isDirectory) {
                val docFile = DocumentFile.fromTreeUri(fileManager.context, arquivo.uri)
                Log.d(TAG, "Diretório encontrado: $nomeArquivo")
                if (docFile != null) {
                    processFilesAsync(docFile.listFiles()) // Recursão assíncrona
                }
                continue
            }

            if (arquivo.name == null) continue
            if (!arquivo.name.endsWith(".js")) continue
            if (!arquivo.isFile) continue

            // Leitura assíncrona do arquivo
            readFileAsync(arquivo)
        }
    }
        private suspend fun readFileAsync(arquivo: DocumentFile) = withContext(Dispatchers.IO) {
            try {
                fileManager.context.contentResolver.openInputStream(arquivo.uri)?.use { inputStream ->
                    BufferedReader(InputStreamReader(inputStream)).use { reader ->
                        var line: String?
                        while (reader.readLine().also { line = it } != null) {
                            if (!isActive) break // Verifica cancelamento
                            stringBuilder.append(line).append("\n")
                        }
                    }
                    stringBuilder.append("\n\n\n")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao ler arquivo: ${arquivo.name}", e)
            }
        }

        // Método assíncrono para injetar no WebView
        suspend fun injectAsync(webView: WebView) {
            if (!isLoadingComplete) {
                loadAsync() // Carrega se ainda não foi carregado
            }

            withContext(Dispatchers.Main) {
                Log.d(TAG, "Executando JavaScript:")
                webView.evaluateJavascript(stringBuilder.toString(), null)
            }
        }

        // Método para injetar com callback (não suspende)
        fun inject(webView: WebView, onComplete: (() -> Unit)? = null) {
            fileReadScope.launch {
                try {
                    injectAsync(webView)
                    onComplete?.invoke()
                } catch (e: Exception) {
                    Log.e(TAG, "Erro ao injetar JavaScript", e)
                }
            }
        }

        // Obter conteúdo atual (pode estar incompleto se ainda carregando)
        fun getCurrentContent(): String = stringBuilder.toString()

        // Limpar conteúdo
        fun clearContent() {
            stringBuilder.clear()
            isLoadingComplete = false
        }

        // Cancelar todas as operações
        fun cancelAllOperations() {
            fileReadScope.cancel()
        }

        // Verificar se há operações ativas
        fun isActive(): Boolean = fileReadScope.isActive
    }


