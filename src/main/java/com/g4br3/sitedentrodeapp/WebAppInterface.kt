package com.g4br3.sitedentrodeapp

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
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
    private val abrirPastaCallback: () -> Unit,
    private val abrirArquivoCallback: ((String) -> Unit)?,
     val listarArquivos: (() -> Unit)?




) {
    /** URI da pasta selecionada, atualizada pela MainActivity */
    var selectedFolderUri: Uri? = null
    var viewPageLoaded = false
    private val TAG = "WebAppInterface"

    init {
        println("🔧 WebAppInterface: Inicializando interface JavaScript")
        Log.d(TAG, "WebAppInterface inicializada com sucesso")
    }
//    fun currentSite(name: FileName = webFiles.webListName, onLoaded: (() -> Unit)? = null) {
//        viewPageLoaded = false
//        webView.post {
//            //webView.loadUrl("file:///android_asset/$name.html")
//            webView.loadUrl(webFiles.getName(name))
//            webView.postDelayed({
//                val js = """
//                    console.log("JS injetado com delay");
//                    document.body.style.backgroundColor = 'red';
//                """.trimIndent()
//                webView.evaluateJavascript(js, null)
//
//                viewPageLoaded = true
//                onLoaded?.invoke()
//            }, 400)
//
//        }
//    }

    @JavascriptInterface
    fun abrirArquivo(name: String) {
        //abrirArquivoCallback(name)
    }
    @JavascriptInterface
    fun filesString() {
      //  webView.evaluateJavascript("receberListaDeNomesHTML([${webFiles.webListName.value},${webFiles.webHtmlViewName.value}])", null)
    }
    @JavascriptInterface
    fun set_home(file: String){
        println(file)
    }


//    @JavascriptInterface
//    fun voltarParaLista() {
//        currentSite(webFiles.webListName){
//            listarArquivos()
//        }
//    }
    /** Escapa string para ser segura dentro de JavaScript inline */
    private fun escaparParaJavascript(codigo: String): String {
        return codigo
            .replace("\\", "\\\\")
            .replace("'", "\\'")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "")
    }
    /** Injeta código JavaScript diretamente na WebView, com escape automático */
    @JavascriptInterface
    fun injetarJavascript(codigo: String) {
//        val codigoSeguro = escaparParaJavascript(codigo)
//        webView.post {
//            webView.evaluateJavascript(codigoSeguro, null)
//        }
    }
    @JavascriptInterface
    fun listarArquivos(){
        listarArquivos?.invoke()
    }
    /** Função chamada do JavaScript para solicitar um nome. */
    @JavascriptInterface
    fun pegarNome() {
        Toast.makeText(context, "Android recebeu pedido de nome", Toast.LENGTH_SHORT).show()
        val nome = "Gabriel"
        webView.post {
            webView.evaluateJavascript("receberNome('$nome')", null)
        }
    }

    /**
     * Método JavaScript para abrir o seletor de pasta.
     *
     * Este método é chamado do JavaScript e aciona o callback
     * para abrir o seletor de pasta do Android.
     */
    @JavascriptInterface
    fun abrirPasta() {
        println("📁 WebAppInterface: Solicitação para abrir pasta recebida")
        Log.d(TAG, "Método abrirPasta() chamado")
        abrirPastaCallback()
    }

    /** Função chamada do JavaScript para ler o conteúdo de um arquivo da pasta. */
    @JavascriptInterface
    fun lerArquivo(caminhoRelativo: String) {
        //currentSite(webFiles.webHtmlViewName)
//
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
                    if (viewPageLoaded) {
                        webView.evaluateJavascript("mostrarConteudo('$conteudoEscapado')", null)
                    } else {
                        // Tenta novamente após atraso
                        Handler(Looper.getMainLooper()).postDelayed({
                            webView.evaluateJavascript("mostrarConteudo('$conteudoEscapado')", null)
                        }, 300)
                    }
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
