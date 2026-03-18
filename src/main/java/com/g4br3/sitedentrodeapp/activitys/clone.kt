package com.g4br3.sitedentrodeapp.activitys


import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import androidx.core.content.ContextCompat.startActivity
import com.g4br3.sitedentrodeapp.GitOperations
import com.g4br3.sitedentrodeapp.MainActivity
import com.g4br3.sitedentrodeapp.R
import com.g4br3.sitedentrodeapp.TokenManager
import androidx.core.content.edit


class Callbacks(val start: ()-> Unit,val finish: ()-> Unit)

class Clone(var activity: AppCompatActivity,var cloneStatus: Callbacks= Callbacks({},{})) {


    public lateinit var gitOperations: GitOperations
    private lateinit var tokenManager: TokenManager
    var expectedState: String? = null
    fun setup(savedInstanceState: Bundle?) : Clone{
        tokenManager = TokenManager(activity)
        val prefs =activity. getSharedPreferences("auth", MODE_PRIVATE)
        expectedState = savedInstanceState?.getString("expected_state")
            ?: prefs.getString("expected_state", null)
                    ?: java.util.UUID.randomUUID().toString()
        Log.d("GITHUB", "expectedState=$expectedState")
        // Persiste o state para sobreviver a morte do processo
        prefs.edit().putString("expected_state", expectedState).apply()

        val clientId = "Ov23li4YMY6XsBKMp3n5"
        val redirectUrl = "fazerlogincomgithubnogitoff://callback"
        val authUrl = "https://github.com/login/oauth/authorize?client_id=$clientId&scope=repo&redirect_uri=$redirectUrl&state=$expectedState"

        val authIntent = Intent(Intent.ACTION_VIEW, Uri.parse(authUrl))
        /*   setContent {
              Column(
                  verticalArrangement = Arrangement.spacedBy(8.dp) // Espaço vertical de 8dp entre os itens
              ) {
                  Text("Item 1")
                  Text("Item 2")
                  Text("Item 3")
  
                  Button(onClick={
                      Log.d("GITHUB","Button clicked")
                      startActivity(authIntent)
                  }) {
                      Text("Login with github", fontSize = 30.sp)
                  }
              }
          }
  */
      activity.  setContentView(R.layout.config_git_hub_acess)
        val btnLoginWithGitHub: MaterialButton =activity. findViewById(R.id.btnAuthGithub)
        btnLoginWithGitHub.setOnClickListener {
            Log.d("GITHUB","Button clicked")
            startActivity(activity, authIntent, savedInstanceState) //, savedInstanceState)
        }
        val statusTextView: MaterialTextView =activity. findViewById(R.id.tvStatus)
        statusTextView.text = "Expected state: $expectedState"

        // Inicializar GitOperations
        gitOperations = GitOperations(activity)

        // Configurar interface de Access Token
        val etAccessToken: TextInputEditText = activity.findViewById(R.id.etAccessToken)
        val btnSaveToken: MaterialButton = activity.findViewById(R.id.btnSaveToken)
        val tvTokenStatus: MaterialTextView = activity.findViewById(R.id.tvTokenStatus)

        // Atualizar status do token ao abrir a tela
        updateTokenStatus(tvTokenStatus)

        btnSaveToken.setOnClickListener {
            val token = etAccessToken.text.toString().trim()
            if (token.isEmpty()) {
                Toast.makeText(activity, "Digite um token válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            tokenManager.saveAccessToken(token).onSuccess {
                Toast.makeText(activity, "Token salvo com segurança!", Toast.LENGTH_SHORT).show()
                etAccessToken.text?.clear()
                updateTokenStatus(tvTokenStatus)
            }.onFailure { error ->
                Toast.makeText(activity, "Erro ao salvar token: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar interface de clone
        val etOwnerRepo: TextInputEditText = activity.findViewById(R.id.etOwnerRepo)
        val btnClonar: MaterialButton = activity.findViewById(R.id.btnClonar)
        val tvCloneResult: MaterialTextView = activity.findViewById(R.id.tvCloneResult)

        btnClonar.setOnClickListener {
            cloneStatus.start()
            val ownerRepo = etOwnerRepo.text.toString().trim()
            val token = tokenManager.getAccessToken()

            if (token == null) {
                Toast.makeText(activity, "Salve um access token primeiro!", Toast.LENGTH_SHORT).show()
                etAccessToken.requestFocus()
                return@setOnClickListener
            }

            if (ownerRepo.isEmpty() || !ownerRepo.contains("/")) {
                Toast.makeText(activity, "Use formato: dono/repositorio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val parts = ownerRepo.split("/")
            if (parts.size != 2) {
                Toast.makeText(activity, "Use formato: dono/repositorio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val owner = parts[0]
            prefs.edit { putString("owner_Name", owner) }
            val repo = parts[1]
            // Salvar corretamente o nome do repositório (antes salvava owner duas vezes)
            prefs.edit { putString("repo_Name", repo) }


            btnClonar.isEnabled = false
            tvCloneResult.text = "Clonando repositório..."

          activity.  lifecycleScope.launch(Dispatchers.IO) {
                val result = gitOperations.cloneRepositoryWithToken(owner, repo, token)
                activity.    lifecycleScope.launch(Dispatchers.Main) {
                    result.onSuccess { message ->
                        tvCloneResult.text = message
                        Toast.makeText(activity, "Repositório clonado!", Toast.LENGTH_SHORT).show()
                        etOwnerRepo.text?.clear()

                        // Navegar automaticamente para FilesListActivity após clone bem-sucedido
                        try {
                            cloneStatus.finish()
                            val intent = Intent(activity, Class.forName("com.g4br3.sitedentrodeapp.FilesListActivity"))
                            activity.startActivity(intent)
                        } catch (e: Exception) {
                            Log.e("Clone", "Erro ao iniciar FilesListActivity", e)
                        }

                    }.onFailure { error ->
                        tvCloneResult.text = "Erro: ${error.message}"
                        Toast.makeText(activity, "Erro: ${error.message}", Toast.LENGTH_LONG).show()
                    }
                    btnClonar.isEnabled = true
                }
            }
        }

        // Tratar caso o app tenha sido iniciado pelo deep link (cold start)
        handleAuthIntent(activity. intent)
        val btnTokenInfo  = activity.findViewById<ImageButton>(R.id.btnTokenInfo)
        btnTokenInfo.setOnClickListener {
            val info = activity.findViewById<LinearLayout>(R.id.layoutTokenInfo)
            if (info.visibility == View.GONE) {
                info.visibility = View.VISIBLE
            } else {
                info.visibility = View.GONE
            }
        }
return  this
    }

     fun handleAuthIntent(intent: Intent?) {
        val uri: Uri? = intent?.data
        if (uri == null) return
        Log.d("GITHUB", "Callback URI: $uri")
        val returnedState = uri.getQueryParameter("state") ?: intent?.getStringExtra("state")
        if (returnedState == null || returnedState != expectedState) {
            Log.w("GITHUB", "State mismatch or missing. expected=$expectedState returned=$returnedState")
            Toast.makeText(activity, "State mismatch in OAuth response", Toast.LENGTH_LONG).show()
            return
        }
        val code = uri.getQueryParameter("code")
        if (code != null) {
            Log.d("GITHUB", "Auth code: $code")
            Toast.makeText(activity, "Code received", Toast.LENGTH_SHORT).show()
            // Limpa o estado salvo (já recebido o code)
           activity. getSharedPreferences("auth", MODE_PRIVATE).edit().remove("expected_state").apply()
            // TODO: trocar o code por token (usar PKCE/AppAuth ou backend)
        } else {
            val error = uri.getQueryParameter("error")
            Log.w("GITHUB", "No code in callback, error=$error")
            Toast.makeText(activity, "Auth error: $error", Toast.LENGTH_LONG).show()
        }
    }

     fun updateTokenStatus(tvTokenStatus: MaterialTextView) {
        if (tokenManager.hasToken()) {
            tvTokenStatus.text = "✓ Token configurado e armazenado com segurança"
            tvTokenStatus.setTextColor(activity. getColor(android.R.color.holo_green_dark))
        } else {
            tvTokenStatus.text = "Token não configurado"
            tvTokenStatus.setTextColor(activity. getColor(android.R.color.darker_gray))
        }
    }
}