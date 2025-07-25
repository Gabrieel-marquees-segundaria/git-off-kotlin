package com.g4br3.sitedentrodeapp.fileManager

import android.net.Uri
import android.util.Log

import java.io.BufferedReader
import java.io.InputStreamReader
import  com.g4br3.sitedentrodeapp.fileManager.INIT

var TAG = INIT.TAG


class File() {
    var Content:  String? = null
   var  isLoaded = false

    /**
     * Carrega o conteúdo de um arquivo HTML selecionado.
     *
     * @param uri URI do arquivo HTML selecionado
     * @return true se carregou com sucesso, false caso contrário
     */
    fun carregarArquivo(uri: Uri, type:String="txt"): Boolean {
        Log.d(TAG, "carregarArquivo() iniciado para URI: $uri")

        return try {
            INIT.context?.contentResolver?.openInputStream(uri)?.use { inputStream ->
                val reader = BufferedReader(InputStreamReader(inputStream))
                val content = reader.readText()

                Log.d(TAG, "Conteúdo $type carregado com sucesso (${content.length} caracteres)")

                Content = content
                isLoaded = true
                true
            } ?: false
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao carregar arquivo ", e)
            false
        }
    }

}