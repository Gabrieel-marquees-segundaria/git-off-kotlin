package com.g4br3.sitedentrodeapp

// removed unused import
// removed unused import
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.g4br3.sitedentrodeapp.fileIO.GroupBuild
import com.g4br3.sitedentrodeapp.fileIO.History
import com.g4br3.sitedentrodeapp.fileIO.RepoIo
import com.g4br3.sitedentrodeapp.fileIO.folderSize
import com.g4br3.sitedentrodeapp.menus.FileListMenu
import com.g4br3.sitedentrodeapp.popup.showFileInfo
import com.g4br3.sitedentrodeapp.recicleView.DirType
import com.g4br3.sitedentrodeapp.recicleView.FileType
import com.g4br3.sitedentrodeapp.recicleView.FilesAdapter
import com.g4br3.sitedentrodeapp.recicleView.Folder
import com.g4br3.sitedentrodeapp.recicleView.HistotyAdapter
import com.g4br3.sitedentrodeapp.recicleView.getIcon
import com.g4br3.sitedentrodeapp.utils.ApkInstall
import com.g4br3.sitedentrodeapp.utils.RequestApk
import java.io.File
import java.time.LocalDate
import com.g4br3.sitedentrodeapp.recicleView.File as ItemFile


class FilesListActivity : AppCompatActivity() {
    lateinit var fileItems: MutableList<ItemFile>
    var backSpaceItems: MutableList<MutableList<ItemFile>> = mutableListOf()
    lateinit var adapter: FilesAdapter

    lateinit var historyItems: MutableList<ItemFile>

