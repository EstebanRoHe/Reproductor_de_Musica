package cr.ac.una.spotify.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load

import cr.ac.una.spotify.R
import cr.ac.una.spotify.entity.Track




class TrackAdapter(var tracks: ArrayList<Track>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
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
        val item = tracks[position]
        if (holder is ViewHolder) {
            val personasItem = item // Restar 1 para compensar el encabezado
            holder.bind(personasItem)
        }
    }
    override fun getItemCount(): Int {
        return tracks.size
    }


    fun updateData(newData: ArrayList<Track>) {
        tracks.clear()
        tracks.addAll(newData)
        notifyDataSetChanged()
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagenImageView = itemView.findViewById<ImageView>(R.id.imagen)
        val nombreTextView = itemView.findViewById<TextView>(R.id.nombre)
        val albumTextView = itemView.findViewById<TextView>(R.id.album)
        val artistaTextView = itemView.findViewById<TextView>(R.id.artista)

        val negro = Color.rgb(47, 48, 48)

        fun bind(track: Track) {
            itemView.setBackgroundColor(negro)


            val imageUrl = track.album.images[0].url
            imagenImageView.load(imageUrl) {

            }

            nombreTextView.text = track.name
            albumTextView.text = track.album.name
            artistaTextView.text = track.album.artists.joinToString(", ") { it.name }
        }
    }
}