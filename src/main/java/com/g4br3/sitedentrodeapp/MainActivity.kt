package com.g4br3.sitedentrodeapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import java.io.BufferedReader
import java.io.InputStreamReader
import com.g4br3.sitedentrodeapp.components.FileManager
import androidx.core.net.toUri


/**
 * Atividade principal da aplicação.
 *
 * Esta classe gerencia o WebView e a integração com o Storage Access Framework (SAF)
 * para seleção de arquivos HTML e listagem de arquivos em pastas.
 */
class MainActivity : ComponentActivity() {
    private lateinit var fileManager: FileManager
    private val TAG = "MainActivity"
    private var selectedFolderUri: Uri? = null
    private var webViewRef: WebView? = null
    private var selectedHtmlUri by mutableStateOf<Uri?>(null)
    private var htmlContent by mutableStateOf<String?>(null)
    private var isHtmlLoaded by mutableStateOf(false)
    private var weblistName = "123weblistname123"
    private lateinit var openDirectoryLauncher: androidx.activity.result.ActivityResultLauncher<Uri?>
    private lateinit var openHtmlLauncher: androidx.activity.result.ActivityResultLauncher<Array<String>>

    /**
     * Método chamado quando a atividade é criada.
     *
     * Inicializa os launchers para seleção de pasta e arquivo HTML,
     * e configura o conteúdo da UI.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println("🚀 MainActivity: Iniciando onCreate()")
        Log.i(TAG, "MainActivity onCreate() iniciado")
        fileManager = FileManager(this)


        enableEdgeToEdge()
        val uriSalva = getSharedPreferences("prefs", Context.MODE_PRIVATE)
            .getString("html_uri", null)

        if (uriSalva != null) {
            try {
                val uri = Uri.parse(uriSalva)
                // tenta persistir permissão novamente se necessário
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                selectedHtmlUri = uri
                carregarArquivoHtml(uri)
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao restaurar URI salva: ${e.message}")
            }
        }
        val uriPathSalva = getSharedPreferences("prefs", Context.MODE_PRIVATE)
            .getString("path_uri", null)
        println("uriPathSalva: $uriPathSalva")
        println("webview$webViewRef")
        selectedFolderUri = Uri.parse(uriPathSalva)




        // Registra o launcher SAF para seleção de pasta
        println("📋 MainActivity: Registrando launcher para seleção de pasta")

        openDirectoryLauncher = this.registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri: Uri? ->
            println("📂 MainActivity: Resultado do seletor de pasta recebido")


            if (uri != null) {
                println("✅ MainActivity: Pasta selecionada: $uri")
                Log.i(TAG, "Pasta selecionada: $uri")

                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                selectedFolderUri = uri
                

                Toast.makeText(this, "Pasta selecionada: $uri", Toast.LENGTH_LONG).show()

                // Lista arquivos da pasta selecionada
                webViewRef?.let {
                    println("📄 MainActivity: Iniciando listagem de arquivos da pasta")
                    fileManager.listarArquivosDasPastas(uri, it, true)


                }
            } else {
                println("❌ MainActivity: Nenhuma pasta foi selecionada")
                Log.w(TAG, "Nenhuma pasta selecionada pelo usuário")
                Toast.makeText(this, "Nenhuma pasta selecionada", Toast.LENGTH_SHORT).show()
            }
        }


        // Registra o launcher SAF para seleção de arquivo HTML
        println("📄 MainActivity: Registrando launcher para seleção de arquivo HTML")
        openHtmlLauncher = this.registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            println("📄 MainActivity: Resultado do seletor de arquivo HTML recebido")

            if (uri != null) {
                println("✅ MainActivity: Arquivo HTML selecionado: $uri")
                Log.i(TAG, "Arquivo HTML selecionado: $uri")

                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                selectedHtmlUri = uri
                Toast.makeText(this, "Arquivo HTML selecionado", Toast.LENGTH_SHORT).show()
                // ⬇️ SALVA O URI NO SharedPreferences
                getSharedPreferences("prefs", Context.MODE_PRIVATE)
                    .edit()
                    .putString("html_uri", uri.toString())
                    .apply()

                // Carrega o conteúdo do arquivo HTML
                carregarArquivoHtml(uri)
            } else {
                println("❌ MainActivity: Nenhum arquivo HTML foi selecionado")
                Log.w(TAG, "Nenhum arquivo HTML selecionado pelo usuário")
                Toast.makeText(this, "Nenhum arquivo HTML selecionado", Toast.LENGTH_SHORT).show()
            }
        }

        println("🎨 MainActivity: Configurando conteúdo da UI")
        setContent {
            MainScreen()
        }

        println("✅ MainActivity: onCreate() concluído com sucesso")
        Log.i(TAG, "MainActivity onCreate() concluído")
    }

    /**
     * Carrega o conteúdo de um arquivo HTML selecionado.
     *
     * @param uri URI do arquivo HTML selecionado
     */
    private fun carregarArquivoHtml(uri: Uri) {
        println("📖 MainActivity: Iniciando carregamento do arquivo HTML")
        Log.d(TAG, "carregarArquivoHtml() iniciado para URI: $uri")

        try {

            contentResolver.openInputStream(uri)?.use { inputStream ->
                val reader = BufferedReader(InputStreamReader(inputStream))
                val content = reader.readText()

                println("📄 MainActivity: Conteúdo HTML carregado (${content.length} caracteres)")
                Log.d(TAG, "Conteúdo HTML carregado com sucesso")

                htmlContent = content
                isHtmlLoaded = true

                Toast.makeText(this, "Arquivo HTML carregado com sucesso", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            println("❌ MainActivity: Erro ao carregar arquivo HTML: ${e.message}")
            Log.e(TAG, "Erro ao carregar arquivo HTML", e)
            Toast.makeText(this, "Erro ao carregar arquivo HTML: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }



    /**
     * Tela principal que gerencia a seleção de arquivo HTML e exibição do WebView.
     */
    @Composable
    private fun MainScreen() {
        println("🏠 MainScreen: Iniciando composable MainScreen")
        Log.d("MainScreen", "MainScreen composable iniciado")

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!isHtmlLoaded) {
                // Tela de seleção de arquivo HTML
                println("📄 MainScreen: Exibindo tela de seleção de arquivo HTML")

                Text(
                    text = "Selecione um arquivo HTML",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
                )

                Button(
                    onClick = {
                        println("🎯 MainScreen: Botão de seleção de HTML clicado")
                        openHtmlLauncher.launch(arrayOf("text/html", "*/*"))
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Selecionar Arquivo HTML")
                }

                if (selectedHtmlUri != null) {
                    Text(
                        text = "Arquivo selecionado: ${selectedHtmlUri?.lastPathSegment}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            } else {
                // Exibe o WebView com o HTML carregado
                println("🌐 MainScreen: Exibindo WebView com HTML carregado")

                htmlContent?.let { content ->
                    WebViewContainer(
                        htmlContent = content,
                        abrirPastaCallback = {
                            println("📁 MainScreen: Callback para abrir pasta acionado")
                            openDirectoryLauncher.launch(null)
                        }
                    )
                }
            }
        }
    }

    /**
     * Container do WebView que carrega o conteúdo HTML.
     *
     * @param htmlContent Conteúdo HTML a ser carregado
     * @param abrirPastaCallback Callback para abrir o seletor de pasta
     */
    @SuppressLint("SetJavaScriptEnabled")
    @Composable
    private fun WebViewContainer(htmlContent: String, abrirPastaCallback: () -> Unit) {
        println("🌐 WebViewContainer: Iniciando composable WebViewContainer")
        Log.d("WebViewContainer", "WebViewContainer composable iniciado")

        var webview: WebView? by remember { mutableStateOf(null) }
        var backButton by remember { mutableStateOf(false) }

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                println("🌐 WebViewContainer: Criando nova instância do WebView")
                Log.d("WebViewContainer", "Criando WebView")

                WebView(ctx).apply {
                    println("⚙️ WebViewContainer: Configurando settings do WebView")
                    settings.javaScriptEnabled = true
                    settings.loadWithOverviewMode = true
                    settings.setSupportZoom(true)
                    settings.allowFileAccess = true
                    settings.allowContentAccess = true

                    println("🔌 WebViewContainer: Adicionando interface JavaScript")
                    val interfaceJS = WebAppInterface(ctx,
                        this,
                        abrirPastaCallback,
                        null,
                        {fileManager.listarArquivosDasPastas(selectedFolderUri,this)})
                    addJavascriptInterface(interfaceJS, "Android")

                    println("🎯 WebViewContainer: Configurando WebViewClient")
                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            println("🔄 WebViewContainer: Página iniciada: $url")
                            Log.d("WebViewContainer", "Página iniciada: $url")
                            backButton = view?.canGoBack() ?: false
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            println("✅ WebViewContainer: Página carregada: $url")
                            Log.d("WebViewContainer", "Página carregada: $url")
                        }
                    }

                    println("📄 WebViewContainer: Carregando conteúdo HTML")
                    loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)

                }
            },
            update = {
                println("🔄 WebViewContainer: Atualizando referência do WebView")
                webview = it
                webViewRef = it
            }
        )

        BackHandler(enabled = backButton) {
            println("◀️ WebViewContainer: Botão voltar pressionado")
            webview?.goBack()
        }

        println("🏁 WebViewContainer: Composable WebViewContainer finalizado")
    }
}