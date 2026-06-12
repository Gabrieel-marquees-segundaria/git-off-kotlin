package com.g4br3.sitedentrodeapp.popup

import android.app.AlertDialog
import android.content.Context

class Default {

   companion object {
        fun showAlertDialog( context: Context, title: String, message: String,dimissCallback: ()->Unit,) {
           val builder = AlertDialog.Builder(context)
           builder.setTitle(title)
           builder.setMessage(message)

           // Positive action button
           builder.setPositiveButton("OK") { dialog, which ->
               dialog.dismiss()
               dimissCallback()
           }

           // Negative action button
           builder.setNegativeButton("Cancel") { dialog, which ->
               dialog.cancel()
           }

           val alertDialog: AlertDialog = builder.create()
           alertDialog.show()
       }
   }
}