package com.g4br3.sitedentrodeapp.recicleView

import android.os.Build
import androidx.annotation.RequiresApi
import com.g4br3.sitedentrodeapp.popup.formatSize
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.io.File as FileIO

data class File(
    val name: String,
    val path: String,
    var size: Long,
    val createAt: Long,
    val type: PathType = DirType(),
    val icon: Icon = Default(),
    var FILE: FileIO
    ){
 fun getSize(): String {
     return formatSize(size)
 }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTime(): String {
        val instant: Instant = Instant.ofEpochMilli(createAt)
        val customFormat: String = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneOffset.UTC)
            .format(instant)
        return customFormat
    }
}

open class PathType(open val value: String,open val  number: Int =0){
   fun isType(type: PathType): Boolean {
       return value == type.value
   }
}

 class FileType(): PathType("FILE", 1)


class DirType(): PathType("DIR", 0)





