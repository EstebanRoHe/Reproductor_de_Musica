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
class ArtistaAdapter(private val track: Track) :
    RecyclerView.Adapter<ArtistaAdapter.TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_third, parent, false)
        return TrackViewHolder(view)
    }
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(track)
    }
    override fun getItemCount(): Int {
        return 1
    }

    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imagenView = itemView.findViewById<ImageView>(R.id.imagenArtista)
        private val artistaView = itemView.findViewById<TextView>(R.id.altistaArtista)

        fun bind(track: Track) {
            val imageUrl = track.album.images[0].url
            imagenView.load(imageUrl)
            artistaView.text = "Artista: ${track.album.artists.joinToString(", ") { it.name }}"
        }
    }
}