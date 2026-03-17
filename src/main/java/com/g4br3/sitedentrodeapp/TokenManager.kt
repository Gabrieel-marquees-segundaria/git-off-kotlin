package com.g4br3.sitedentrodeapp

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Gerencia o armazenamento seguro do Access Token do GitHub
 */
class TokenManager(private val context: Context) {

    private val masterKey: MasterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedSharedPrefs: EncryptedSharedPreferences = EncryptedSharedPreferences.create(
        context,
        "github_tokens",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    ) as EncryptedSharedPreferences

    /**
     * Salva o access token de forma segura
     */
    fun saveAccessToken(token: String): Result<Unit> {
        return try {
            if (token.isBlank()) {
                return Result.failure(IllegalArgumentException("Token não pode estar vazio"))
            }
            encryptedSharedPrefs.edit().putString(KEY_ACCESS_TOKEN, token).apply()
            Log.d("TokenManager", "Access token salvo com sucesso")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("TokenManager", "Erro ao salvar token", e)
            Result.failure(e)
        }
    }

    /**
     * Recupera o access token salvo
     */
    fun getAccessToken(): String? {
        return try {
            val token = encryptedSharedPrefs.getString(KEY_ACCESS_TOKEN, null)
            Log.d("TokenManager", "Token recuperado: ${if (token != null) "***" else "null"}")
            token
        } catch (e: Exception) {
            Log.e("TokenManager", "Erro ao recuperar token", e)
            null
        }
    }

    /**
     * Verifica se há um token salvo
     */
    fun hasToken(): Boolean {
        return getAccessToken() != null
    }

    /**
     * Remove o access token
     */
    fun deleteAccessToken(): Result<Unit> {
        return try {
            encryptedSharedPrefs.edit().remove(KEY_ACCESS_TOKEN).apply()
            Log.d("TokenManager", "Access token removido")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("TokenManager", "Erro ao remover token", e)
            Result.failure(e)
        }
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "github_access_token"
    }
}
