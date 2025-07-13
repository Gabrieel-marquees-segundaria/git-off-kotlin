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
@SuppressLint("SetJavaScriptEnabled") // Suprime o warning sobre JavaScript habilitado
@Composable
fun WebViewScreen(
    url: String,

    onWebViewReady: (WebView) -> Unit,
    onPageFinished: (() -> Unit)? = null // <- CALLBACK
) {
    // Estado para armazenar a referência da WebView criada
    var webView: WebView? by remember { mutableStateOf(null) }

    // Estado para controlar se o botão voltar está habilitado
    var podeVoltar by remember { mutableStateOf(false) }

    // AndroidView é usado para integrar Views nativas do Android no Compose
    AndroidView(
        factory = { ctx ->

            // Cria uma nova instância da WebView
            WebView(ctx).apply {
                settings.javaScriptEnabled = true
                settings.loadWithOverviewMode = true
                settings.setSupportZoom(true)

                // Define o WebViewClient ANTES de carregar a URL
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        podeVoltar = view?.canGoBack() ?: false
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        onPageFinished?.invoke()
//                        view?.postDelayed({
//                            val js = """
//                    console.log("JS injetado com delay");
//                    document.body.style.backgroundColor = 'red';
//                """.trimIndent()
//                            view.evaluateJavascript(js, null)
//                        }, 10)
                    }
                }

                // ✅ SÓ CHAMA DEPOIS DE DEFINIR O webViewClient
                loadUrl(url)

                onWebViewReady(this)
            }

        },
        // Chamado quando a WebView é atualizada
        update = {
            // Armazena a referência da WebView no estado
            webView = it
        }
    )

    // Intercepta o botão voltar do sistema Android
    BackHandler(enabled = podeVoltar) {
        // Quando o botão voltar é pressionado e há histórico, navega para a página anterior
        webView?.goBack()
    }
}