package com.g4br3.sitedentrodeapp.fileIO

import java.io.File


fun folderSize(dir: File): Long {
    if (!dir.exists()) return 0

    return dir.listFiles()?.sumOf { file ->
        if (file.isDirectory) {
            folderSize(file)
        } else {
            file.length()
        }
    } ?: 0
}