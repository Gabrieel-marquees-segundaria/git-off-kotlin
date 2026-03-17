package com.g4br3.sitedentrodeapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.g4br3.sitedentrodeapp.components.FileName
import java.io.File

class FileActivity : AppCompatActivity() {
    var isFull = false

    @Volatile
    var viewPageLoaded = true

    companion object {
        // Hook to control the artificial delay used when injecting JS into the WebView.
        // Tests can set this to 0 to make tests faster and more deterministic.
        var pageLoadDelayMs: Long = 400L
    }

    fun currentSite(url: String, onLoaded: (() -> Unit)? = null) {
        viewPageLoaded = false
        webView.post {
            //webView.loadUrl("file:///android_asset/$name.html")
            webView.loadUrl(url)
            webView.postDelayed({
                val js = """
                    console.log("JS injetado com delay");
                    document.body.style.backgroundColor = 'green';
                """.trimIndent()
                webView.evaluateJavascript(js, null)

                viewPageLoaded = true
                onLoaded?.invoke()
            }, pageLoadDelayMs)

        }
    }

    lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
        setContentView(R.layout.activity_file_item_webview)

// ... dentro do onCreate

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let { controller ->
                // Esconde a barra de status e a barra de navegação
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                // Faz com que as barras reapareçam apenas com um swipe (comportamento imersivo)
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
        // Definir título da tela (pode vir via Intent)
        val titulo = intent.getStringExtra("titulo") ?: "Nome da Tela"
        var url = intent.getStringExtra("url") ?: "file:///android_asset/view.html"
        var file = intent.getStringExtra("file")
        var fileName ="hello"
        if (file is String) {
            fileName = file.split("/")[file.split("/").size - 1]
        }
        Log.d("WebView", "file name: $file")
        var conteudo = File(file).readText(Charsets.UTF_8) ?: "not found"
        var conteudoScapado = conteudo
            .replace("\\", "\\\\")
            .replace("'", "\\'")
            .replace("\n", "\\n")
            .replace("\r", "")
        Log.d("WebView", "file conteudo: $conteudoScapado")
        val tvTituloTela = findViewById<TextView>(R.id.tvTituloTela)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        tvTituloTela.text = titulo

        // Botão voltar
        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        webView = findViewById(R.id.webView)
        btnVoltar.setOnClickListener {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                onBackPressed()
            }
        }

        // Configurar WebView
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            setSupportZoom(false)
            builtInZoomControls = false
            displayZoomControls = false
            // Aumenta a fonte para 150% do tamanho original
            textZoom = 200

// Opcional: Melhora o suporte para visualização em desktop
            useWideViewPort = true
            loadWithOverviewMode = true

            // Permite mixed content (HTTP dentro de HTTPS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
        }
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                progressBar.visibility = View.GONE
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                Log.e("WebView", "Erro: ${error?.description} | URL: ${request?.url}")
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
                Log.e("WebView", "HTTP Erro: ${errorResponse?.statusCode}")
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress < 100) {
                    progressBar.visibility = View.VISIBLE
                } else {
                    progressBar.visibility = View.GONE
                }
            }
        }

        Log.d("WebView", "Carregando URL: $url")
        // webView.loadUrl(url)


        currentSite(
            url, {
             //   webView.evaluateJavascript("mostrarConteudo('$conteudoScapado', 'hello')", null)

                webView.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView, url: String) {
                        webView.evaluateJavascript("window.mostrarConteudo('$conteudoScapado', '$fileName')", null)
                    }
                }
            }
        )
        class JsBridge {
            @JavascriptInterface
            fun onJsReady() {
                runOnUiThread {
                    webView.evaluateJavascript("window.mostrarConteudo('$conteudoScapado', '$titulo')", null)
                }
            }
        }

        webView.addJavascriptInterface(JsBridge(), "Android")
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    @SuppressLint("GestureBackNavigation")
    override fun onBackPressed() {
        super.onBackPressed()
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            //super.onBackPressed()

            val intent = Intent(this@FileActivity, FilesListActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}