/*
package com.g4br3.sitedentrodeapp


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import org.json.JSONArray
import androidx.documentfile.provider.DocumentFile

class WebAppInterface(
    private val context: Context,
    private val webView: WebView,
    private val abrirPastaCallback: () -> Unit
) {
    @JavascriptInterface
    fun pegarNome() {
        Toast.makeText(context, "Android recebeu pedido de nome", Toast.LENGTH_SHORT).show()
        val nome = "Gabriel"
        webView.post {
            webView.evaluateJavascript("receberNome('$nome')", null)
        }
    }

    @JavascriptInterface
    fun abrirPasta() {
        abrirPastaCallback()
    }
}

class MainActivity : ComponentActivity() {

    private var selectedFolderUri: Uri? = null
    private var webViewRef: WebView? = null

    private lateinit var openDirectoryLauncher: androidx.activity.result.ActivityResultLauncher<Uri?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Registra o launcher SAF e lida com o resultado
        openDirectoryLauncher = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri: Uri? ->
            if (uri != null) {
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                selectedFolderUri = uri
                Toast.makeText(this, "Pasta selecionada: $uri", Toast.LENGTH_LONG).show()

                // Lista arquivos da pasta selecionada
                webViewRef?.let { listarArquivosDaPasta(uri, it) }
            } else {
                Toast.makeText(this, "Nenhuma pasta selecionada", Toast.LENGTH_SHORT).show()
            }
        }

        setContent {
            Home("file:///android_asset/meupagina.html") {
                openDirectoryLauncher.launch(null)
            }.also { webViewRef = it }
        }
    }


    fun listarArquivosDaPasta(uri: Uri, webView: WebView) {
        val docFile = DocumentFile.fromTreeUri(this, uri)
        val nomesArquivos = mutableListOf<String>()

        if (docFile != null && docFile.isDirectory) {
            val arquivos = docFile.listFiles()
            for (arquivo in arquivos) {
                nomesArquivos.add(arquivo.name ?: "Nome desconhecido")
            }
        }

        val arquivosJson = JSONArray(nomesArquivos).toString()
        webView.evaluateJavascript("receberArquivos('$arquivosJson')", null)
    }


}


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun Home(url: String, abrirPastaCallback: () -> Unit): WebView? {
    var webview: WebView? by remember { mutableStateOf(null) }
    var backButton by remember { mutableStateOf(false) }

    AndroidView(
        factory = { ctx ->
            WebView(ctx).apply {
                settings.javaScriptEnabled = true
                settings.loadWithOverviewMode = true
                settings.setSupportZoom(true)

                val interfaceJS = WebAppInterface(ctx, this, abrirPastaCallback)
                addJavascriptInterface(interfaceJS, "Android")

                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        backButton = view?.canGoBack() ?: false
                    }
                }

                loadUrl(url)
            }
        },
        update = {
            webview = it
        }
    )

    BackHandler(enabled = backButton) {
        webview?.goBack()
    }

    return webview
}
*/