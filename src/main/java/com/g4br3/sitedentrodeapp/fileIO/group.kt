package com.g4br3.sitedentrodeapp.fileIO

import android.util.Log
import com.g4br3.sitedentrodeapp.FilesListActivity
import com.g4br3.sitedentrodeapp.R









class GroupBuild(val activity: FilesListActivity) {
    fun all(callback: ()->Unit): GroupBuild {
        activity.findViewById<com.google.android.material.chip.Chip>(R.id.chipTodos)
            .setOnClickListener {
                Log.d("GroupBuild", "all")

                callback()
            }
        return this
    }

    fun images(callback: ()->Unit): GroupBuild {
        activity.findViewById<com.google.android.material.chip.Chip>(R.id.chipImagens)
            .setOnClickListener {
                Log.d("GroupBuild", "img")
                callback()
            }
        return this
    }


    fun documentos(callback: ()->Unit): GroupBuild {
        activity.findViewById<com.google.android.material.chip.Chip>(R.id.chipDocumentos)
            .setOnClickListener {
                Log.d("GroupBuild", "doc")
                callback()
            }
        return this
    }



    fun videos(callback: ()->Unit): GroupBuild {
        activity.findViewById<com.google.android.material.chip.Chip>(R.id.chipVideos)
            .setOnClickListener {
                callback()
            }
        return this
    }

    fun audios(callback: ()->Unit): GroupBuild {
        activity.findViewById<com.google.android.material.chip.Chip>(R.id.chipAudios)
            .setOnClickListener {
                callback()
            }
        return this
    }
}