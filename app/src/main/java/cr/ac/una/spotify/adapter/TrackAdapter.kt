package cr.ac.una.spotify.adapter

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load

import cr.ac.una.spotify.R
import cr.ac.una.spotify.entity.Track
import cr.ac.una.spotify.viewModel.SpotifyViewModel

class TrackAdapter(var tracks: ArrayList<Track>, private val spotifyViewModel: SpotifyViewModel) :
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
            val cancionItem = item
            val url = cancionItem.preview_url
            holder.imagenImageView.setOnClickListener(){
                println("name : "+item.name)
                if (url != null) {
                    spotifyViewModel.playPlayer(url)
                }else{
                    //Toast.makeText(this, "Se agregó correctamente", Toast.LENGTH_SHORT).show()
                    println("No tiene url para reproducir ")
                }
            }
            holder.bind(cancionItem)
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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        val imagenImageView = itemView.findViewById<ImageView>(R.id.imagen)
        val nombreTextView = itemView.findViewById<TextView>(R.id.nombre)
        //val albumTextView = itemView.findViewById<TextView>(R.id.album)
        val artistaTextView = itemView.findViewById<TextView>(R.id.artista)
        val boton= itemView.findViewById<Button>(R.id.info)

        val negro = Color.rgb(47, 48, 48)
        fun bind(track: Track) {
               boton.setOnClickListener {
                    val popupMenu = PopupMenu(itemView.context, boton)
                    popupMenu.inflate(R.menu.menu_main)
                    popupMenu.setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.album -> {
                                val action = R.id.action_FirstFragment_to_SecondFragment
                                val bundle = Bundle().apply {
                                    putParcelable("track",track)
                                }
                                itemView.findNavController().navigate(action,bundle)
                                true
                            }
                            R.id.artista -> {
                                val action = R.id.action_FirstFragment_to_ThirdFragment
                                val bundle = Bundle().apply {
                                    putParcelable("track",track)
                                }
                                itemView.findNavController().navigate(action,bundle)
                                true
                            }
                            R.id.artistaRelate -> {
                                val action = R.id.action_FirstFragment_to_ArtistaFragment
                                val bundle = Bundle().apply {
                                    putParcelable("track",track)
                                }
                                itemView.findNavController().navigate(action,bundle)
                                true
                            }
                            R.id.play -> {
                                val action = R.id.action_FirstFragment_to_reproductorFragment
                                val bundle = Bundle().apply {
                                    putParcelable("track",track)
                                }
                                itemView.findNavController().navigate(action,bundle)
                                true
                            }

                            else -> false
                        }
                    }
                    popupMenu.show()
                }

            val imageUrl = track.album.images[0].url
            imagenImageView.load(imageUrl) {
            }
            nombreTextView.text = track.name
            artistaTextView.text = track.album.artists.joinToString(", ") { it.name }
        }
    }
}
