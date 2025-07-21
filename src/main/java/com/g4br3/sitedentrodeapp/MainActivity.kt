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
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.g4br3.sitedentrodeapp.components.FileManager
import com.g4br3.sitedentrodeapp.components.UriList
import com.g4br3.sitedentrodeapp.components.modulos
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * Atividade principal da aplicação.
 *
 * Esta classe gerencia o WebView e a integração com o Storage Access Framework (SAF)
 * para seleção de arquivos HTML e listagem de arquivos em pastas.
 */
class MainActivity : ComponentActivity() {
    private lateinit var interfaceJS: WebAppInterface
    private lateinit var fileManager: FileManager
    private val TAG = "MainActivity"
    private var webViewRef: WebView? = null
    private var uriList: UriList = UriList()
    private var htmlContent by mutableStateOf<String?>(null)
    private var isHtmlLoaded by mutableStateOf(false)
    private lateinit var openDirectoryLauncher: androidx.activity.result.ActivityResultLauncher<Uri?>
    private lateinit var openFileLauncher: androidx.activity.result.ActivityResultLauncher<Array<String>>
    private var theOpenLaucherCallback: ((Uri) -> Unit)? = null
    private lateinit var appDataModule: String
    /**
     * Método chamado quando a atividade é criada.
     *
     * Inicializa os launchers para seleção de pasta e arquivo HTML,
     * e configura o conteúdo da UI.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appDataModule = getSharedPreferences("prefs", Context.MODE_PRIVATE)
            .getString(intent.getStringExtra(modulos).toString(), "console.log('modulo nao encontrado')")
            .toString()
        installSplashScreen()
        println("🚀 MainActivity: Iniciando onCreate()")
        Log.i(TAG, "MainActivity onCreate() iniciado")
        fileManager = FileManager(this)


        enableEdgeToEdge()
        val uriSalva = getSharedPreferences("prefs", Context.MODE_PRIVATE)
            .getString(uriList.html.key, null)
        uriList.html.uri = uriStatus(uriSalva) { uri ->
            CoroutineScope(Dispatchers.Default).launch {
                carregarArquivoHtml(uri)
            }
        }

        val uriPathSalva = getSharedPreferences("prefs", Context.MODE_PRIVATE)
            .getString(uriList.repository.key, null)
        println("uriPathSalva: $uriPathSalva")
        uriList.repository.uri = uriStatus(uriPathSalva)
        println("📋 MainActivity: Registrando launcher para seleção de pasta")

        openDirectoryLauncher =
            this.registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri: Uri? ->
                println("📂 MainActivity: Resultado do seletor de pasta recebido")

                if (uri != null) {
                    println("✅ MainActivity: Pasta selecionada: $uri")
                    Log.i(TAG, "Pasta selecionada: $uri")

                    contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )

                    Toast.makeText(this, "Pasta selecionada: $uri", Toast.LENGTH_LONG).show()

                    theOpenLaucherCallback?.invoke(uri)
                } else {
                    println("❌ MainActivity: Nenhuma pasta foi selecionada")
                    Log.w(TAG, "Nenhuma pasta selecionada pelo usuário")
                    Toast.makeText(this, "Nenhuma pasta selecionada", Toast.LENGTH_SHORT).show()
                }
                Log.d(TAG, "modulo js externo carregado com sucesso")
            }


        // Registra o launcher SAF para seleção de arquivo HTML
        println("📄 MainActivity: Registrando launcher para seleção de arquivo")
        openFileLauncher =
            this.registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
                println("📄 MainActivity: Resultado do seletor de arquivo HTML recebido")

                if (uri != null) {
                    println("✅ MainActivity: Arquivo  selecionado: $uri")
                    Log.i(TAG, "Arquivo  selecionado: $uri")

                    contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    theOpenLaucherCallback?.invoke(uri)
                } else {
                    println("❌ MainActivity: Nenhum arquivo  foi selecionado")
                    Log.w(TAG, "Nenhum arquivo  selecionado pelo usuário")
                    Toast.makeText(this, "Nenhum arquivo  selecionado", Toast.LENGTH_SHORT).show()
                }
            }

        println("🎨 MainActivity: Configurando conteúdo da UI")
        setContent {
            MainScreen()
        }
        println("✅ MainActivity: onCreate() concluído com sucesso")
        Log.i(TAG, "MainActivity onCreate() concluído")
//        @Suppress("DEPRECATION")
//        val appData = intent.getParcelableExtra<AppData>("FILE_DATA")
//        println(appData)
    }
    /**
     * Carrega o conteúdo de um arquivo HTML selecionado.
     *
     * @param uri URI do arquivo HTML selecionado
     */
    private fun carregarArquivoHtml(uri: Uri) {
        println("📖 MainActivity: Iniciando carregamento do arquivo HTML")
        Log.d(TAG, "carregarArquivoHtml() iniciado para URI: $uri")
        fileManager.carregarArquivo(uri, "text/html")
        htmlContent = fileManager.Content
        isHtmlLoaded = fileManager.isLoaded
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
                        selectFileSAF(uriList.html.key, { uri ->
                            uriList.html.uri = uri
                            // Carrega o conteúdo do arquivo HTML
                            CoroutineScope(Dispatchers.Default).launch {
                                carregarArquivoHtml(uri)
                            }
                        }, "text/html")
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Selecionar Arquivo HTML")
                }

