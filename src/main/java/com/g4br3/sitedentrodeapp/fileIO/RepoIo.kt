package com.g4br3.sitedentrodeapp.fileIO

import android.content.Context
import java.io.File

class RepoIo {
    companion object {
        fun FILE(context: Context): File {
        return    File(context.filesDir, "repos")
        }
    }
}