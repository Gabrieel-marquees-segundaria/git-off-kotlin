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
    onWebViewReady: (WebView) -> Unit
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
                // Habilita JavaScript (necessário para muitos sites modernos)
                settings.javaScriptEnabled = true

                // Permite que a página seja carregada em modo de visão geral
                settings.loadWithOverviewMode = true

                // Habilita suporte a zoom na WebView
                settings.setSupportZoom(true)

                // Define um WebViewClient personalizado para interceptar eventos
                webViewClient = object : WebViewClient() {
                    // Chamado quando uma nova página começa a carregar
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        // Atualiza o estado indicando se é possível voltar na navegação
                        podeVoltar = view?.canGoBack() ?: false
                    }
                }

                // Carrega a URL fornecida como parâmetro
                loadUrl(url)

                // Chama o callback passando a instância da WebView
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