package com.g4br3.sitedentrodeapp // Altere para o seu pacote, se for usar no Android

import android.os.Build
import androidx.annotation.RequiresApi
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.interfaces.RSAPublicKey
import java.util.Base64
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.math.BigInteger

/**
 * Gera um par de chaves SSH RSA.
 *
 * @param keySize Tamanho da chave em bits (ex: 2048, 4096).
 * @param comment Comentário a ser incluído na chave pública SSH.
 * @return Um Pair onde o primeiro elemento é a chave privada no formato PKCS#8 PEM (NÃO criptografada)
 *         e o segundo elemento é a chave pública no formato OpenSSH.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun generateRsaSshKeyPair(keySize: Int = 2048, comment: String = "generated-by-kotlin-app"): Pair<String, String> {
    // 1. Gerar o par de chaves RSA
    val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
    // Inicializa o gerador de chaves com o tamanho e uma fonte segura de aleatoriedade
    keyPairGenerator.initialize(keySize, SecureRandom())
    val keyPair = keyPairGenerator.generateKeyPair()

    val privateKey = keyPair.private
    val publicKey = keyPair.public as RSAPublicKey // Conversão para acessar detalhes específicos da chave RSA

    // --- 2. Formatação da Chave Privada (Formato PKCS#8 PEM, NÃO criptografada) ---
    // O PKCS#8 é um padrão para o armazenamento de chaves privadas.
    // Para criptografar a chave privada com uma senha (como o 'ssh-keygen -p'),
    // seria necessário implementar um algoritmo de criptografia específico (ex: AES com PBKDF2),
    // o que é mais complexo e geralmente feito com bibliotecas de terceiros como Bouncy Castle,
    // ou por ferramentas de linha de comando como o OpenSSL.
    val pkcs8Encoded = privateKey.encoded
    val privateKeyPem = """
        -----BEGIN PRIVATE KEY-----
        ${Base64.getMimeEncoder().encodeToString(pkcs8Encoded)}
        -----END PRIVATE KEY-----
    """.trimIndent()

    // --- 3. Formatação da Chave Pública (Formato OpenSSH) ---
    // O formato OpenSSH para chaves públicas RSA é:
    // <tipo_chave> <base64_da_chave> <comentário>
    // O conteúdo Base64 da chave é uma sequência binária de:
    // a. Comprimento da string "ssh-rsa" (4 bytes) + bytes da string "ssh-rsa"
    // b. Comprimento do expoente público (4 bytes) + bytes do expoente público
    // c. Comprimento do módulo (4 bytes) + bytes do módulo
    val type = "ssh-rsa"

    val bos = ByteArrayOutputStream()
    val dos = DataOutputStream(bos)

    // Escreve o tipo da chave
    val typeBytes = type.toByteArray(Charsets.UTF_8)
    dos.writeInt(typeBytes.size) // Escreve o comprimento como um inteiro de 4 bytes
    dos.write(typeBytes)         // Escreve os bytes da string

    // Escreve o expoente público
    val publicExponentBytes = publicKey.publicExponent.toByteArray()
    dos.writeInt(publicExponentBytes.size)
    dos.write(publicExponentBytes)

    // Escreve o módulo
    val modulusBytes = publicKey.modulus.toByteArray()
    dos.writeInt(modulusBytes.size)
    dos.write(modulusBytes)

    dos.flush() // Garante que todos os dados foram escritos no ByteArrayOutputStream
    val opensshKeyContent = Base64.getEncoder().encodeToString(bos.toByteArray())

    val publicKeyOpenSsh = "$type $opensshKeyContent $comment"

    return Pair(privateKeyPem, publicKeyOpenSsh)
}

// Exemplo de uso
@RequiresApi(Build.VERSION_CODES.O)
fun main() {
    val (privateKey, publicKey) = generateRsaSshKeyPair(keySize = 2048, comment = "minha-chave-app")
    println("--- Chave Privada (PKCS#8 PEM, NÃO criptografada) ---")
    println(privateKey)
    println("\n--- Chave Pública (OpenSSH) ---")
    println(publicKey)
}