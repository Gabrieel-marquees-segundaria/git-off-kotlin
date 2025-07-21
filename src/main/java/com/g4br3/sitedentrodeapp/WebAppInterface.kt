package com.g4br3.sitedentrodeapp

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.documentfile.provider.DocumentFile
import com.g4br3.sitedentrodeapp.components.Info

import com.g4br3.sitedentrodeapp.components.UriList
import org.json.JSONObject

var uriList: UriList = UriList()
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
    private val abrirPasrtasCallback: (name: String, on_selected: (Uri) -> Unit)-> Unit,
    val listarArquivos: (() -> Unit)?
) {
    private var info = Info(context)
    /** URI da pasta selecionada, atualizada pela MainActivity */
    var selectedFolderUri: Uri? = null
        set(value) {
            field = value
            Log.d(TAG, "selectedFolderUri atualizada: $value")
        }

    var viewPageLoaded = false
        set(value) {
            field = value
            Log.d(TAG, "viewPageLoaded alterado para: $value")
        }

    private val TAG = "WebAppInterface"

    init {
        Log.i(TAG, "🔧 WebAppInterface: Inicializando interface JavaScript")
        Log.d(TAG, "WebAppInterface inicializada com sucesso")
        Log.d(TAG, "Context: ${context.javaClass.simpleName}")
        Log.d(TAG, "WebView: ${webView.javaClass.simpleName}")
        Log.d(TAG, "AbrirPastaCallback: ${if (abrirPastaCallback != null) "Configurado" else "Não configurado"}")
        Log.d(TAG, "AbrirArquivoCallback: ${if (abrirPasrtasCallback != null) "Configurado" else "Não configurado"}")
        Log.d(TAG, "ListarArquivos: ${if (listarArquivos != null) "Configurado" else "Não configurado"}")
    }

    @JavascriptInterface
    fun abrirArquivo(name: String) {
        Log.d(TAG, "📄 abrirArquivo chamado com parâmetro: '$name'")
        // TODO: Implementar lógica
        Log.w(TAG, "abrirArquivo não implementado - callback comentado")
        //abrirArquivoCallback(name)
    }

    @JavascriptInterface
    fun filesString() {
        Log.d(TAG, "📋 filesString() chamado")
        // TODO: Implementar lógica
        Log.w(TAG, "filesString não implementado - código comentado")
        //webView.evaluateJavascript("receberListaDeNomesHTML([${webFiles.webListName.value},${webFiles.webHtmlViewName.value}])", null)
    }

    @JavascriptInterface
    fun set_home(file: String){
        Log.d(TAG, "🏠 set_home chamado com parâmetro: '$file'")
        println(file)
    }

    /** Escapa string para ser segura dentro de JavaScript inline */
    private fun escaparParaJavascript(codigo: String): String {
        Log.v(TAG, "🔄 Escapando código JavaScript (${codigo.length} caracteres)")
        val resultado = codigo
            .replace("\\", "\\\\")
            .replace("'", "\\'")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "")
        Log.v(TAG, "✅ Código JavaScript escapado com sucesso")
        return resultado
    }

    /** Injeta código JavaScript diretamente na WebView, com escape automático */
    @JavascriptInterface
    fun injetarJavascript(codigo: String) {
        Log.d(TAG, "💉 injetarJavascript chamado com código de ${codigo.length} caracteres")
        Log.v(TAG, "Código recebido: $codigo")
        // TODO: Implementar lógica
        Log.w(TAG, "injetarJavascript não implementado - código comentado")
//        val codigoSeguro = escaparParaJavascript(codigo)
//        webView.post {
//            webView.evaluateJavascript(codigoSeguro, null)
//        }
    }

    @JavascriptInterface
    fun listarArquivos(){
        Log.d(TAG, "📁 listarArquivos() chamado do JavaScript")
        webView.post {
            Log.d(TAG, "Executando callback listarArquivos")
            listarArquivos?.invoke()
        }
    }

    /** Função chamada do JavaScript para solicitar um nome. */
    @JavascriptInterface
    fun pegarNome() {
        Log.d(TAG, "👤 pegarNome() chamado")
        Toast.makeText(context, "Android recebeu pedido de nome", Toast.LENGTH_SHORT).show()
        val nome = "Gabriel"
        Log.d(TAG, "Enviando nome '$nome' para JavaScript")
        webView.post {
            Log.d(TAG, "Executando JavaScript: receberNome('$nome')")
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
        Log.i(TAG, "📁 WebAppInterface: Solicitação para abrir pasta recebida")
        Log.d(TAG, "Método abrirPasta() chamado do JavaScript")
        try {
            abrirPastaCallback()
            Log.d(TAG, "Callback abrirPasta executado com sucesso")
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao executar callback abrirPasta", e)
        }
    }

    /** salvar uri de pasta com modulos externos da interface */
    @JavascriptInterface
    fun setExternModels(){
        Log.i(TAG, "selecionando modulo externo")
        abrirPasrtasCallback(uriList.externalModulejs.key){
            Log.i(TAG, " modulo externo selected")
        }

    }
    @RequiresApi(Build.VERSION_CODES.P)
    @JavascriptInterface
    fun getAllInfo():String{
      return JSONObject(this.info.all).toString()

    }
    /** Função chamada do JavaScript para ler o conteúdo de um arquivo da pasta. */
    @JavascriptInterface
    fun lerArquivo(caminhoRelativo: String) {
        Log.i(TAG, "📖 lerArquivo chamado com caminho: '$caminhoRelativo'")

        selectedFolderUri?.let { baseUri ->
            Log.d(TAG, "URI da pasta base: $baseUri")

            try {
                val arquivo = localizarArquivoPorCaminho(baseUri, caminhoRelativo)
                if (arquivo != null && arquivo.isFile) {
                    Log.d(TAG, "Arquivo encontrado: ${arquivo.name}, URI: ${arquivo.uri}")

                    val inputStream = context.contentResolver.openInputStream(arquivo.uri)
                    val conteudo = inputStream?.bufferedReader().use { it?.readText() } ?: "Erro ao ler arquivo"
                    Log.d(TAG, "Conteúdo do arquivo lido: ${conteudo.length} caracteres")

                    val conteudoEscapado = conteudo
                        .replace("\\", "\\\\")
                        .replace("'", "\\'")
                        .replace("\n", "\\n")
                        .replace("\r", "")
                    Log.v(TAG, "Conteúdo escapado para JavaScript")

                    webView.post {
                        if (viewPageLoaded) {
                            Log.d(TAG, "Página carregada, executando mostrarConteudo imediatamente")
                            webView.evaluateJavascript("mostrarConteudo('$conteudoEscapado')", null)
                        } else {
                            Log.w(TAG, "Página não carregada, tentando novamente em 300ms")
                            // Tenta novamente após atraso
                            Handler(Looper.getMainLooper()).postDelayed({
                                Log.d(TAG, "Executando mostrarConteudo após delay")
                                webView.evaluateJavascript("mostrarConteudo('$conteudoEscapado','$caminhoRelativo')", null)
                            }, 300)
                        }
                    }
                } else {
                    Log.w(TAG, "Arquivo não encontrado ou não é um arquivo válido: '$caminhoRelativo'")
                    webView.post {
                        Log.d(TAG, "Enviando mensagem de erro para JavaScript")
                        webView.evaluateJavascript("mostrarConteudo('Arquivo não encontrado')", null)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao ler arquivo '$caminhoRelativo'", e)
                webView.post {
                    webView.evaluateJavascript("mostrarConteudo('Erro ao ler arquivo: ${e.message}')", null)
                }
            }
        } ?: run {
            Log.w(TAG, "selectedFolderUri é null - nenhuma pasta selecionada")
            webView.post {
                webView.evaluateJavascript("mostrarConteudo('Nenhuma pasta selecionada')", null)
            }
        }
    }

    /** Percorre recursivamente a árvore DocumentFile para localizar um arquivo por caminho relativo. */
    private fun localizarArquivoPorCaminho(baseUri: Uri, caminhoRelativo: String): DocumentFile? {
        Log.d(TAG, "🔍 Localizando arquivo por caminho: '$caminhoRelativo'")
        Log.d(TAG, "URI base: $baseUri")

        try {
            val partes = caminhoRelativo.split("/")
            Log.d(TAG, "Caminho dividido em ${partes.size} partes: ${partes.joinToString(" -> ")}")

            var atual: DocumentFile? = DocumentFile.fromTreeUri(context, baseUri)
            Log.d(TAG, "DocumentFile raiz criado: ${atual?.name}")

            for ((index, parte) in partes.withIndex()) {
                Log.v(TAG, "Procurando parte $index: '$parte'")

                val arquivos = atual?.listFiles()
                Log.v(TAG, "Arquivos na pasta atual: ${arquivos?.size ?: 0}")

                atual = arquivos?.firstOrNull { it.name == parte }

                if (atual == null) {
                    Log.w(TAG, "Parte '$parte' não encontrada na pasta atual")
                    break
                } else {
                    Log.d(TAG, "Parte '$parte' encontrada: ${atual.name} (${if (atual.isDirectory) "pasta" else "arquivo"})")
                }
            }

            if (atual != null) {
                Log.i(TAG, "✅ Arquivo localizado com sucesso: ${atual.name}")
            } else {
                Log.w(TAG, "❌ Arquivo não encontrado: '$caminhoRelativo'")
            }

            return atual
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao localizar arquivo '$caminhoRelativo'", e)
            return null
        }
    }
}