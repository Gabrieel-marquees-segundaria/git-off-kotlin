package com.g4br3.sitedentrodeapp.components
import android.content.Context
import kotlin.collections.listOf


class JSLoader(private val context: Context)  {
    val jsList: List<String>

    var paths: List<String> = listOf<String>(
        "android",
        //"settings-2-testes",
        "settings"
    )
    init {
        val listaFinal = mutableListOf<String>()
        var assetTextReader: AssetTextReader = AssetTextReader(context)
        this.paths.forEach { name ->
            var assetJsList = assetTextReader.lerConteudosDaPasta(name)
            if (assetJsList != null){
               listaFinal.addAll(assetJsList)
            }

        }
        jsList = listaFinal
    }

fun JsJuncao(): String{
        var stringFinal = String()
        var assetTextReader: AssetTextReader = AssetTextReader(context)
        this.paths.forEach { name ->
            var assetJsString = assetTextReader.JuncaoDeConteudosDaPasta(name)
            if (assetJsString != null){
                stringFinal += assetJsString
            }

        }
        return stringFinal
}
}