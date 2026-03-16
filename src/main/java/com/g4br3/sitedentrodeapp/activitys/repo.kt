package com.g4br3.sitedentrodeapp.activitys

import androidx.activity.compose.setContent
import com.g4br3.sitedentrodeapp.MainActivity
import com.g4br3.sitedentrodeapp.components.WebFiles

class repo(var webFiles: WebFiles) {

//
//    fun setup(var activity: MainActivity)  {
//
//        // Composable que cria a WebView
//       activity. setContent {
//            println(webFiles.webList)
//            WebViewScreen(
//                // Carrega página HTML dos assets do app
//
//                url = webFiles.webList,
//                onWebViewReady = { webView ->
//                    // Armazena referência da WebView quando ela estiver pronta
//                    webViewRef = webView
//
//                    // Instancia a interface de comunicação JS ↔ Android
//                    // Passa uma lambda que abre o seletor de pasta quando chamada
//                    interfaceJS = WebAppInterface(
//                        this, webView, webFiles,
//                        { nome: String ->
//                            // Você recebeu "nome" aqui
//                            openFileLauncher.launch(arrayOf("*/*")) // Pode filtrar com base no nome se quiser
//                            println("Nome recebido do JS: $nome")
//                            openNameFileLauncher = nome
//                        },
//                        { homeWebSite() }
//                    ) {
//
//                        openDirectoryLauncher.launch(null)
//
//
//                    }
//
//// Agora que interfaceJS foi criado, podemos adicionar à WebView
//                    webView.addJavascriptInterface(interfaceJS, "Android")
//
//
//                },
//                onPageFinished = {
//                    selectedFolderUri = recuperarUri()
//
//                    selectedFolderUri?.let {
//                        interfaceJS.selectedFolderUri = it
//                        webViewRef?.let { webView -> listarArquivosDaPasta(it, webView) }
//                    }
//                }
//            )
//
//
//        }
//
//
//
//}
}