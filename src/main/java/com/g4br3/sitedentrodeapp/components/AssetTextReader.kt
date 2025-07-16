package com.g4br3.sitedentrodeapp.components


import android.content.Context
import android.util.Log

class AssetTextReader(private val context: Context) {

    fun lerConteudosDaPasta(pasta: String): List<String> {
        return try {
            val arquivos = context.assets.list(pasta) ?: return emptyList()

            arquivos.map { nomeArquivo ->
                //println(nomeArquivo)
                val caminho = if (pasta.isNotEmpty()) "$pasta/$nomeArquivo" else nomeArquivo
                context.assets.open(caminho).bufferedReader().use { it.readText() }
            }
        } catch (e: Exception) {
        Log.e("AssetTextReader", "Erro ao ler arquivos de $pasta", e)
        emptyList()
    }

}

    fun JuncaoDeConteudosDaPasta(pasta: String): Any {
        val contents: StringBuilder = StringBuilder(" ")
        val arquivos = context.assets.list(pasta)
        arquivos?.forEach { nomeArquivo ->
            println(nomeArquivo)
            val caminho = if (pasta.isNotEmpty()) "$pasta/$nomeArquivo" else nomeArquivo
            val text: String = context.assets.open(caminho).bufferedReader().use { it.readText() }
            contents.append("\n\n$text")

        }
        return contents.toString()
    }
}
