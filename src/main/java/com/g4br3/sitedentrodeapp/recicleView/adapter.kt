package com.g4br3.sitedentrodeapp.recicleView

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.g4br3.sitedentrodeapp.R


class FilesAdapter(private val Files: MutableList<File>, val onClickItem: (fileData: File)->Unit): RecyclerView.Adapter<FilesAdapter.FilesViewHolder>() {

    class FilesViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val icon = view.findViewById<ImageView>(R.id.ivFileIcon)
        val name = view.findViewById<TextView>(R.id.tvFileName)
        val size = view.findViewById<TextView>(R.id.tvFileSize)
        val data = view.findViewById<TextView>(R.id.tvFileDate)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FilesAdapter.FilesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_file, parent, false)

        return FilesViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: FilesAdapter.FilesViewHolder, position: Int) {
        // Antes: estava alterando o id da View, o que não muda a imagem exibida.
        // Deve-se alterar o recurso exibido pela ImageView.
        holder.icon.setImageResource(Files[position].icon.value)
        holder.name.text = Files[position].name
        holder.size.text = Files[position].getSize()
        holder.data.text = Files[position].getTime()
        holder.view.setOnClickListener {
            onClickItem( Files[position])
        }

    }


    override fun getItemCount(): Int {
        return Files.size
    }

    /**
     * Replace the adapter contents with new items and refresh the list.
     * Using a dedicated method keeps mutation logic inside the adapter and
     * avoids reassigning the original list reference from callers.
     */
    fun updateItems(newItems: List<File>) {
        Files.clear()
        Files.addAll(newItems)
        notifyDataSetChanged()
    }
}