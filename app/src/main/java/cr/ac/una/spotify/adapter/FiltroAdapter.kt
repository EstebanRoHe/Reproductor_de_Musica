package cr.ac.una.spotify.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import cr.ac.una.spotify.R
import cr.ac.una.spotify.entity.Busqueda


class FiltroAdapter( var busquedas: ArrayList<Busqueda> ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1

    private var onItemClickListener: ((Busqueda) -> Unit)? = null
    fun setOnItemClickListener(listener: (Busqueda) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_filtro , parent, false)
        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_HEADER
        } else {
            VIEW_TYPE_ITEM
        }
    }
    /*
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = busquedas[position]
        if (holder is ViewHolder) {
            val BusquedasItem = item
            holder.bind(BusquedasItem)
        }

    }

     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = busquedas[position]
        if (holder is ViewHolder) {
            val busquedaItem = item
            holder.bind(busquedaItem)
            holder.itemView.setOnClickListener {
                onItemClickListener?.invoke(busquedaItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return busquedas.size
    }
    fun updateData(newData: ArrayList<Busqueda>) {
        busquedas.clear()
        busquedas.addAll(newData)
        notifyDataSetChanged()
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        val busquedaView = itemView.findViewById<TextView>(R.id.filtro)
        val negro = Color.rgb(47, 48, 48)
        fun bind(busquedas: Busqueda) {
            busquedaView.text=busquedas.busqueda
        }

    }


    }