                if (uriList.html.uri != null) {
                    Text(
                        text = "Arquivo selecionado: ${uriList.html.uri?.lastPathSegment}",
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
                            //openDirectoryLauncher.launch(null)

                            selectFolderURI(uriList.repository.key) { uri ->
                                uriList.repository.uri = uri
                                // Lista arquivos da pasta selecionada
                                webViewRef?.let {
                                    println("📄 MainActivity: Iniciando listagem de arquivos da pasta")
                                    CoroutineScope(Dispatchers.Default).launch {
                                        fileManager.listarArquivosDasPastas(uri, it, true)
                                        getSharedPreferences("prefs", Context.MODE_PRIVATE)
                                            .edit()
                                            .putString("path_uri", uri.toString())
                                            .apply()
                                    }

                                }

                                interfaceJSupdate()
                            }
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
        Log.d("WebViewContainer", "WebViewContainer composable iniciado")

        var webview: WebView? by remember { mutableStateOf(null) }
        var backButton by remember { mutableStateOf(false) }

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                println("🌐 WebViewContainer: Criando nova instância do WebView")
                Log.d("WebViewContainer", "Criando WebView")

                WebView(ctx).apply {
                    println("⚙️ WebViewContainer: Configurando settings-2 do WebView")
                    settings.javaScriptEnabled = true
                    settings.loadWithOverviewMode = true
                    settings.setSupportZoom(true)
                    settings.allowFileAccess = true
                    settings.allowContentAccess = true

                    println("🔌 WebViewContainer: Adicionando interface JavaScript")
                    interfaceJS = WebAppInterface(
                        ctx,
                        this,
                        abrirPastaCallback,
                        fun(name: String, on_selected: (Uri) -> Unit) {
                            selectFolderURI(name, on_selected)
                        },
                        { homeWebSite() })

                    addJavascriptInterface(interfaceJS, "Android")
                    interfaceJSupdate()
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
                            loadJS()
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
    private fun homeWebSite() {
        uriList.repository.uri?.let {
            webViewRef?.let { webView ->
                fileManager.listarArquivosDasPastas(it, webView)
            }
        }
    }
    private fun loadJS() {
        val code: String = appDataModule
            webViewRef?.let { webView ->
            println("item da lista de javascript scripts: ${code.take(20)}")
            webView.post {
                webView.evaluateJavascript(code, null)
            }
        }
    }
    private fun interfaceJSupdate() {
        interfaceJS.selectedFolderUri = uriList.repository.uri
    }

    private fun selectFileSAF(
        name: String?,
        on_selected: (Uri) -> Unit,
        mimeType: String = "text/html"
    ) {
        theOpenLaucherCallback = { uri ->
            if (name != null) {
                saveString(name, uri.toString())
            }
            on_selected.invoke(uri)
        }
        this.openFileLauncher.launch(arrayOf(mimeType, "*/*"))
    }

    private fun selectFolderURI(name: String, on_selected: (Uri) -> Unit) {
        theOpenLaucherCallback = { uri ->
            if (name != null) {
                saveString(name, uri.toString())
            }

            on_selected.invoke(uri)
        }
        this.openDirectoryLauncher.launch(null)
    }

    private fun saveString(name: String, value: String) {
        // ⬇️ SALVA O URI NO SharedPreferences
        getSharedPreferences("prefs", Context.MODE_PRIVATE)
            .edit()
            .putString(name, value)
            .apply()
    }

    fun uriStatus(uriString: String?, callback: ((uri: Uri) -> Unit)? = null): Uri? {
        if (uriString != null) {
            Log.i(TAG, "onCreate: 'uriSalva' NÃO é nulo, tentando parsear.")
            try {
                Log.i(TAG, "onCreate: Chamando Uri.parse com: '$uriString'") // LOG IMPORTANTE
                val uri = Uri.parse(uriString) // Esta é a linha mais provável
                Log.i(TAG, "onCreate: Uri.parse bem-sucedido: '$uri'")

                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                callback?.invoke(uri)
                return uri
                //carregarArquivoHtml(uri)
            } catch (e: Exception) {
                // Se for uma NullPointerException aqui, o catch genérico pode pegá-la.
                // Verifique se 'e' é uma NullPointerException.
                Log.e(
                    TAG,
                    "Erro ao restaurar URI salva: Tipo=${e::class.java.simpleName}, Msg=${e.message}",
                    e
                )
            }
        } else {
            Log.i(TAG, "onCreate: 'uriSalva' é nulo, pulando o bloco de restauração.")
        }
        return null
    }
}