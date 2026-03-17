package com.g4br3.sitedentrodeapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
// removed unused import
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.g4br3.sitedentrodeapp.recicleView.Default
import com.g4br3.sitedentrodeapp.recicleView.DirType
import com.g4br3.sitedentrodeapp.recicleView.FileType
import com.g4br3.sitedentrodeapp.recicleView.FilesAdapter
import org.bouncycastle.asn1.iana.IANAObjectIdentifiers.directory
import org.eclipse.jgit.internal.storage.file.FileSnapshot.save
// removed unused import
import java.io.File
import com.g4br3.sitedentrodeapp.recicleView.File as ItemFile


class FilesListActivity : AppCompatActivity() {
    lateinit var fileItems: MutableList<ItemFile>
    var backSpaceItems: MutableList<MutableList<ItemFile>> = mutableListOf()
    lateinit var adapter: FilesAdapter
    lateinit var reposDir: File

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Salvar String
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
//        with (sharedPref.edit()) {
//            putString("chave_usuario", "texto_salvo")
//            apply() // ou commit()
//        }
//
//// Recuperar String
//        val textoRecuperado = sharedPref.getString("chave_usuario", "valor_padrao")


        // Usar o layout da lista de arquivos
        setContentView(R.layout.activity_files_list)

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



        val recyclerView = findViewById<RecyclerView>(R.id.rvArquivos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Use a mutable list so we can add items dynamically
        fileItems = mutableListOf()
        // Make the adapter variable assignable so the click lambda can call notifyDataSetChanged()
        // Fix: use a properly-formed lambda and close the isType(...) call.
        // Use trailing lambda syntax for clarity.
        adapter = FilesAdapter(fileItems) { fileData ->
            if (fileData.type.isType(DirType())) {
                // Directory clicked — implement navigation if needed
                Log.d("FilesList", "Directory clicked: ${fileData.path}")
                // IMPORTANT: do not reassign fileItems (adapter keeps the original reference).
                // Clear the list and add new items instead.
                salvarEstado(fileData.FILE, sharedPref)
                val children = fileData.FILE.listFiles() ?: emptyArray()
                NewRecycleViewFiles(children.asIterable())

            } else {
                Log.d("FilesList", "File clicked: ${fileData.path}")
                val intent = Intent(this@FilesListActivity, FileActivity::class.java).apply {
                    putExtra("titulo", fileData.name)
                    putExtra("file", fileData.path)
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
        }
        recyclerView.adapter = adapter

        // Inicializa GitOperations diretamente (não chamar Clone.setup() aqui,
        // pois ele troca o contentView para a tela de configuração)
        val gitOperations = GitOperations(this)

        reposDir = gitOperations.reposDir
        val filePath = pegarEstado(sharedPref) ?: reposDir.absolutePath
        val fileState = File(filePath)
        Log.d(
            "FilesList",
            "reposDir=${filePath} exists=${fileState.exists()}"
        )
        val files = fileState.listFiles()
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
//                val repoFiles = repo.listFiles() ?: emptyArray()
                for (item in files) {

                    fileItems.add(
                        addItemFile(item)
                    )
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

                    isEnabled = false // Desativa este callback
                    // Dispatch a back press to the OnBackPressedDispatcher so default behavior runs
                    this@FilesListActivity.onBackPressedDispatcher.onBackPressed()
                }
            }
        }
        this.onBackPressedDispatcher.addCallback(this, callback)



        findViewById<ImageButton>(R.id.btnMenu).setOnClickListener {
            // limpar repo para clonar outro, temp, se tiver mais fun transformar em menu


            val deleted = reposDir.deleteRecursively()
            if (deleted) {
                println("Pasta deletada com sucesso.")
                val intent = Intent(this@FilesListActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else println("Falha ao deletar a pasta.")

        }
        pegarLista_D_Estados(sharedPref)
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
                    item.length(),
                    createAt = item.lastModified(),
                    type = if (item.isDirectory) DirType() else FileType(),
                    icon = Default(),
                    FILE = item
                )
            }

            is ItemFile -> {
                return item
            }

        }
        TODO("item nao corresponde ao esperado")
    }

    /**
     * @param files Iterable<File> or Iterable<ItemFile>
     */
    fun NewRecycleViewFiles(files: Iterable<Any>) {
        // Save a snapshot (copy) of the current items so back navigation restores exactly
        backSpaceItems.add(ArrayList(fileItems))
        updateRecycleView(files)

    }

    /**
     * @param children Iterable<File> or Iterable<ItemFile>
     */
    fun updateRecycleView(children: Iterable<Any>) {
        fileItems.clear()
        for (file in children) {
            Log.d("FilesList", file.toString())
            fileItems.add(addItemFile(file))
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
                if (tempDirFILE.exists() && cont > 0  && cont < dirs.size -1)  {
                    var itemsFiles = tempDirFILE.listFiles().map { addItemFile(it) }
                    backSpaceItems.add(itemsFiles as MutableList<ItemFile>)
                }
                cont += 1
            }

        }


    }
}


