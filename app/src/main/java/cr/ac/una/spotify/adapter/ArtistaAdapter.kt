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
import cr.ac.una.spotify.entity.topSong
import cr.ac.una.spotify.viewModel.SpotifyViewModel

class ArtistaAdapter( var trackTops: ArrayList<topSong>,private val spotifyViewModel: SpotifyViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.artista_item, parent, false)
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
        val item = trackTops[position]
        if (holder is ViewHolder) {
            val CancionItem = item
            holder.imagenTopView.setOnClickListener(){
                spotifyViewModel.playPlayer(CancionItem.preview_url)
            }
            holder.bind(CancionItem)
        }
    }
    override fun getItemCount(): Int {
        return trackTops.size
    }

    fun updateData(newData: ArrayList<topSong>) {
        trackTops.clear()
        trackTops.addAll(newData)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        val imagenTopView = itemView.findViewById<ImageView>(R.id.imagenTop)
        val nombreAlbumView = itemView.findViewById<TextView>(R.id.nombreAlbumTop)
        val nombreArtistaView = itemView.findViewById<TextView>(R.id.nombreArtistaTop)
        val popularidadView = itemView.findViewById<TextView>(R.id.popularidadTop)

        val negro = Color.rgb(47, 48, 48)
        fun bind(trackTops: topSong) {

           val imageUrl = trackTops.album.images[0].url.toString()
            imagenTopView.load(imageUrl) {
            }
            nombreAlbumView.text=trackTops.album.name
            nombreArtistaView.text=trackTops.artists.joinToString(", "){it.name}
            popularidadView.text = trackTops.popularity.toString()

        }
    }
    }
