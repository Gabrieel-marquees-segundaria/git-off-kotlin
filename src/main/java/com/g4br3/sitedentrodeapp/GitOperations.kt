package com.g4br3.sitedentrodeapp

import android.content.Context
import android.util.Log
import java.io.File
import java.io.IOException
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider

/**
 * Gerencia operações Git como clone, pull e listar arquivos
 */
class GitOperations(private val context: Context) {
    
    public val reposDir = File(context.filesDir, "repos")
    private val sshDir = File(context.filesDir, ".ssh")
    
    init {
        reposDir.mkdirs()
        sshDir.mkdirs()
    }
    
    /**
     * Salva a chave privada SSH
     */
    fun saveSshKey(privateKey: String): Result<Unit> {
        return try {
            val keyFile = File(sshDir, "id_rsa")
            keyFile.writeText(privateKey)
            keyFile.setReadable(true, true)
            keyFile.setWritable(true, true)
            Log.d("GitOps", "SSH key saved at ${keyFile.absolutePath}")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("GitOps", "Error saving SSH key", e)
            Result.failure(e)
        }
    }
    
    /**
     * Clona um repositório via SSH
     * @param owner Proprietário do repositório (ex: torvalds)
     * @param repo Nome do repositório (ex: linux)
     * @return Resultado do clone com mensagem
     */
    fun cloneRepository(owner: String, repo: String): Result<String> {
        return try {
            if (owner.isBlank() || repo.isBlank()) {
                return Result.failure(IllegalArgumentException("Owner e repo não podem estar vazios"))
            }
            
            val repoUrl = "git@github.com:$owner/$repo.git"
            val repoPath = File(reposDir, "$owner-$repo")
            
            // Se o repositório já existe, retorna sucesso
            if (repoPath.exists()) {
                return Result.success("Repositório já existe em: ${repoPath.absolutePath}")
            }
            
            // Executa o comando git clone via SSH
            val command = arrayOf(
                "sh", "-c",
                "GIT_SSH_COMMAND='ssh -i ${sshDir}/id_rsa -o StrictHostKeyChecking=no' git clone $repoUrl ${repoPath.absolutePath}"
            )
            
            val process = Runtime.getRuntime().exec(command)
            val exitCode = process.waitFor()
            
            val output = process.inputStream.bufferedReader().readText()
            val error = process.errorStream.bufferedReader().readText()
            
            Log.d("GitOps", "Clone output: $output")
            Log.d("GitOps", "Clone error: $error")
            Log.d("GitOps", "Exit code: $exitCode")
            
            if (exitCode == 0) {
                Result.success("✓ Repositório clonado com sucesso em:\n${repoPath.absolutePath}")
            } else {
                val errorMsg = error.ifBlank { "Erro ao clonar repositório (código: $exitCode)" }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: IOException) {
            Log.e("GitOps", "IOException ao clonar", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("GitOps", "Erro ao clonar repositório", e)
            Result.failure(e)
        }
    }
    
    /**
     * Faz pull de um repositório
     */
    fun pullRepository(owner: String, repo: String): Result<String> {
        return try {
            val repoPath = File(reposDir, "$owner-$repo")
            
            if (!repoPath.exists()) {
                return Result.failure(IllegalArgumentException("Repositório não encontrado: ${repoPath.absolutePath}"))
            }
            
            val command = arrayOf(
                "sh", "-c",
                "cd ${repoPath.absolutePath} && GIT_SSH_COMMAND='ssh -i ${sshDir}/id_rsa -o StrictHostKeyChecking=no' git pull"
            )
            
            val process = Runtime.getRuntime().exec(command)
            val exitCode = process.waitFor()
            
            val output = process.inputStream.bufferedReader().readText()
            val error = process.errorStream.bufferedReader().readText()
            
            Log.d("GitOps", "Pull output: $output")
            Log.d("GitOps", "Pull error: $error")
            
            if (exitCode == 0) {
                Result.success("✓ Pull executado com sucesso\n$output")
            } else {
                val errorMsg = error.ifBlank { "Erro ao fazer pull (código: $exitCode)" }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: IOException) {
            Log.e("GitOps", "IOException ao fazer pull", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("GitOps", "Erro ao fazer pull", e)
            Result.failure(e)
        }
    }
    
    /**
     * Lista os arquivos do repositório clonado
     */
    fun listRepositoryFiles(owner: String, repo: String): Result<String> {
        return try {
            val repoPath = File(reposDir, "$owner-$repo")
            
            if (!repoPath.exists()) {
                return Result.failure(IllegalArgumentException("Repositório não encontrado"))
            }
            
            val files = repoPath.listFiles()?.map { it.name }?.sorted() ?: emptyList()
            
            if (files.isEmpty()) {
                Result.success("Repositório vazio ou sem permissão de leitura")
            } else {
                val fileList = files.joinToString("\n") { "  • $it" }
                Result.success("Arquivos em ${repoPath.name}:\n$fileList")
            }
        } catch (e: Exception) {
            Log.e("GitOps", "Erro ao listar arquivos", e)
            Result.failure(e)
        }
    }
    
    /**
     * Obtém o caminho do repositório clonado
     */
    fun getRepositoryPath(owner: String, repo: String): File {
        return File(reposDir, "$owner-$repo")
    }
    
    /**
     * Verifica se um repositório está clonado
     */
    fun isRepositoryCloned(owner: String, repo: String): Boolean {
        return getRepositoryPath(owner, repo).exists()
    }

    /**
     * Clona um repositório via HTTPS usando Access Token (com JGit)
     * @param owner Proprietário do repositório (ex: torvalds)
     * @param repo Nome do repositório (ex: linux)
     * @param accessToken GitHub Access Token (com permissão repo)
     * @return Resultado do clone com mensagem
     */
    fun cloneRepositoryWithToken(owner: String, repo: String, accessToken: String): Result<String> {
        return try {
            if (owner.isBlank() || repo.isBlank()) {
                return Result.failure(IllegalArgumentException("Owner e repo não podem estar vazios"))
            }
            if (accessToken.isBlank()) {
                return Result.failure(IllegalArgumentException("Access token não pode estar vazio"))
            }

            val repoUrl = "https://github.com/$owner/$repo.git"
            val repoPath = File(reposDir, "$owner-$repo")

            // Se o repositório já existe, retorna sucesso
            if (repoPath.exists()) {
                return Result.success("✓ Repositório já existe em: ${repoPath.absolutePath}")
            }

            repoPath.mkdirs()

            Log.d("GitOps", "Iniciando clone de $repoUrl para ${repoPath.absolutePath}")

            try {
                // Usar JGit com credentials do token
                Git.cloneRepository()
                    .setURI(repoUrl)
                    .setDirectory(repoPath)
                    .setCredentialsProvider(UsernamePasswordCredentialsProvider("oauth2", accessToken))
                    .call()
                    .close()

                Log.d("GitOps", "Clone completado com sucesso")
                Result.success("✓ Repositório clonado com sucesso em:\n${repoPath.absolutePath}")
            } catch (e: Exception) {
                // Limpar diretório em caso de falha
                repoPath.deleteRecursively()
                Log.e("GitOps", "Erro ao clonar com JGit", e)
                Result.failure(e)
            }
        } catch (e: IOException) {
            Log.e("GitOps", "IOException ao clonar", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("GitOps", "Erro ao clonar repositório", e)
            Result.failure(e)
        }
    }

    /**
     * Faz pull de um repositório usando Access Token (com JGit)
     */
    fun pullRepositoryWithToken(owner: String, repo: String, accessToken: String): Result<String> {
        return try {
            val repoPath = File(reposDir, "$owner-$repo")

            if (!repoPath.exists()) {
                return Result.failure(IllegalArgumentException("Repositório não encontrado: ${repoPath.absolutePath}"))
            }

            if (accessToken.isBlank()) {
                return Result.failure(IllegalArgumentException("Access token não pode estar vazio"))
            }

            Log.d("GitOps", "Iniciando pull em ${repoPath.absolutePath}")

            try {
                val git = Git.open(repoPath)
                git.pull()
                    .setCredentialsProvider(UsernamePasswordCredentialsProvider("oauth2", accessToken))
                    .call()
                git.close()

                Log.d("GitOps", "Pull completado com sucesso")
                Result.success("✓ Pull executado com sucesso")
            } catch (e: Exception) {
                Log.e("GitOps", "Erro ao fazer pull com JGit", e)
                Result.failure(e)
            }
        } catch (e: Exception) {
            Log.e("GitOps", "Erro ao fazer pull", e)
            Result.failure(e)
        }
    }
}
