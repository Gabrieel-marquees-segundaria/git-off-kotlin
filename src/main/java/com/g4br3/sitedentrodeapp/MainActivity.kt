package com.g4br3.sitedentrodeapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import com.g4br3.sitedentrodeapp.components.WebViewScreen
import com.g4br3.sitedentrodeapp.components.StorageHelper
import org.json.JSONArray
import java.io.File
import com.g4br3.sitedentrodeapp.components.WebFiles

/**
 * Tela principal do app. Gera uma WebView com interface JavaScript,
 * permitindo a seleção de uma pasta no Android e listando seus arquivos para o JavaScript.
 */
class MainActivity : ComponentActivity() {

    // Armazena a URI da pasta selecionada pelo usuário
    private var selectedFolderUri: Uri? = null
    private  var storageHelper: StorageHelper = StorageHelper(this)
    private   var webFiles: WebFiles = WebFiles(storageHelper)


    // Referência à WebView para comunicação com JavaScript
    private var webViewRef: WebView? = null

    // Interface que permite comunicação entre JavaScript e Android
    private lateinit var interfaceJS: WebAppInterface

    // Launcher SAF (Storage Access Framework) para selecionar pastas
    private lateinit var openDirectoryLauncher: ActivityResultLauncher<Uri?>
    private lateinit var openFileLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var openNameFileLauncher: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //storageHelper = StorageHelper(this)
        webFiles.loadData().storageHelper = storageHelper
        println(webFiles.dict)




        // Habilita exibição edge-to-edge (tela cheia)
        enableEdgeToEdge()



        openFileLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            if (uri != null) {
                // Exemplo: Ler conteúdo do arquivo
//                val inputStream = contentResolver.openInputStream(uri)
//                val conteudo = inputStream?.bufferedReader()?.use { it.readText() } ?: "Erro ao ler"
//
//                // Enviar para o JS (ou qualquer outra coisa)
//                webViewRef?.evaluateJavascript("mostrarConteudo('${conteudo.replace("'", "\\'")}')", null)
                storageHelper.salvarString(openNameFileLauncher,uri.toString())
                // Carrega o arquivo HTML no WebView
                loadHtmlFromUri(uri)

               }
        }

        // Configura launcher para abrir o seletor de pasta do sistema
        openDirectoryLauncher =
            registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri: Uri? ->
                if (uri != null) {
                    // Solicita permissão persistente para acessar a pasta
                    // Isso permite que o app mantenha acesso mesmo após reinicialização
                    contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )

                    // Armazena a URI da pasta selecionada
                    selectedFolderUri = uri
                    salvarUri(uri)

                    // Atualiza a interface JS com a nova pasta selecionada
                    interfaceJS.selectedFolderUri = uri

                    // Mostra toast confirmando a seleção
                    Toast.makeText(this, "Pasta selecionada: $uri", Toast.LENGTH_LONG).show()

                    // Lista os arquivos da pasta e envia para o JavaScript
                    webViewRef?.let { listarArquivosDaPasta(uri, it, true) }
                } else {
                    // Usuario cancelou a seleção
                    Toast.makeText(this, "Nenhuma pasta selecionada", Toast.LENGTH_SHORT).show()
                }
            }

        // Composable que cria a WebView
        setContent {
    println( webFiles.webList)
            WebViewScreen(
                // Carrega página HTML dos assets do app

                url = webFiles.webList,
                onWebViewReady = { webView ->
                    // Armazena referência da WebView quando ela estiver pronta
                    webViewRef = webView

                    // Instancia a interface de comunicação JS ↔ Android
                    // Passa uma lambda que abre o seletor de pasta quando chamada
                    interfaceJS = WebAppInterface(this, webView, webFiles,
                        { nome: String ->
                            // Você recebeu "nome" aqui
                            openFileLauncher.launch(arrayOf("*/*")) // Pode filtrar com base no nome se quiser
                            println("Nome recebido do JS: $nome")
                            openNameFileLauncher = nome
                        },
                        { homeWebSite() }
                        ) {

                        openDirectoryLauncher.launch(null)


                    }

// Agora que interfaceJS foi criado, podemos adicionar à WebView
                    webView.addJavascriptInterface(interfaceJS, "Android")


                },
                onPageFinished = {
                    selectedFolderUri = recuperarUri()

                    selectedFolderUri?.let {
                        interfaceJS.selectedFolderUri = it
                        webViewRef?.let { webView -> listarArquivosDaPasta(it, webView) }
                    }
                }
            )





        }
    }


    /**
     * Carrega arquivo HTML selecionado no WebView
     */
    private fun loadHtmlFromUri(uri: Uri) {
        try {
            // Garantir permissão persistente para a URI
            val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
            contentResolver.takePersistableUriPermission(uri, takeFlags)

            // Ler o conteúdo do arquivo HTML
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val htmlContent = inputStream.bufferedReader().use { it.readText() }

                // Verificar se é um arquivo HTML
                if (isHtmlFile(uri, htmlContent)) {
                    loadHtmlContentInWebView(htmlContent, uri)
                } else {
                    // Se não for HTML, enviar conteúdo para JavaScript
                    sendContentToJavaScript(htmlContent)
                }
            }

        } catch (e: SecurityException) {
            Toast.makeText(this, "Erro de permissão: ${e.message}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Erro ao carregar arquivo: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Verifica se o arquivo é HTML
     */
    private fun isHtmlFile(uri: Uri, content: String): Boolean {
        val fileName = getFileName(uri)
        val isHtmlExtension = fileName?.endsWith(".html", ignoreCase = true) == true ||
                fileName?.endsWith(".htm", ignoreCase = true) == true
        val hasHtmlContent = content.contains("<html", ignoreCase = true) ||
                content.contains("<!doctype html", ignoreCase = true)

        return isHtmlExtension || hasHtmlContent
    }

    /**
     * Obtém o nome do arquivo a partir da URI
     */
    private fun getFileName(uri: Uri): String? {
        return DocumentFile.fromSingleUri(this, uri)?.name
    }

    /**
     * Carrega conteúdo HTML diretamente no WebView
     */
    private fun loadHtmlContentInWebView(htmlContent: String, originalUri: Uri) {
        try {
            // Obter base URL para resolver recursos relativos
            val baseUrl = getBaseUrlFromUri(originalUri)

            webViewRef?.loadDataWithBaseURL(
                baseUrl,
                htmlContent,
                "text/html",
                "UTF-8",
                null
            )

            Toast.makeText(this, "Arquivo HTML carregado com sucesso", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Toast.makeText(this, "Erro ao carregar HTML: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Envia conteúdo para JavaScript (para arquivos não-HTML)
     */
    private fun sendContentToJavaScript(content: String) {
        // Escapar caracteres especiais para JavaScript
        val escapedContent = content
            .replace("\\", "\\\\")
            .replace("'", "\\'")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")

        webViewRef?.evaluateJavascript("mostrarConteudo('$escapedContent')", null)
    }

    /**
     * Obtém base URL a partir da URI para resolver recursos relativos
     */
    private fun getBaseUrlFromUri(uri: Uri): String? {
        return try {
            val path = uri.path
            if (path != null) {
                val lastSlash = path.lastIndexOf('/')
                if (lastSlash != -1) {
                    "file://${path.substring(0, lastSlash + 1)}"
                } else null
            } else null
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Método para carregar arquivo HTML salvo anteriormente
     */
    private fun loadSavedHtmlFile(fileName: String) {
        val savedUriString = storageHelper.recuperarString(fileName)
        if (savedUriString != null) {
            try {
                val uri = Uri.parse(savedUriString)
                loadHtmlFromUri(uri)
            } catch (e: Exception) {
                Toast.makeText(this, "Erro ao carregar arquivo salvo: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Lista todos os arquivos da pasta selecionada (inclusive subpastas) e envia ao JavaScript.
     *
     * @param uri URI da pasta selecionada
     * @param webView Instância da WebView para comunicação com JavaScript
     */
    private fun listarArquivosDaPasta(uri: Uri, webView: WebView, clear: Boolean =false) {
        val nomeArquivoCache = "lista_arquivos.txt"
        val arquivoCache = File(filesDir, nomeArquivoCache)

        if (arquivoCache.exists() and !clear) {
            // Se já existe, lê o conteúdo e retorna ao JavaScript
            val conteudo = arquivoCache.readText()
            webView.evaluateJavascript("receberArquivos('$conteudo')", null)
            return
        }

        val docFile = DocumentFile.fromTreeUri(this, uri)
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

    private fun homeWebSite(){
        selectedFolderUri?.let {
            webViewRef?.let { webView ->
                listarArquivosDaPasta(it, webView)
            }
        }
    }
    private fun jsInject(webView:WebView,javascript: String){

        webView.evaluateJavascript(javascript, null)
        }
    private fun salvarUri(uri: Uri) {

        storageHelper.salvarString("uri_salva", uri.toString())
        println("uri salva com sucesso")

    }

    private fun recuperarUri(): Uri? {

        val uriString = storageHelper.recuperarString("uri_salva")
        println("uri-salva$uriString")
        return uriString?.let { Uri.parse(it) }
    }

}