    lateinit var historyAdapter: HistotyAdapter
    lateinit var reposDir: File
    lateinit var sharedPref: SharedPreferences
    private var downloadReceiver: BroadcastReceiver? = null
    private var currentDownloadId: Long? = null
    private lateinit var pathBar: TextView
    private lateinit var history: History
    private lateinit var ropoAbsolutePath: String
private var oldList: MutableList<Any> = mutableListOf<Any>()
    fun setTextFromTopBar(text: String) {
        val textFormated = text.replace(ropoAbsolutePath, "")
        pathBar.text = textFormated
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("WrongViewCast", "MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        // Usar o layout da lista de arquivos
        setContentView(R.layout.activity_files_list)


        sharedPref = getPreferences(MODE_PRIVATE)
        history = History(this)
        pathBar = findViewById<TextView>(R.id.pathBar)
        ropoAbsolutePath = RepoIo.FILE(this).listFiles()[0].absolutePath ?: ""
        getCurrentVersion()
        start_python()
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

        findViewById<ImageButton>(R.id.btnBackPrass).setOnClickListener {
            btnBackPress({ }, false)
        }
        val historyReciclerView = findViewById<RecyclerView>(R.id.rvRecentes)
        historyReciclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        historyItems = mutableListOf()



        historyAdapter = HistotyAdapter(historyItems, {
openFile(it)
        })

        historyReciclerView.adapter = historyAdapter
        history.getHistory().forEach {
            Log.d(history.key+"time",it.value.toString())
            updateHistory(it.key)
        }



        val recyclerView = findViewById<RecyclerView>(R.id.rvArquivos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Use a mutable list so we can add items dynamically
        fileItems = mutableListOf()

        // Make the adapter variable assignable so the click lambda can call notifyDataSetChanged()
        // Fix: use a properly-formed lambda and close the isType(...) call.
        // Use trailing lambda syntax for clarity.
        adapter = FilesAdapter(fileItems,{ fileData ->
            if (fileData.type.isType(DirType())) {
                // Directory clicked — implement navigation if needed
                Log.d("FilesList", "Directory clicked: ${fileData.path}")
                // IMPORTANT: do not reassign fileItems (adapter keeps the original reference).
                // Clear the list and add new items instead.
                salvarEstado(fileData.FILE, sharedPref)
                setTextFromTopBar(fileData.path)
                val children = fileData.FILE.listFiles(true) ?: listOf()
                NewRecycleViewFiles(children.asIterable())


            } else {
                openFile(fileData)
            }
        })
        {
            showFileInfo(this,it)
        }
        recyclerView.adapter = adapter

        // Inicializa GitOperations diretamente (não chamar Clone.setup() aqui,
        // pois ele troca o contentView para a tela de configuração)
        val gitOperations = GitOperations(this)

        reposDir = gitOperations.reposDir
        val filePath = pegarEstado(sharedPref) ?: reposDir.absolutePath
        val fileState = File(filePath)
        setTextFromTopBar(fileState.name)
        Log.d(
            "FilesList",
            "reposDir=${filePath} exists=${fileState.exists()}"
        )
        var files = fileState.listFiles(true) //.sortedBy { it.isFile }
        Log.d(
            "FilesList",
            "reposDir=${reposDir.absolutePath} exists=${reposDir.exists()} files=${files?.map { it.name } ?: "null"}")

        // Atualiza contador de arquivos na UI
        val tvContador = findViewById<TextView>(R.id.tvContador)
        tvContador?.text = "${files?.size ?: 0} itens"

        // Mostrar estado vazio se não houver arquivos
        val emptyState = findViewById<LinearLayout>(R.id.emptyState)
        if (files == null || files.isEmpty()) {
            emptyState?.visibility = View.VISIBLE
        } else {
            emptyState?.visibility = View.GONE
            // Logar nomes para debug
            // choose the first repo found (or the last, as before). Safer to pick first
            val repo: File? = files.firstOrNull()
            if (repo != null) {
                Log.d("FilesList", "using repo: ${repo.name}")

                for (item in files) {

                    fileItems.add(
                        addItemFile(item)
                    )
                    oldList.add(addItemFile((item)))
                }

                // Notify adapter that data changed
                adapter.notifyDataSetChanged()
            } else {
                Log.d("FilesList", "No repo directory found inside reposDir")
            }
        }


// No onCreate
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                btnBackPress(
                    {
                        isEnabled = false // Desativa este callback
                    }
                )
            }
        }
        this.onBackPressedDispatcher.addCallback(this, callback)


        FileListMenu(this, gitOperations, {

            val deleted = reposDir.deleteRecursively()
            if (deleted) {
                println("Pasta deletada com sucesso.")
                val intent = Intent(this@FilesListActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else println("Falha ao deletar a pasta.")
        }) {
            val estado = pegarEstado(sharedPref) ?: reposDir.absolutePath
            val fileEstado = File(estado)
            val childrens = fileEstado.listFiles(true)
            if (childrens != null) {
                NewRecycleViewFiles(childrens.asIterable())
            }
        }


        pegarLista_D_Estados(sharedPref)


        fun findByGroup(sufix: String) {
            Log.d("GroupBuild", oldList.toString())
            fileItems.clear()
            for (file in oldList) {
                val name = when (file) {
                    is File -> file.name
                    is ItemFile -> file.name
                    else -> ""
                }
                if (name.endsWith(sufix)) {
                    fileItems.add(addItemFile(file))
                }
            }
            adapter.notifyDataSetChanged()
        }

        fun defaultGroupCallback(sufixList: List<String>) {
            Log.d("GroupBuild", oldList.toString())
            fileItems.clear()
    var cont = 0
            for (file in oldList) {
                val name = when (file) {
                    is File -> file.name
                    is ItemFile -> file.name
                    else -> ""
                }
                if (sufixList.any { name.endsWith(it) }) {
                    fileItems.add(addItemFile(file))
                    cont = cont + 1
                }
            }
            adapter.notifyDataSetChanged()
            if (cont <= 0) {
                emptyState?.visibility = View.VISIBLE
            }
            else {
                emptyState?.visibility = View.GONE
            }
        }
        GroupBuild(this@FilesListActivity)
            .all {
                fileItems.clear()
                emptyState?.visibility = View.GONE
                Log.d("group", oldList.toString())
                for (file in oldList) {
                    fileItems.add(addItemFile(file))
                }
                adapter.notifyDataSetChanged()
            }
            .audios {
                defaultGroupCallback(listOf(".mp3"))
            }
            .images {
                defaultGroupCallback(listOf(".png", ".jpg", "webp", "svg"))


            }
            .documentos {
//                findByGroup(".txt")

                defaultGroupCallback(listOf(".txt", ".md"))
            }
            .videos {
                defaultGroupCallback(listOf(".mp4", ".mkv"))
            }
    }
    fun openFile(fileData: ItemFile){
        Log.d("FilesList", "File clicked: ${fileData.path}")
        history.setHistory(fileData.path)
        val intent = Intent(this@FilesListActivity, FileActivity::class.java).apply {
            putExtra("titulo", fileData.name)
            putExtra("file", fileData.path)
            putExtra("http", "http://localhost:8080${fileData.path.replace(ropoAbsolutePath,"")}")
            //  putExtra("url", "https://topanimes.net/")
        }
        try {
            Log.d("Main", "Tentando iniciar FilesListActivity")
            startActivity(intent)
            Log.d("Main", "startActivity chamado com sucesso")
            finish()
        } catch (e: Exception) {
            Log.e("Main", "Erro ao iniciar FilesListActivity", e)
        }
    }
    fun start_python(){
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
        val py = Python.getInstance()
        py.getModule("server")
            .callAttr("start_server", ropoAbsolutePath,8080)
    }
    /**
     * @param item is File or ItemFile
     */
    fun addItemFile(item: Any): ItemFile {
        when (item) {
            is File -> {
                return ItemFile(
                    item.name,
                    item.absolutePath,
                    if (item.isDirectory) folderSize(item) else item.length()  ,
                    createAt = item.lastModified(),
                    type = if (item.isDirectory) DirType() else FileType(),
                    icon = if (item.isDirectory) Folder() else getIcon(item.name),
                    FILE = item
                )
            }

            is ItemFile -> {
                return item
            }

        }
        TODO("item nao corresponde ao esperado")
    }

    fun updateHistory(file: String) {

        val path = file
        val name = path.split("/")[path.split("/").size - 1]
        val fileIo = File(path)
        Log.d("history", name)
        val item = ItemFile(
            name,
            path,
            fileIo.length(),
            fileIo.lastModified(),
            type = FileType(),
            icon = getIcon(name),
            FILE = fileIo
        )
        historyItems.add(item)
        Log.d("history", item.toString())
        historyAdapter.notifyDataSetChanged()
    }

    fun btnBackPress(isEnabledFun: () -> Unit, closeApp: Boolean = true) {
        // Faça sua ação aqui (ex: mostrar um diálogo)
        // Se quiser fechar a activity: finish()
        Log.d("FilesList", "back pressd ${backSpaceItems.size}")
        if (backSpaceItems.size > 0) {
            val index = backSpaceItems.size - 1
            val valorAnterior = backSpaceItems[index]
            // remove the saved snapshot from the stack
            backSpaceItems.removeAt(index)
            val index1 = valorAnterior[0].FILE.parentFile
            if (index1 != null) {
                Log.d("FilesList", index1.absolutePath)
                salvarEstado(index1, sharedPref)
            }
            // valorAnterior is a MutableList<ItemFile> (a snapshot) — pass it directly
            updateRecycleView(valorAnterior)

        } else {
            // Ou simplesmente:
            if (!closeApp) return

            isEnabledFun.invoke()
            // Dispatch a back press to the OnBackPressedDispatcher so default behavior runs
            this@FilesListActivity.onBackPressedDispatcher.onBackPressed()
        }
    }

    fun castFileIOFromTextTopBar(files: Iterable<Any>) {
        val indexZero = files.toList()[0]

        when (indexZero) {
            is File -> {
                val father = indexZero.parentFile.path
                setTextFromTopBar(father)
            }

            is ItemFile -> {
                val father = indexZero.FILE.parentFile.path
                setTextFromTopBar(father)
            }
        }
    }

    /**
     * @param files Iterable<File> or Iterable<ItemFile>
     */
    fun NewRecycleViewFiles(files: Iterable<File>) {
        // Save a snapshot (copy) of the current items so back navigation restores exactly
        backSpaceItems.add(ArrayList(fileItems))

        updateRecycleView(files)

    }

    /**
     * @param children Iterable<File> or Iterable<ItemFile>
     */
    fun updateRecycleView(children: Iterable<Any>) {
        fileItems.clear()
        oldList.clear()

        castFileIOFromTextTopBar(children)
        for (file in children) {
            Log.d("FilesList", file.toString())
            fileItems.add(addItemFile(file))
            oldList.add(addItemFile((file)))
        }
        // Update UI elements (counter and empty state). Use findViewById because
        // those local variables may not be captured yet depending on declaration order.
        val tvContador = findViewById<TextView>(R.id.tvContador)
        tvContador?.text = "${fileItems.size} itens"
        val emptyState = findViewById<LinearLayout>(R.id.emptyState)
        emptyState?.visibility = if (fileItems.isEmpty()) View.VISIBLE else View.GONE

        // Notify the adapter that the data changed so the RecyclerView updates.
        adapter.notifyDataSetChanged()
    }



    val estadoAtual = "Repo-estado-atual8"
    fun salvarEstado(path: File, sharedPref: SharedPreferences) {
        Log.d("FilesList", "estado atual: ${path.absolutePath}")
        with(sharedPref.edit()) {
            putString(estadoAtual, path.absolutePath)
            apply() // ou commit()
        }
    }

    fun pegarEstado(sharedPref: SharedPreferences): String? {
        return sharedPref.getString(estadoAtual, null)
    }

    fun pegarLista_D_Estados(sharedPref: SharedPreferences) {
        var pathAtual = pegarEstado(sharedPref)
        var repo = reposDir.absolutePath
        if (pathAtual != null) {
            val dirs = pathAtual.removePrefix(repo).split("/")
            var tempDir = repo
            var cont = 0
            for (dir in dirs) {

                Log.d("FilesList", dir)
                if (!tempDir.endsWith("/")) tempDir += "/"
                tempDir += dir

                Log.d("FilesList", "$tempDir $cont")
                var tempDirFILE = File(tempDir)
                if (tempDirFILE.exists() && cont > 0 && cont < dirs.size - 1) {
                    var itemsFiles = tempDirFILE.listFiles(true).map { addItemFile(it) }
                    backSpaceItems.add(itemsFiles as MutableList<ItemFile>)
                }
                cont += 1
            }

        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getData(): String {
        val hoje = LocalDate.now()
        val dia = hoje.dayOfMonth
        val mes = hoje.monthValue
        val ano = hoje.year
        return "$dia/$mes/$ano"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentVersion() {
        val state = sharedPref.getString("datetimeupdate", "").toString()

        if (state == getData()) return
        sharedPref.edit().putString("datetimeupdate", getData()).apply()
        val requestApk = RequestApk()
        val currentVersionName = "Apk-version-5"
        var currentVersion = sharedPref.getString(currentVersionName, "1.0").toString()
        Log.d("FilesList", "currentVercion: " + currentVersion.toString())

        RequestApk.getLatestReleaseApkUrl(currentVersion = currentVersion) { hasUpdate, latest, apkUrl, error ->
            runOnUiThread {

                if (error != null) {
                    Toast.makeText(this, "Erro: $error", Toast.LENGTH_LONG).show()
                    return@runOnUiThread
                }

                if (!hasUpdate) {
                    Toast.makeText(
                        this,
                        "App já está atualizado (última: $latest)",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@runOnUiThread
                }

                if (apkUrl.isNullOrBlank()) {
                    Toast.makeText(
                        this,
                        "Nova versão $latest, mas nenhum APK disponível.",
                        Toast.LENGTH_LONG
                    ).show()
                    return@runOnUiThread
                }

                // Register receiver on application context and store it
                downloadReceiver =
                    RequestApk.downloadApkReceiver(this) { id: Long, localUri: Uri? ->
                        runOnUiThread {
                            Log.d("FilesList", "Download complete id=$id localUri=$localUri")
                            if (id == currentDownloadId) {
                                if (localUri != null) {
                                    ApkInstall().installAPK(this, localUri)
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Download finalizado, mas arquivo não está disponível localmente.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    }

                // Inicia download e guarda o id
                if (latest != currentVersion) {
                    currentDownloadId = RequestApk.downloadApk(this, apkUrl, "gitOffAppKotlin.apk")
                    Log.d("FilesList", "Started download with id=$currentDownloadId")
                    sharedPref.edit().putString(currentVersionName, latest).apply()
                }
            }
        }

        RequestApk.getVersion(currentVersion = "v1.0.0") { hasUpdate, latest, error ->
            runOnUiThread {
                when {
                    error != null -> Toast.makeText(this, "Erro: ${error}", Toast.LENGTH_LONG)
                        .show()

                    hasUpdate -> Toast.makeText(this, "Nova versão: $latest", Toast.LENGTH_LONG)
                        .show()

                    else -> Toast.makeText(this, "Atualizado (última: $latest)", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        // Cleanup receiver if registered
        try {
            if (downloadReceiver != null) {
                applicationContext.unregisterReceiver(downloadReceiver)
                downloadReceiver = null
            }
        } catch (e: Exception) {
            // ignore
        }
    }
}

private fun File.listFiles(formaterFromDir: Boolean): List<File> {
 if (formaterFromDir ==true) return  this.listFiles().sortedBy { it.isFile }
    return this.listFiles() as List<File>
}


