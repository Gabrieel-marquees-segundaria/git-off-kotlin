package com.g4br3.sitedentrodeapp.components


import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.WebView
import androidx.documentfile.provider.DocumentFile
import org.json.JSONArray
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class FileManager(context: Context) {
    var TAG: String = "FileManager"
    var CONTEXT: Context = context
    var isHtmlLoaded: Boolean = false
    lateinit var htmlContent: String
    /**
     * Carrega o conteúdo de um arquivo HTML selecionado.
     *
     * @param uri URI do arquivo HTML selecionado
     */
    private fun carregarArquivoHtml(uri: Uri) {
        println("📖 MainActivity: Iniciando carregamento do arquivo HTML")
        Log.d(TAG, "carregarArquivoHtml() iniciado para URI: $uri")

        try {
            CONTEXT.contentResolver.openInputStream(uri)?.use { inputStream ->
                val reader = BufferedReader(InputStreamReader(inputStream))
                val content = reader.readText()

                println("📄 MainActivity: Conteúdo HTML carregado (${content.length} caracteres)")
                Log.d(TAG, "Conteúdo HTML carregado com sucesso")

                htmlContent = content
                isHtmlLoaded = true

               // Toast.makeText(this, "Arquivo HTML carregado com sucesso", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            println("❌ MainActivity: Erro ao carregar arquivo HTML: ${e.message}")
            Log.e(TAG, "Erro ao carregar arquivo HTML", e)
          //  Toast.makeText(this, "Erro ao carregar arquivo HTML: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }


    /**
     * Lista os arquivos de uma pasta selecionada e envia para o JavaScript.
     *
     * @param uri URI da pasta selecionada
     * @param webView Instância do WebView para executar JavaScript
     */
     fun listarArquivosDaPasta(uri: Uri, webView: WebView) {
        println("📁 MainActivity: Iniciando listagem de arquivos para URI: $uri")
        Log.d(TAG, "listarArquivosDaPasta() iniciado para URI: $uri")

        val docFile = DocumentFile.fromTreeUri(CONTEXT, uri)
        val nomesArquivos = mutableListOf<String>()

        if (docFile != null && docFile.isDirectory) {
            println("📂 MainActivity: Pasta válida encontrada, listando arquivos...")
            val arquivos = docFile.listFiles()

            for (arquivo in arquivos) {
                val nomeArquivo = arquivo.name ?: "Nome desconhecido"
                nomesArquivos.add(nomeArquivo)
                println("📄 MainActivity: Arquivo encontrado: $nomeArquivo")
            }

            println("📊 MainActivity: Total de ${nomesArquivos.size} arquivos encontrados")
            Log.d(TAG, "Total de arquivos encontrados: ${nomesArquivos.size}")
        } else {
            println("❌ MainActivity: Pasta inválida ou não é um diretório")
            Log.w(TAG, "DocumentFile inválido ou não é um diretório")
        }

        val arquivosJson = JSONArray(nomesArquivos).toString()
        println("📤 MainActivity: Enviando lista de arquivos para JavaScript: $arquivosJson")
        webView.evaluateJavascript("receberArquivos('$arquivosJson')", null)

        Log.d(TAG, "listarArquivosDaPasta() concluído")
    }




    /**
     * Lista todos os arquivos da pasta selecionada (inclusive subpastas) e envia ao JavaScript.
     *
     * @param uri URI da pasta selecionada
     * @param webView Instância da WebView para comunicação com JavaScript
     */
     fun listarArquivosDasPastas(uri: Uri?, webView: WebView, clear: Boolean =false) {
        val nomeArquivoCache = "lista_arquivos.txt"
        val arquivoCache = File(CONTEXT.filesDir, nomeArquivoCache)

        if (arquivoCache.exists() and !clear) {
            // Se já existe, lê o conteúdo e retorna ao JavaScript
            val conteudo = arquivoCache.readText()
            webView.evaluateJavascript("receberArquivos('$conteudo')", null)
            return
        }

        val docFile = DocumentFile.fromTreeUri(CONTEXT, uri)
        val nomesArquivos = mutableListOf<String>()
        /**
         * Função recursiva que percorre arquivos e subpastas, construindo o caminho relativo.
         *
         * @param pasta DocumentFile da pasta atual
         * @param caminhoAtual Caminho relativo atual (usado para construir hierarquia)
         */
        fun percorrerPasta(pasta: DocumentFile?, caminhoAtual: String = "") {
            if (pasta != null && pasta.isDirectory) {
                for (arquivo in pasta.listFiles()) {
                    val nome = arquivo.name ?: continue
                    val caminhoRelativo =
                        if (caminhoAtual.isEmpty()) nome else "$caminhoAtual/$nome"

                    if (arquivo.isDirectory) {
                        percorrerPasta(arquivo, caminhoRelativo)
                    } else if (arquivo.isFile) {
                        nomesArquivos.add(caminhoRelativo)
                    }
                }
            }
        }

        percorrerPasta(docFile)

        val arquivosJson = JSONArray(nomesArquivos).toString()

        // Salva no arquivo local
        arquivoCache.writeText(arquivosJson)

        // Envia para o JavaScript
        webView.evaluateJavascript("receberArquivos('$arquivosJson')", null)
    }

}