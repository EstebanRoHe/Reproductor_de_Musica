package cr.ac.una.spotify.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import cr.ac.una.spotify.R
import cr.ac.una.spotify.entity.ImagenResponse


class AdapterImagen(var imagenResponse: ArrayList<ImagenResponse>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_third, parent, false)
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
        val item = imagenResponse[position]
        if (holder is ViewHolder) {
            val personasItem = item
            holder.bind(personasItem)
        }
    }
    override fun getItemCount(): Int {
        return imagenResponse.size
    }

    fun updateData(newData: ArrayList<ImagenResponse>) {
        imagenResponse.clear()
        imagenResponse.addAll(newData)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {

        val ImageView = itemView.findViewById<ImageView>(R.id.imagenArtista)
        fun bind(imagenResponse: ImagenResponse) {
            val imageUrl = imagenResponse.images[0].url
            ImageView.load(imageUrl) {
            }

        }
    }
}