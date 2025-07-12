package com.g4br3.sitedentrodeapp.components

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.documentfile.provider.DocumentFile

/**
 * Classe utilitária para gerenciar armazenamento local usando SharedPreferences.
 *
 * @param context Contexto Android necessário para acessar SharedPreferences
 */
class StorageHelper(private val context: Context) {

    private val prefs = context.getSharedPreferences("meu_app_prefs", Context.MODE_PRIVATE)

    /**
     * Salva uma string no SharedPreferences.
     *
     * @param chave Chave para identificar o valor
     * @param valor String a ser salva
     */
    fun salvarString(chave: String, valor: String) {
        prefs.edit().putString(chave, valor).apply()
        println("Valor salvo com sucesso: $chave = $valor")
    }

    /**
     * Recupera uma string do SharedPreferences.
     *
     * @param chave Chave do valor a ser recuperado
     * @return String salva ou null se não encontrada
     */
    fun recuperarString(chave: String): String? {
        return prefs.getString(chave, null)
    }
}

/**
 * Utilitário para processamento de arquivos com funcionalidades granulares.
 *
 * Esta classe oferece uma abordagem mais modular para leitura e processamento
 * de arquivos, com funções específicas para cada responsabilidade.
 */
object FileProcessor {

    /**
     * Processa um arquivo localizado em uma pasta específica.
     *
     * @param selectedFolderUri URI da pasta base selecionada
     * @param caminhoRelativo Caminho relativo do arquivo dentro da pasta
     * @param context Contexto Android para operações de sistema
     * @return Conteúdo processado do arquivo ou mensagem de erro
     */
    fun processarArquivo(selectedFolderUri: Uri?, caminhoRelativo: String, context: Context): String {
        if (selectedFolderUri == null) return "URI da pasta não selecionada"

        val arquivo = localizarArquivoPorCaminho(selectedFolderUri, caminhoRelativo, context)
        return processarDocumentFile(arquivo, context)
    }

    /**
     * Processa um DocumentFile já localizado.
     *
     * @param arquivo DocumentFile a ser processado
     * @param context Contexto Android
     * @return Conteúdo processado ou mensagem de erro apropriada
     */
    private fun processarDocumentFile(arquivo: DocumentFile?, context: Context): String {
        return when {
            arquivo == null -> "Arquivo não encontrado"
            !arquivo.isFile -> "Não é um arquivo válido"
            else -> lerEEscapar(arquivo, context)
        }
    }

    /**
     * Coordena a leitura e escape do conteúdo de um arquivo válido.
     *
     * @param arquivo DocumentFile válido a ser lido
     * @param context Contexto Android
     * @return Conteúdo do arquivo com caracteres especiais escapados
     */
    private fun lerEEscapar(arquivo: DocumentFile, context: Context): String {
        val conteudo = lerArquivo(arquivo, context)
        return escaparTexto(conteudo)
    }

    /**
     * Lê o conteúdo de um arquivo com tratamento de exceções.
     *
     * @param arquivo DocumentFile a ser lido
     * @param context Contexto Android para acessar ContentResolver
     * @return Conteúdo do arquivo como String ou mensagem de erro
     */
    private fun lerArquivo(arquivo: DocumentFile, context: Context): String {
        return try {
            abrirELerStream(arquivo.uri, context)
        } catch (e: Exception) {
            "Erro ao ler arquivo: ${e.message}"
        }
    }

