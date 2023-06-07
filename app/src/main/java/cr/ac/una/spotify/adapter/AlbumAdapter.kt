package cr.ac.una.spotify.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import cr.ac.una.spotify.R
import cr.ac.una.spotify.entity.Track

class AlbumAdapter(private val track: Track) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_second, parent, false)
            TrackViewHolder(view)

        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.ficha_album , parent, false)
            FichaViewHolder(view)
        }
    }
    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_HEADER
        } else {
            VIEW_TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TrackViewHolder) {
            holder.bind(track)
        } else if (holder is FichaViewHolder) {
            holder.bind(track)
        }
    }
    override fun getItemCount(): Int {
        return 1
    }

    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imagenImageView = itemView.findViewById<ImageView>(R.id.imagenAlbum)
        private val nombreTextView = itemView.findViewById<TextView>(R.id.nombreAlbum)
        private val artistaTextView = itemView.findViewById<TextView>(R.id.altistaAlbum)
        private val fechaTextView = itemView.findViewById<TextView>(R.id.fechaDelAlbum)

        fun bind(track: Track) {
            val imageUrl = track.album.images[0].url
            imagenImageView.load(imageUrl)
            nombreTextView.text = "Álbum: ${track.album.name}"
            artistaTextView.text = "Artista: ${track.album.artists.joinToString(", ") { it.name }}"
            fechaTextView.text = "Fecha: ${track.album.release_date}"

        }
    }

    inner class FichaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val generoTextView = itemView.findViewById<TextView>(R.id.genero)

        fun bind(track: Track) {
           // generoTextView.text = track.album.genres.joinToString(", "){ it.name }
            generoTextView.text = "hola"
        }
    }
}
/*
class AlbumAdapter(private val track: Track) :
    RecyclerView.Adapter<AlbumAdapter.TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_second, parent, false)
        return TrackViewHolder(view)
    }


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(track)
    }
    override fun getItemCount(): Int {
        return 1
    }

    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imagenImageView = itemView.findViewById<ImageView>(R.id.imagenAlbum)
        private val nombreTextView = itemView.findViewById<TextView>(R.id.nombreAlbum)
        private val artistaTextView = itemView.findViewById<TextView>(R.id.altistaAlbum)

        fun bind(track: Track) {
            val imageUrl = track.album.images[0].url
            imagenImageView.load(imageUrl)
            nombreTextView.text = "Álbum: ${track.album.name}"
            artistaTextView.text = "Artista: ${track.album.artists.joinToString(", ") { it.name }}"
        }
    }

    inner class FichaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imagenImageView = itemView.findViewById<ImageView>(R.id.imagenAlbum)
        private val nombreTextView = itemView.findViewById<TextView>(R.id.nombreAlbum)
        private val artistaTextView = itemView.findViewById<TextView>(R.id.altistaAlbum)

        fun bind(track: Track) {
            val imageUrl = track.album.images[0].url
            imagenImageView.load(imageUrl)
            nombreTextView.text = "Álbum: ${track.album.name}"
            artistaTextView.text = "Artista: ${track.album.artists.joinToString(", ") { it.name }}"
        }
    }
}*/