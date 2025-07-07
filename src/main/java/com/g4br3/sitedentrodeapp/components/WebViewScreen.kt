package com.g4br3.sitedentrodeapp.components

import android.webkit.WebView
import android.graphics.Bitmap
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.activity.compose.BackHandler
import android.webkit.WebViewClient
import android.annotation.SuppressLint

/**
 * Composable que exibe uma WebView com JavaScript ativado.
 *
 * @param url URL da página a ser carregada.
 * @param onWebViewReady Callback com a instância da WebView, útil para comunicação nativa.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    url: String,
    onWebViewReady: (WebView) -> Unit
) {
    var webView: WebView? by remember { mutableStateOf(null) }
    var podeVoltar by remember { mutableStateOf(false) }

    AndroidView(
        factory = { ctx ->
            WebView(ctx).apply {
                settings.javaScriptEnabled = true
                settings.loadWithOverviewMode = true
                settings.setSupportZoom(true)

                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        podeVoltar = view?.canGoBack() ?: false
                    }
                }

                loadUrl(url)
                onWebViewReady(this)
            }
        },
        update = {
            webView = it
        }
    )

    BackHandler(enabled = podeVoltar) {
        webView?.goBack()
    }
}
