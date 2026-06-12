package com.g4br3.sitedentrodeapp.popup

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import com.g4br3.sitedentrodeapp.R
import com.g4br3.sitedentrodeapp.recicleView.File

@SuppressLint("MissingInflatedId")
fun showFileInfo(context: Context, file: File) {

    val view = LayoutInflater.from(context)
        .inflate(R.layout.dialog_file_info, null)

    fun setText(id: Int, value: Any?) {
        view.findViewById<TextView>(id).text = "$value"
    }

    setText(R.id.txtName, file.name)
    setText(R.id.txtPath,  file.path.replace(context.filesDir.absolutePath,""))
    setText(R.id.txtSize,  formatSize(file.size))
    setText(R.id.txtNumber,  file.type.number)
    setText(R.id.txCreated, formatDate(file.createAt))

    AlertDialog.Builder(context)
        .setTitle("Informações")
        .setView(view)
        .setPositiveButton("OK", null)
        .show()
}

private fun formatDate(timestamp: Long): String {
    return java.text.SimpleDateFormat(
        "dd/MM/yyyy HH:mm:ss",
        java.util.Locale.getDefault()
    ).format(java.util.Date(timestamp))
}
fun formatSize(size: Long): String {
    if (size <= 0) return "0 B"

    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    var value = size.toDouble()
    var index = 0

    while (value >= 1024 && index < units.lastIndex) {
        value /= 1024
        index++
    }

    return String.format("%.2f %s", value, units[index])
}

