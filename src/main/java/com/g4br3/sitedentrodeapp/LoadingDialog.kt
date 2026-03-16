package com.g4br3.sitedentrodeapp

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.TextView

/**
 * Popup transparente de loading.
 *
 * Uso:
 *   val loader = LoadingDialog(this)
 *   loader.show()                          // mensagem padrão
 *   loader.show("Salvando dados...")       // mensagem personalizada
 *   loader.dismiss()
 */
class LoadingDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Remove title bar padrão
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.loading)

        // Fundo da janela completamente transparente
        window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            // Permite que o fundo do app apareça atrás do dialog
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            setDimAmount(0.5f)  // escurecimento do fundo (0f = nenhum, 1f = total)
        }

        // Não fecha ao tocar fora ou pressionar "Voltar"
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    /**
     * Exibe o dialog com mensagem padrão "Carregando..."
     */
    fun show(message: String = "Carregando...") {
        if (!isShowing) {
            findViewById<TextView>(R.id.tvMessage)?.text = message
            super.show()
        }
    }

    /**
     * Fecha o dialog com segurança
     */
    override fun hide() {
        if (isShowing) dismiss()
    }
}