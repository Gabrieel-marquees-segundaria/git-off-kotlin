package com.g4br3.sitedentrodeapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import com.g4br3.sitedentrodeapp.components.WebViewScreen
import org.json.JSONArray

/**
 * Tela principal do app. Gera uma WebView com interface JavaScript,
 * permitindo a seleção de uma pasta no Android e listando seus arquivos para o JavaScript.
 */
class MainActivity : ComponentActivity() {

    private var selectedFolderUri: Uri? = null
    private var webViewRef: WebView? = null
    private lateinit var interfaceJS: WebAppInterface

    // Launcher SAF para selecionar pastas
    private lateinit var openDirectoryLauncher: androidx.activity.result.ActivityResultLauncher<Uri?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Configura launcher para abrir o seletor de pasta
        openDirectoryLauncher =
            registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri: Uri? ->
                if (uri != null) {
                    contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                    selectedFolderUri = uri
                    interfaceJS.selectedFolderUri = uri // atualiza interface JS

                    Toast.makeText(this, "Pasta selecionada: $uri", Toast.LENGTH_LONG).show()
                    webViewRef?.let { listarArquivosDaPasta(uri, it) }
                } else {
                    Toast.makeText(this, "Nenhuma pasta selecionada", Toast.LENGTH_SHORT).show()
                }
            }

        // Composable que cria a WebView
        setContent {
            WebViewScreen(
                url = "file:///android_asset/meupagina.html",
                onWebViewReady = { webView ->
                    webViewRef = webView

                    // Instancia a interface de comunicação JS ↔ Android
                    interfaceJS = WebAppInterface(this, webView) {
                        openDirectoryLauncher.launch(null)
                    }
                    webView.addJavascriptInterface(interfaceJS, "Android")
                }
            )
        }
    }

    /**
     * Lista todos os arquivos da pasta selecionada (inclusive subpastas) e envia ao JavaScript.
     */
    private fun listarArquivosDaPasta(uri: Uri, webView: WebView) {
        val docFile = DocumentFile.fromTreeUri(this, uri)
        val nomesArquivos = mutableListOf<String>()

        /**
         * Percorre arquivos e subpastas recursivamente, construindo o caminho relativo.
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
        webView.evaluateJavascript("receberArquivos('$arquivosJson')", null)
    }
}