package com.g4br3.sitedentrodeapp.recicleView

import com.g4br3.sitedentrodeapp.R

open class Icon(var value: Int){
    public open fun getHeight(): Int {
        return 54
    }

    public open fun getWidth(): Int {
        return 44
    }
}

class Default(
): Icon(R.mipmap.ic_document){

}


class Audio(
): Icon(R.mipmap.ic_audio)


class Image(
): Icon( R.mipmap.ic_image)


class Folder(
): Icon(R.mipmap.ic_folder_empty){
    override fun getHeight(): Int {
        return 56
    }
    override fun getWidth(): Int {
        return 46
    }
}


class Video(
): Icon(R.mipmap.ic_video)
// cria um icon de um documento qualquer referencie o git hub

class Python(
): Icon(R.mipmap.ic_python)

class JavaScript(
): Icon(R.mipmap.ic_js)

class Kotlin(
): Icon(R.mipmap.ic_kt)

class Clang(
): Icon(R.mipmap.ic_clang)


class Html(
): Icon(R.mipmap.ic_html)



class Rust(
): Icon(R.mipmap.ic_rust)



class Css(
): Icon(R.mipmap.ic_css)


class Markdown(
): Icon(R.mipmap.ic_md)




class CPlusPlus(
): Icon(R.mipmap.ic_cpp)

class LuaLang(
): Icon(R.mipmap.ic_lua)


class Json(
): Icon(R.mipmap.ic_json)

class Bash(
): Icon(R.mipmap.ic_sh)


fun getIcon(name: String): Icon {
    // Normaliza o nome para comparação (minúsculas)
    val lower = name.lowercase()

    fun endWithDot(ext: String): Boolean = lower.endsWith(ext)

    return when {
        endWithDot(".mp3") -> Audio()
        endWithDot(".png") || endWithDot(".jpg") || endWithDot(".jpeg") || endWithDot(".webp") || endWithDot(".gif") -> Image()
        endWithDot(".mp4") || endWithDot(".mkv") || endWithDot(".mov") -> Video()
        endWithDot(".py") ||  endWithDot(".pyc") ||  endWithDot(".pyw") ||  endWithDot(".pyi")  ||  endWithDot(".pyx") ||  endWithDot(".pyd")  ||  endWithDot(".pycz")-> Python()
        endWithDot(".js") ||  endWithDot(".jsx") ||  endWithDot(".mjs") ||  endWithDot(".cjs") ||  endWithDot(".ts")   -> JavaScript()
        endWithDot(".c") -> Clang()
        endWithDot(".cpp") || endWithDot(".cc") || endWithDot(".cxx") -> CPlusPlus()
        endWithDot(".css") -> Css()
        endWithDot(".rs") -> Rust()
        endWithDot(".html") || endWithDot(".htm") -> Html()
        endWithDot(".md") || endWithDot(".markdown") -> Markdown()
        endWithDot(".kt") -> Kotlin()
        endWithDot(".lua") -> LuaLang()
        endWithDot(".json") -> Json()
        else -> Default()
    }
}