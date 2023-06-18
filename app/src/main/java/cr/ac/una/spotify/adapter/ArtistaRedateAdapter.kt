package cr.ac.una.spotify.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import cr.ac.una.spotify.R
import cr.ac.una.spotify.entity.ArtistaRelated

class ArtistaRedateAdapter(var artistaRelated: ArrayList<ArtistaRelated>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_artista_relade , parent, false)
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
        val item = artistaRelated[position]
        if (holder is ViewHolder) {
            val personasItem = item
            holder.bind(personasItem)
        }
    }
    override fun getItemCount(): Int {
        return artistaRelated.size
    }

    fun updateData(newData: ArrayList<ArtistaRelated>) {
        artistaRelated.clear()
        artistaRelated.addAll(newData)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        val imagenRelacionadoView = itemView.findViewById<ImageView>(R.id.imagenRelacionado)
        val nombreAlbumView = itemView.findViewById<TextView>(R.id.nombreRelate )
        val nombreArtistaView = itemView.findViewById<TextView>(R.id.artistaRelate)
        fun bind(artistaRelated: ArtistaRelated) {
            val imageUrl = artistaRelated.images[0].url.toString()
            imagenRelacionadoView.load(imageUrl) {
            }
            nombreAlbumView.text = artistaRelated.name
            nombreArtistaView.text = artistaRelated.popularity.toString()
        }

    }
}