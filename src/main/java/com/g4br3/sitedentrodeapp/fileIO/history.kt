package com.g4br3.sitedentrodeapp.fileIO

import android.content.Context
import android.util.Log
import androidx.core.content.edit


class History(val context: Context) {
    val prefsHistorico = context.getSharedPreferences("historico_milissegundos", Context.MODE_PRIVATE)
    val editor = prefsHistorico.edit()
    val key = "history"
    val maxSize = 12
    val minSize = 8
    var size: Number? = null
    fun setHistory(file:String ){
        val timestampMs =System.currentTimeMillis()
        setHistory(file, timestampMs)
        size.let {
            size = it?.toInt()?.plus(1)
            Log.d(key, size.toString())
        }
        larger_mins_size()
    }

    fun setHistory(file: String, timestampMs: Long){
        editor.putString(file, timestampMs.toString())
        editor.apply()
    }
    fun larger_mins_size(){
        size?.let {
            if (it.toInt() >= maxSize ){

                val allHistory  =getHistory()
                val keys = allHistory.keys.toList()

                clear()
                for  ( (i,v) in  allHistory.values.withIndex()){
                    if (i <= minSize +1){
                        setHistory(keys[i],v)

                    }
                }

            }
        }

    }
    fun getHistory(): Map< String,Long> {
        val todosOsItensRaw =   prefsHistorico.all as Map<String, String>
        // 2. Converte as chaves de String para Long e organiza em ordem cronológica (Crescente)
        val mapaOrdenado = todosOsItensRaw
            .mapValues { it.value.toLong() }
            .entries
            .sortedByDescending { it.value }
            .associate { it.toPair() }
       // Log.d("history",mapaOrdenado.toString())
        size = mapaOrdenado.size
        return mapaOrdenado

    }
    fun clear_item(item: String){
        editor.remove(item)
    }
    fun clear(){
        prefsHistorico.edit { clear() }
    }
}