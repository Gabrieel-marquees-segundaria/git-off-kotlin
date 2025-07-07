package com.g4br3.sitedentrodeapp

import android.content.Context
import android.net.Uri
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile

/**
 * Interface entre o JavaScript da WebView e o código nativo Android.
 * Permite executar funções Kotlin diretamente do JavaScript.
 *
 * @param context Contexto da Activity.
 * @param webView Referência para a WebView que executa os comandos.
 * @param abrirPastaCallback Callback para abrir o seletor de pastas.
 */
class WebAppInterface(
    private val context: Context,
    private val webView: WebView,
    private val abrirPastaCallback: () -> Unit
) {
    /** URI da pasta selecionada, atualizada pela MainActivity */
    var selectedFolderUri: Uri? = null

    /** Função chamada do JavaScript para solicitar um nome. */
    @JavascriptInterface
    fun pegarNome() {
        Toast.makeText(context, "Android recebeu pedido de nome", Toast.LENGTH_SHORT).show()
        val nome = "Gabriel"
        webView.post {
            webView.evaluateJavascript("receberNome('$nome')", null)
        }
    }

    /** Função chamada do JavaScript para abrir o seletor de pastas. */
    @JavascriptInterface
    fun abrirPasta() {
        abrirPastaCallback()
    }

    /** Função chamada do JavaScript para ler o conteúdo de um arquivo da pasta. */
    @JavascriptInterface
    fun lerArquivo(caminhoRelativo: String) {
        selectedFolderUri?.let { baseUri ->
            val arquivo = localizarArquivoPorCaminho(baseUri, caminhoRelativo)
            if (arquivo != null && arquivo.isFile) {
                val inputStream = context.contentResolver.openInputStream(arquivo.uri)
                val conteudo = inputStream?.bufferedReader().use { it?.readText() } ?: "Erro ao ler arquivo"

                val conteudoEscapado = conteudo
                    .replace("\\", "\\\\")
                    .replace("'", "\\'")
                    .replace("\n", "\\n")
                    .replace("\r", "")

                webView.post {
                    webView.evaluateJavascript("mostrarConteudo('$conteudoEscapado')", null)
                }
            } else {
                webView.post {
                    webView.evaluateJavascript("mostrarConteudo('Arquivo não encontrado')", null)
                }
            }
        }
    }

    /** Percorre recursivamente a árvore DocumentFile para localizar um arquivo por caminho relativo. */
    private fun localizarArquivoPorCaminho(baseUri: Uri, caminhoRelativo: String): DocumentFile? {
        val partes = caminhoRelativo.split("/")
        var atual: DocumentFile? = DocumentFile.fromTreeUri(context, baseUri)

        for (parte in partes) {
            atual = atual?.listFiles()?.firstOrNull { it.name == parte }
            if (atual == null) break
        }
        return atual
    }
}
