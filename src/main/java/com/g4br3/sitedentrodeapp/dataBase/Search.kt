package com.g4br3.sitedentrodeapp.dataBase

import androidx.sqlite.db.SimpleSQLiteQuery




fun buscarPorColuna(pathDao: PathDao, coluna: String, termo: String): List<Path> {
        // Lista de colunas válidas (evita SQL Injection)
        val colunasPermitidas = listOf("name", "type", "level", "father")

        if (coluna !in colunasPermitidas) {
            throw IllegalArgumentException("Coluna inválida: $coluna")
        }
        val sql = "SELECT * FROM path WHERE $coluna LIKE ?"
        val args = arrayOf("%$termo%")
        val query = SimpleSQLiteQuery(sql, args)
        return pathDao.searchByRawQuery(query)
    }
