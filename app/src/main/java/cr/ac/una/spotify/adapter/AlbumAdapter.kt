package cr.ac.una.spotify.adapter

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import cr.ac.una.spotify.R
import cr.ac.una.spotify.entity.Item
import cr.ac.una.spotify.entity.Track


class AlbumAdapter(var items: ArrayList<Item>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ficha_album, parent, false)
        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_HEADER
        } else {
            VIEW_TYPE_ITEM
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (holder is ViewHolder) {
            val CancionItem = item
            holder.bind(CancionItem)
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }

    fun updateData(newData: ArrayList<Item>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        val nombreTextView = itemView.findViewById<TextView>(R.id.cancionAlbum)
        val negro = Color.rgb(47, 48, 48)
        fun bind(item: Item) {
            nombreTextView.text = item.name

        }
    }
}

