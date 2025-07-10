package com.g4br3.sitedentrodeapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import com.g4br3.sitedentrodeapp.components.WebViewScreen
import org.json.JSONArray
import java.io.File
/**
 * Tela principal do app. Gera uma WebView com interface JavaScript,
 * permitindo a seleção de uma pasta no Android e listando seus arquivos para o JavaScript.
 */
class MainActivity : ComponentActivity() {

    // Armazena a URI da pasta selecionada pelo usuário
    private var selectedFolderUri: Uri? = null

    // Referência à WebView para comunicação com JavaScript
    private var webViewRef: WebView? = null

    // Interface que permite comunicação entre JavaScript e Android
    private lateinit var interfaceJS: WebAppInterface

    // Launcher SAF (Storage Access Framework) para selecionar pastas
    private lateinit var openDirectoryLauncher: ActivityResultLauncher<Uri?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        // Habilita exibição edge-to-edge (tela cheia)
        enableEdgeToEdge()

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
            WebViewScreen(
                // Carrega página HTML dos assets do app
                url = "file:///android_asset/list.html",
                onWebViewReady = { webView ->
                    // Armazena referência da WebView quando ela estiver pronta
                    webViewRef = webView

                    // Instancia a interface de comunicação JS ↔ Android
                    // Passa uma lambda que abre o seletor de pasta quando chamada
                    interfaceJS = WebAppInterface(this, webView, { homeWebSite() }) {

                        openDirectoryLauncher.launch(null)
                    }

// Agora que interfaceJS foi criado, podemos adicionar à WebView
                    webView.addJavascriptInterface(interfaceJS, "Android")

// Define WebViewClient e espera carregar a página
                    webView.webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            selectedFolderUri = recuperarUri()
                            selectedFolderUri?.let {
                                interfaceJS.selectedFolderUri = it
                                webViewRef?.let { webView -> listarArquivosDaPasta(it, webView) }
                            }
                        }
                    }



                }
            )
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

    private fun salvarUri(uri: Uri) {
        val prefs = getSharedPreferences("meu_app_prefs", MODE_PRIVATE)
        prefs.edit().putString("uri_salva", uri.toString()).apply()
        println("uri salva com sucesso")

    }

    private fun recuperarUri(): Uri? {
        val prefs = getSharedPreferences("meu_app_prefs", MODE_PRIVATE)
        val uriString = prefs.getString("uri_salva", null)
        return uriString?.let { Uri.parse(it) }
    }

}