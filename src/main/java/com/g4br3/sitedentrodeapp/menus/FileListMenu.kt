package com.g4br3.sitedentrodeapp.menus

import android.content.Context.MODE_PRIVATE
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import com.g4br3.sitedentrodeapp.FilesListActivity
import com.g4br3.sitedentrodeapp.GitOperations
import com.g4br3.sitedentrodeapp.R
import com.g4br3.sitedentrodeapp.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FileListMenu(
    val activity: FilesListActivity,
    var github: GitOperations,
    val limparPastaCallback: () -> Unit,
    val atualizarCallback: () -> Unit,
) {

    val tokenManager = TokenManager(activity)
    val prefs = activity.getSharedPreferences("auth", MODE_PRIVATE)

    // No onCreate da sua Activity ou no onViewCreated do Fragment:
    init {

        val btnMenu = activity.findViewById<ImageButton>(R.id.btnMenu) // ou use view binding

        btnMenu.setOnClickListener { view ->
            val popup = PopupMenu(activity, view)
            popup.menuInflater.inflate(R.menu.file_list_menu, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {

                    R.id.action_atualizar -> {
                        atualizarDados()
                        true
                    }

                    R.id.action_limpar_pasta -> {
                        limparPasta()
                        true
                    }

                    else -> false
                }
            }

            popup.show()
        }
    }

    // Funções de exemplo para cada ação
    private fun receberDados() {
        // TODO: implemente a lógica de recebimento
        Toast.makeText(activity, "Recebendo dados...", Toast.LENGTH_SHORT).show()
    }

    private fun atualizarDados() {
        // TODO: implemente a lógica de atualização
        val owner = prefs.getString("owner_Name", null)
        val repo = prefs.getString("repo_Name", null)
        val token = tokenManager.getAccessToken()
        // ============================================================================================
        // ============================================================================================
        if (owner.isNullOrBlank() || repo.isNullOrBlank()) {
            Toast.makeText(activity, "Owner ou repo não configurado", Toast.LENGTH_LONG).show()
            return
        }

        if (token.isNullOrBlank()) {
            Toast.makeText(
                activity,
                "Token não configurado. Salve um token antes de atualizar.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        // Executa o pull em background usando coroutines
        CoroutineScope(Dispatchers.IO).launch {
            val result = github.pullRepositoryWithToken(owner, repo, token)
            withContext(Dispatchers.Main) {
                result.onSuccess {
                    Toast.makeText(activity, "Pull executado com sucesso", Toast.LENGTH_SHORT)
                        .show()
                }.onFailure { err ->
                    Toast.makeText(activity, "Erro no pull: ${err.message}", Toast.LENGTH_LONG)
                        .show()
                    Log.d("FileListMenu", "Erro no pull: ${err.message}")
                }

                // Aguarda um pouco para refletir mudanças (se necessário) e atualiza a UI
                Handler(Looper.getMainLooper()).postDelayed({
                    atualizarCallback()
                }, 500)
            }
        }
        //  ==========================================================================================
        // ============================================================================================
        Toast.makeText(activity, "Atualizando...", Toast.LENGTH_SHORT).show()
    }

    private fun limparPasta() {
        limparPastaCallback.invoke()
        // TODO: implemente a lógica de limpeza da pasta
        Toast.makeText(activity, "Pasta limpa!", Toast.LENGTH_SHORT).show()
    }
}