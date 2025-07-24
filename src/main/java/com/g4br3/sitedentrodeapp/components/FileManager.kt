package com.g4br3.sitedentrodeapp.components

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.webkit.WebView
import androidx.annotation.RequiresApi
import androidx.documentfile.provider.DocumentFile
import org.json.JSONArray
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import kotlin.collections.MutableMap

class FileManager(context: Context) {
    companion object {
        private const val TAG = "FileManager"
        private const val CACHE_FILE_NAME = "lista_arquivos.txt"
    }

     val context: Context = context
    var isLoaded: Boolean = false
    var Content: String = ""
        private set

    /**
     * Carrega o conteúdo de um arquivo HTML selecionado.
     *
     * @param uri URI do arquivo HTML selecionado
     * @return true se carregou com sucesso, false caso contrário
     */
    fun carregarArquivo(uri: Uri, type:String="txt"): Boolean {
        Log.d(TAG, "carregarArquivoHtml() iniciado para URI: $uri")

        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val reader = BufferedReader(InputStreamReader(inputStream))
                val content = reader.readText()

                Log.d(TAG, "Conteúdo $type carregado com sucesso (${content.length} caracteres)")

                Content = content
                isLoaded = true
                true
            } ?: false
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao carregar arquivo ", e)
            false
        }
    }

    /**
     * Lista os arquivos de uma pasta selecionada e envia para o JavaScript.
     *
     * @param uri URI da pasta selecionada
     * @param webView Instância do WebView para executar JavaScript
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun listarArquivosDaPasta(uri: Uri, webView: WebView) {
        Log.d(TAG, "listarArquivosDaPasta() iniciado para URI: $uri")

        try {
            val docFile = DocumentFile.fromTreeUri(context, uri)
            val nomesArquivos = mutableListOf<String>()

            if (docFile != null && docFile.isDirectory) {
                Log.d(TAG, "Pasta válida encontrada, listando arquivos...")
                val arquivos = docFile.listFiles()

                for (arquivo in arquivos) {
                    val nomeArquivo = arquivo.name ?: "Nome desconhecido"
                    nomesArquivos.add(nomeArquivo)
                    Log.d(TAG, "Arquivo encontrado: $nomeArquivo")
                }

                Log.d(TAG, "Total de arquivos encontrados: ${nomesArquivos.size}")
            } else {
                Log.w(TAG, "DocumentFile inválido ou não é um diretório")
            }

            val arquivosJson = JSONArray(nomesArquivos).toString()
            enviarArquivosParaJavaScript(webView, arquivosJson)

        } catch (e: Exception) {
            Log.e(TAG, "Erro ao listar arquivos da pasta", e)
            enviarArquivosParaJavaScript(webView, "[]")
        }
    }

    /**
     * Função recursiva que percorre arquivos e subpastas, construindo o caminho relativo.
     *
     * @param pasta DocumentFile da pasta atual
     * @param caminhoAtual Caminho relativo atual (usado para construir hierarquia)

     */
    private fun percorrerPasta(
        pasta: DocumentFile?,
        caminhoAtual: String = "",

        callback: (DocumentFile?, String?) -> Unit
    ) {
        if (pasta == null || !pasta.isDirectory) return

            callback(null,null)
            for (arquivo in pasta.listFiles()) {
                val nome = arquivo.name ?: continue
                val caminhoRelativo = if (caminhoAtual.isEmpty()) nome else "$caminhoAtual/$nome"
                callback(arquivo, arquivo.type)
                if (arquivo.isDirectory) {
                    percorrerPasta(arquivo, caminhoRelativo, callback)
                }
        }
    }
    fun toFileInfo(arquivo: DocumentFile, caminhoRelativo: String, type: String): MutableMap<String, String> {
      return  mutableMapOf(
            "name" to arquivo.name.toString(),
            "uri" to arquivo.uri.toString(),
            "relativePath" to caminhoRelativo,
            "type" to type
      )
    }
    private fun mutableMapOf(pairs: Pair<String, String>) {}

    /**
     * Lista todos os arquivos da pasta selecionada (inclusive subpastas) e envia ao JavaScript.
     *
     * @param uri URI da pasta selecionada
     * @param webView Instância da WebView para comunicação com JavaScript
     * @param forceRefresh Se true, força a atualização do cache
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun listarArquivosDasPastas(
        uri: Uri?,
        webView: WebView,
        forceRefresh: Boolean = false,
        callback: (file: DocumentFile, type: String) -> Unit
    ) {
        if (uri == null) {
            Log.w(TAG, "URI é null")
            enviarArquivosParaJavaScript(webView, "[]")
            return
        }

        val arquivoCache = File(context.filesDir, CACHE_FILE_NAME)

        try {
            // Verifica se deve usar cache
            if (arquivoCache.exists() && !forceRefresh) {
                val conteudo = arquivoCache.readText()
                enviarArquivosParaJavaScript(webView, conteudo)
                return
            }

            // Gera nova lista de arquivos
            val docFile = DocumentFile.fromTreeUri(context, uri)
            val nomesArquivos = mutableListOf<String>()

            percorrerPasta(docFile, "", callback as (DocumentFile?, String?) -> Unit)

            val arquivosJson = JSONArray(nomesArquivos).toString()

            // Salva no cache
            arquivoCache.writeText(arquivosJson)

            // Envia para o JavaScript
            enviarArquivosParaJavaScript(webView, arquivosJson)

        } catch (e: Exception) {
            Log.e(TAG, "Erro ao listar arquivos das pastas", e)
            enviarArquivosParaJavaScript(webView, "[]")
        }
    }

    /**
     * Envia a lista de arquivos para o JavaScript de forma segura.
     *
     * @param webView Instância do WebView
     * @param arquivosJson JSON com a lista de arquivos
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun enviarArquivosParaJavaScript(webView: WebView, arquivosJson: String) {
        // Faz escape das aspas para evitar problemas no JavaScript
        val jsonEscapado = arquivosJson.replace("'", "\\'")
    webView.post {
        webView.evaluateJavascript("receberArquivos('$jsonEscapado')", null)

    }
          }

    /**
     * Limpa o cache de arquivos.
     */
    fun limparCache() {
        val arquivoCache = File(context.filesDir, CACHE_FILE_NAME)
        if (arquivoCache.exists()) {
            arquivoCache.delete()
            Log.d(TAG, "Cache limpo com sucesso")
        }
    }
}