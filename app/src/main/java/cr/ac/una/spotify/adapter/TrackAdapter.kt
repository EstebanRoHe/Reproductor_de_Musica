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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        val imagenImageView = itemView.findViewById<ImageView>(R.id.imagen)
        val nombreTextView = itemView.findViewById<TextView>(R.id.nombre)
        //val albumTextView = itemView.findViewById<TextView>(R.id.album)
        val artistaTextView = itemView.findViewById<TextView>(R.id.artista)
        val boton= itemView.findViewById<Button>(R.id.info)

        val negro = Color.rgb(47, 48, 48)
        fun bind(track: Track) {
          //  itemView.setBackgroundColor(negro)
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
                                println("usiiiii : "+track.uri)
                                println("canciones de album "+track.album.uri)
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

                            else -> false
                        }
                    }
                    popupMenu.show()
                }

            val imageUrl = track.album.images[0].url
            imagenImageView.load(imageUrl) {
            }
            nombreTextView.text = "Canción : "+track.name
            //albumTextView.text = "Álbum : "+track.album.name
            artistaTextView.text = "Artista : " + track.album.artists.joinToString(", ") { it.name }
        }
    }
}
//hasta aqui
 /*
 class TrackAdapter(var tracks: ArrayList<Track>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
            ViewHolder(view)
        } else {

            val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_second, parent, false)
            HeaderViewHolder(view)
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
    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind2(track: Track){
            val imagenView = itemView.findViewById<ImageView>(R.id.imagenAlbum)
            val nombreAlbumView = itemView.findViewById<TextView>(R.id.nombreAlbum)
            val altistaAlbumView = itemView.findViewById<TextView>(R.id.altistaAlbum)

            val imagenViewv = track.album.images[0].url
            imagenView.load(imagenViewv) {
            }
            nombreAlbumView.text = track.album.name
            altistaAlbumView.text = track.album.artists.joinToString(", ") { it.name }
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagenImageView = itemView.findViewById<ImageView>(R.id.imagen)
        val nombreTextView = itemView.findViewById<TextView>(R.id.nombre)
        val albumTextView = itemView.findViewById<TextView>(R.id.album)
        val artistaTextView = itemView.findViewById<TextView>(R.id.artista)
        val boton = itemView.findViewById<Button>(R.id.info)

        val negro = Color.rgb(47, 48, 48)

        fun bind(track: Track) {
            boton.setOnClickListener {

                    val popupMenu = PopupMenu(itemView.context, boton)
                    popupMenu.inflate(R.menu.menu_main)
                    popupMenu.setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.album -> {
                                val action = R.id.action_FirstFragment_to_SecondFragment
                                itemView.findNavController().navigate(action)
                                true
                            }
                            R.id.artista -> {
                                val action = R.id.action_FirstFragment_to_ThirdFragment
                                itemView.findNavController().navigate(action)
                                true
                            }
                            else -> false
                        }
                    }
                    popupMenu.show()
                }


            val imageUrl = track.album.images[0].url
            imagenImageView.load(imageUrl) {
                // Opciones de carga de la imagen
            }
            nombreTextView.text = "Canción : " + track.name
            albumTextView.text = "Álbum : " + track.album.name
            artistaTextView.text = "Artista : " + track.album.artists.joinToString(", ") { it.name }
        }
    }


}
  */
/*   val action = R.id.action_FirstFragment_to_SecondFragment
                itemView.findNavController().navigate(action)

 */

/*

           boton.setOnClickListener {
               val popupMenu = PopupMenu(itemView.context, boton)
               popupMenu.inflate(R.menu.menu_main)
               popupMenu.setOnMenuItemClickListener {
                       menuItem ->
                   val navController = itemView.findNavController()
                   when (menuItem.itemId) {
                       R.id.album -> {
                           val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment("Dato de ejemplo")
                           navController.navigate(action) true
                       } R.id.artista -> {
                       val action = FirstFragmentDirections.actionFirstFragmentToThirdFragment("Dato de ejemplo")
                       navController.navigate(action) true
                       }
                       else -> false
                   }
               }
               popupMenu.show()
           }

               */
/*
 /*

            boton.setOnClickListener {
                val popupMenu = PopupMenu(itemView.context, boton)
                popupMenu.inflate(R.menu.menu_main)
                popupMenu.setOnMenuItemClickListener {
                        menuItem ->
                    val navController = itemView.findNavController()
                    when (menuItem.itemId) {
                        R.id.album -> {
                            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment("Dato de ejemplo")
                            navController.navigate(action) true
                        } R.id.artista -> {
                        val action = FirstFragmentDirections.actionFirstFragmentToThirdFragment("Dato de ejemplo")
                        navController.navigate(action) true
                        }
                        else -> false
                    }
                }
                popupMenu.show()
            }

                */
 */