    /**
     * Abre um InputStream e lê todo o conteúdo.
     *
     * @param uri URI do arquivo a ser lido
     * @param context Contexto Android
     * @return Conteúdo completo do arquivo
     * @throws Exception Se houver erro ao abrir ou ler o arquivo
     */
    private fun abrirELerStream(uri: Uri, context: Context): String {
        return context.contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.bufferedReader().use { reader ->
                reader.readText()
            }
        } ?: "Erro: InputStream nulo"
    }

    /**
     * Percorre recursivamente a árvore DocumentFile para localizar um arquivo por caminho relativo.
     *
     * @param baseUri URI base da pasta
     * @param caminhoRelativo Caminho relativo do arquivo
     * @param context Contexto Android necessário para criar DocumentFile
     * @return DocumentFile encontrado ou null se não encontrado
     */
    private fun localizarArquivoPorCaminho(baseUri: Uri, caminhoRelativo: String, context: Context): DocumentFile? {
        val partes = caminhoRelativo.split("/")
        var atual: DocumentFile? = DocumentFile.fromTreeUri(context, baseUri)

        for (parte in partes) {
            atual = atual?.listFiles()?.firstOrNull { it.name == parte }
            if (atual == null) break
        }
        return atual
    }

    /**
     * Aplica todas as transformações de escape necessárias ao texto.
     *
     * @param texto Texto original a ser processado
     * @return Texto com todos os caracteres especiais escapados
     */
    private fun escaparTexto(texto: String): String {
        return texto
            .escaparBarras()
            .escaparAspas()
            .escaparQuebrasDeLinha()
            .removerCarriageReturn()
    }

    /**
     * Extension function para escapar barras invertidas.
     * Converte \ para \\
     *
     * @return String com barras invertidas escapadas
     */
    private fun String.escaparBarras(): String = replace("\\", "\\\\")

    /**
     * Extension function para escapar aspas simples.
     * Converte ' para \'
     *
     * @return String com aspas simples escapadas
     */
    private fun String.escaparAspas(): String = replace("'", "\\'")

    /**
     * Extension function para escapar quebras de linha.
     * Converte \n para \\n
     *
     * @return String com quebras de linha escapadas
     */
    private fun String.escaparQuebrasDeLinha(): String = replace("\n", "\\n")

    /**
     * Extension function para remover caracteres de retorno de carro.
     * Remove todos os \r do texto
     *
     * @return String sem caracteres de retorno de carro
     */
    private fun String.removerCarriageReturn(): String = replace("\r", "")
}

/**
 * Alternativa usando classe em vez de object, se preferir instanciar o FileProcessor.
 *
 * @param context Contexto Android para operações de sistema
 */
class FileProcessorInstanciavel(private val context: Context) {

    /**
     * Processa um arquivo localizado em uma pasta específica.
     *
     * @param selectedFolderUri URI da pasta base selecionada
     * @param caminhoRelativo Caminho relativo do arquivo dentro da pasta
     * @return Conteúdo processado do arquivo ou mensagem de erro
     */
    fun processarArquivo(selectedFolderUri: Uri?, caminhoRelativo: String): String {
        if (selectedFolderUri == null) return "URI da pasta não selecionada"

        val arquivo = localizarArquivoPorCaminho(selectedFolderUri, caminhoRelativo)
        return processarDocumentFile(arquivo)
    }

    /**
     * Percorre recursivamente a árvore DocumentFile para localizar um arquivo por caminho relativo.
     */
    private fun localizarArquivoPorCaminho(baseUri: Uri, caminhoRelativo: String): DocumentFile? {
        val partes = caminhoRelativo.split("/")
        var atual: DocumentFile? = DocumentFile.fromTreeUri(context, baseUri)

        for (parte in partes) {
            atual = atual?.listFiles()?.firstOrNull { it.name == parte }
            if (atual == null) break
        }
        return atual
    }

    /**
     * Processa um DocumentFile já localizado.
     */
    private fun processarDocumentFile(arquivo: DocumentFile?): String {
        return when {
            arquivo == null -> "Arquivo não encontrado"
            !arquivo.isFile -> "Não é um arquivo válido"
            else -> lerEEscapar(arquivo)
        }
    }

    /**
     * Coordena a leitura e escape do conteúdo de um arquivo válido.
     */
    private fun lerEEscapar(arquivo: DocumentFile): String {
        val conteudo = lerArquivo(arquivo)
        return escaparTexto(conteudo)
    }

    /**
     * Lê o conteúdo de um arquivo com tratamento de exceções.
     */
    private fun lerArquivo(arquivo: DocumentFile): String {
        return try {
            abrirELerStream(arquivo.uri)
        } catch (e: Exception) {
            "Erro ao ler arquivo: ${e.message}"
        }
    }

    /**
     * Abre um InputStream e lê todo o conteúdo.
     */
    private fun abrirELerStream(uri: Uri): String {
        return context.contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.bufferedReader().use { reader ->
                reader.readText()
            }
        } ?: "Erro: InputStream nulo"
    }

    /**
     * Aplica todas as transformações de escape necessárias ao texto.
     */
    private fun escaparTexto(texto: String): String {
        return texto
            .escaparBarras()
            .escaparAspas()
            .escaparQuebrasDeLinha()
            .removerCarriageReturn()
    }

    private fun String.escaparBarras(): String = replace("\\", "\\\\")
    private fun String.escaparAspas(): String = replace("'", "\\'")
    private fun String.escaparQuebrasDeLinha(): String = replace("\n", "\\n")
    private fun String.removerCarriageReturn(): String = replace("\r", "")
}