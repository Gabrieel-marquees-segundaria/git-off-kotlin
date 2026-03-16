package com.g4br3.sitedentrodeapp.recicleView

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.File as FileIO
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

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
     return "${size/ 1024} KB"
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

open class PathType(open val value: String){
   fun isType(type: PathType): Boolean {
       return value == type.value
   }
}

 class FileType(): PathType("FILE")


class DirType(): PathType("DIR")





