package com.g4br3.sitedentrodeapp.utils

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import androidx.core.content.ContextCompat
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import android.util.Log
import java.io.File


class RequestApk {
    companion object {
        private const val LATEST_RELEASE_URL =
            "https://api.github.com/repos/Gabrieel-marquees-segundaria/git-off-kotlin/releases/latest"
        private const val TAG = "RequestApk"

        fun getVersion(
            currentVersion: String,
            client: OkHttpClient = OkHttpClient(),
            onResult: (hasUpdate: Boolean, latestVersion: String?, error: String?) -> Unit
        ) {
            val request = Request.Builder()
                .url(LATEST_RELEASE_URL)
                .header("Accept", "application/vnd.github+json")
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    onResult(false, null, e.message ?: "Falha na requisição")
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        if (!response.isSuccessful) {
                            val msg = "HTTP ${response.code}"
                            response.close()
                            onResult(false, null, msg)
                            return
                        }

                        val body = response.body?.string()
                        response.close()

                        if (body.isNullOrBlank()) {
                            onResult(false, null, "Resposta vazia")
                            return
                        }

                        val latest: String? = try {
                            JSONObject(body).optString("tag_name").takeIf { it.isNotBlank() }
                        } catch (e: Exception) {
                            Log.e(TAG, "Erro parseando JSON de release", e)
                            null
                        }

                        if (latest == null) {
                            onResult(false, null, "Campo tag_name não encontrado")
                            return
                        }

                        onResult(latest != currentVersion, latest, null)
                    } catch (e: Exception) {
                        try {
                            response.close()
                        } catch (_: Exception) {
                        }
                        onResult(false, null, e.message ?: "Erro processando resposta")
                    }
                }
            })
        }

        /**
         * Busca a última release e retorna também a URL de download do APK (primeiro asset com .apk)
         * Callback: (hasUpdate, latestVersion, apkUrl, error)
         */
        fun getLatestReleaseApkUrl(
            currentVersion: String,
            client: OkHttpClient = OkHttpClient(),
            onResult: (hasUpdate: Boolean, latestVersion: String?, apkUrl: String?, error: String?) -> Unit
        ) {
            val request = Request.Builder()
                .url(LATEST_RELEASE_URL)
                .header("Accept", "application/vnd.github+json")
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    onResult(false, null, null, e.message ?: "Falha na requisição")
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        if (!response.isSuccessful) {
                            val msg = "HTTP ${response.code}"
                            response.close()
                            onResult(false, null, null, msg)
                            return
                        }

                        val body = response.body?.string()
                        response.close()

                        if (body.isNullOrBlank()) {
                            onResult(false, null, null, "Resposta vazia")
                            return
                        }

                        val json = JSONObject(body)
                        val latest = json.optString("tag_name").takeIf { it.isNotBlank() }
                        Log.d(TAG, latest.toString())
                        var apkUrl: String? = null
                        try {

                            val assets = json.optJSONArray("assets")
                            if (assets != null) {
                                for (i in 0 until assets.length()) {
                                    val asset = assets.getJSONObject(i)
                                    val name = asset.optString("name")
                                    if (!name.isNullOrBlank() && name.endsWith(".apk", ignoreCase = true)) {
                                        apkUrl = asset.optString("browser_download_url").takeIf { it.isNotBlank() }
                                        Log.d(TAG, apkUrl.toString())
                                        if (apkUrl != null) break

                                    }
                                }
                            }
                        } catch (e: Exception) {
                            Log.w(TAG, "Erro lendo assets da release", e)
                        }

                        if (latest == null) {
                            onResult(false, null, apkUrl, "Campo tag_name não encontrado")
                            return
                        }

                        onResult(latest != currentVersion, latest, apkUrl, null)
                    } catch (e: Exception) {
                        try { response.close() } catch (_: Exception) {}
                        onResult(false, null, null, e.message ?: "Erro processando resposta")
                    }
                }
            })
        }


    /**
     * Inicia download do APK usando DownloadManager.
     * Retorna o ID do download (long) que pode ser usado para monitorar.
     */
    fun downloadApk(context: Context, url: String, fileName: String): Long {
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Baixando Atualização")
            .setDescription("O arquivo APK está sendo baixado...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            // Use app-specific external files dir to avoid scoped-storage issues
            //.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName)
            // ERRADO (Gera o erro java.lang.IllegalArgumentException)
            // CORRETO (Caminho público: /storage/emulated/0/Download/)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)
        if (file.exists()) {
            file.delete()
        }
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        return downloadManager.enqueue(request)
    }

    /**
     * Registra um BroadcastReceiver para ACTION_DOWNLOAD_COMPLETE.
     * Retorna o BroadcastReceiver para que o chamador possa desregistrá-lo quando apropriado.
     * O callback onComplete recebe o downloadId e a uri local (ou null se falhou).
     */
    fun downloadApkReceiver(
        context: Context,
        onComplete: (downloadId: Long, localUri: Uri?) -> Unit
    ): BroadcastReceiver {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                Log.d(TAG, "downloadApkReceiver onReceive, intent=$intent extras=${intent?.extras}")
                val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L) ?: -1L
                Log.d(TAG, "downloadApkReceiver received id=$id")
                if (id == -1L) return

                val dm = ctx?.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager ?: return
                val query = DownloadManager.Query().setFilterById(id)
                val cursor = dm.query(query)
                var localUri: Uri? = null
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        // Log all available columns for debugging
                        try {
                            for (i in 0 until cursor.columnCount) {
                                Log.d(TAG, "cursor col(${i})=${cursor.getColumnName(i)}")
                            }
                        } catch (_: Exception) {}

                        val statusIdx = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                        if (statusIdx >= 0) {
                            val status = cursor.getInt(statusIdx)
                            Log.d(TAG, "download status=$status")
                            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                // Try multiple columns which may contain the local URI/path depending on OEM/Android version
                                val possibleCols = listOf(DownloadManager.COLUMN_LOCAL_URI, "local_filename", DownloadManager.COLUMN_MEDIAPROVIDER_URI)
                                for (col in possibleCols) {
                                    val idx = try { cursor.getColumnIndex(col) } catch (_: Exception) { -1 }
                                    if (idx >= 0) {
                                        val uriString = cursor.getString(idx)
                                        Log.d(TAG, "found column $col -> $uriString")
                                        if (!uriString.isNullOrBlank()) {
                                            try { localUri = Uri.parse(uriString) } catch (_: Exception) { localUri = null }
                                            if (localUri != null) break
                                        }
                                    }
                                }
                            }
                        }
                    }
                } finally {
                    cursor?.close()
                    onComplete(id, localUri)
                }
            }
        }

        // Register the receiver on the application context so it survives Activity lifecycle.
        // Use simple registerReceiver to maximize compatibility across devices/OEMs.
        val appCtx = context.applicationContext
        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        try {
            ContextCompat.registerReceiver(
                appCtx,
                receiver,
                filter,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
            Log.d(TAG, "downloadApkReceiver registered on application context")
        } catch (e: Exception) {
            Log.w(TAG, "Failed to register receiver on application context", e)
            try {
                ContextCompat.registerReceiver(
                    context,
                    receiver,
                    filter,
                    ContextCompat.RECEIVER_NOT_EXPORTED
                )
                Log.d(TAG, "downloadApkReceiver registered on provided context")
            } catch (ex: Exception) {
                Log.e(TAG, "Failed to register receiver on any context", ex)
            }
        }
        return receiver
    }

}}