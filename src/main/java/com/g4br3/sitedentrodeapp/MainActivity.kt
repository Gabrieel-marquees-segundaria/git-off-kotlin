package com.g4br3.sitedentrodeapp

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE

import android.content.Intent
import android.net.Uri

import android.os.Bundle
import android.util.Log
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import com.g4br3.sitedentrodeapp.activitys.Callbacks

import com.g4br3.sitedentrodeapp.activitys.Clone


class MainActivity : AppCompatActivity() {

    private lateinit var clone: Clone
    private lateinit var dialog: LoadingDialog


    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dialog = LoadingDialog(this )
        val cloneCallbacks = Callbacks({
            dialog.show()
        }, {
            dialog.hide()
        })
        clone = Clone(this, cloneCallbacks)
            .setup(savedInstanceState)



        // Segurança: listFiles() pode retornar null, então protegemos contra NPE
        val reposDir = clone.gitOperations.reposDir
        val files = reposDir.listFiles()
        Log.d("Main", "reposDir=${reposDir.absolutePath} exists=${reposDir.exists()} files=${files?.map { it.name } ?: "null"}")

        if (files == null || files.isEmpty()) {
            Log.d("main", "repo nao encontrada")
        } else {
            Log.d("main", "repo encontrada")
            val intent = Intent(this@MainActivity, FilesListActivity::class.java)
            try {
                Log.d("Main", "Tentando iniciar FilesListActivity")
                startActivity(intent)
                Log.d("Main", "startActivity chamado com sucesso")
                finish()
            } catch (e: Exception) {
                Log.e("Main", "Erro ao iniciar FilesListActivity", e)
            }
        }




        val tvTokenInfoLink = findViewById<TextView>(R.id.tvTokenInfoLink)

        tvTokenInfoLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/settings/tokens"))
            startActivity(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleAuthIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        handleAuthIntent(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("expected_state", clone.expectedState)
        getSharedPreferences("auth", MODE_PRIVATE).edit()
            .putString("expected_state", clone.expectedState).apply()
    }

    private fun handleAuthIntent(intent: Intent?) {
        clone.handleAuthIntent(intent)
    }

}