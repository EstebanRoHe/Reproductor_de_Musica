package cr.ac.una.spotify

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.una.spotify.adapter.FiltroAdapter
import cr.ac.una.spotify.adapter.TrackAdapter
import cr.ac.una.spotify.dao.BusquedaDAO
import cr.ac.una.spotify.dao.SpotifyService
import cr.ac.una.spotify.databinding.FragmentFirstBinding
import cr.ac.una.spotify.db.AppDatabase
import cr.ac.una.spotify.entity.AccessTokenResponse
import cr.ac.una.spotify.entity.Busqueda
import cr.ac.una.spotify.entity.Track
import cr.ac.una.spotify.entity.TrackResponse
import cr.ac.una.spotify.viewModel.SpotifyViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private lateinit var spotifyViewModel: SpotifyViewModel
    private lateinit var busquedaDAO: BusquedaDAO
    private lateinit var tracks: List<Track>
    private lateinit var busquedas: List<Busqueda>
    private lateinit var adapterFiltro: FiltroAdapter

    private val binding get() = _binding!!
    private val filterViewMinHeight by lazy {
        resources.getDimensionPixelSize(R.dimen.filter_view_min_height)
    }
    private val filterViewMin2Height by lazy {
        resources.getDimensionPixelSize(R.dimen.filter_view_min2_height)
    }
    private val filterViewMin3Height by lazy {
        resources.getDimensionPixelSize(R.dimen.filter_view_min3_height)
    }
    private val filterViewMin4Height by lazy {
        resources.getDimensionPixelSize(R.dimen.filter_view_min4_height)
    }
    private val filterViewMin5Height by lazy {
        resources.getDimensionPixelSize(R.dimen.filter_view_min5_height)
    }
    private val filterViewMaxHeight by lazy {
        resources.getDimensionPixelSize(R.dimen.filter_view_max_height)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        busquedaDAO = AppDatabase.getInstance(requireContext()).busquedaDao()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val txtCancion = binding.cancion


        val listView = binding.listView
        val filterView = binding.filtroView
        val layoutParams = filterView.layoutParams
        layoutParams.height = filterViewMinHeight
        filterView.layoutParams = layoutParams

        val filterViewMargin = resources.getDimensionPixelSize(R.dimen.fab_margin)
        val filterViewParams = filterView.layoutParams as ViewGroup.MarginLayoutParams
        filterViewParams.setMargins(filterViewMargin, filterViewMargin, filterViewMargin, filterViewMargin)
        filterView.layoutParams = filterViewParams

        busquedas = mutableListOf<Busqueda>()
        adapterFiltro = FiltroAdapter(busquedas as ArrayList<Busqueda>)
        filterView.adapter = adapterFiltro
        filterView.layoutManager = LinearLayoutManager(requireContext())



        spotifyViewModel = ViewModelProvider(requireActivity()).get(SpotifyViewModel::class.java)
        tracks = mutableListOf<Track>()
        val adapter = TrackAdapter(tracks as ArrayList<Track>, spotifyViewModel)
        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(requireContext())


        txtCancion.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val cancion = s.toString()
                spotifyViewModel.busquedas.observe(viewLifecycleOwner) { elementos ->
                    adapterFiltro.updateData(elementos as ArrayList<Busqueda>)
                    busquedas = elementos
                    val layoutParams = filterView.layoutParams
                    if(cancion == ""){
                        filterView.visibility = View.GONE
                    }else if(elementos.size == 1 ){
                        layoutParams.height = filterViewMinHeight
                        filterView.visibility = View.VISIBLE
                    }else if(elementos.size == 2 ) {
                        layoutParams.height = filterViewMin2Height
                        filterView.visibility = View.VISIBLE
                    }else if(elementos.size == 3 ) {
                        layoutParams.height = filterViewMin3Height
                        filterView.visibility = View.VISIBLE
                    }else if(elementos.size == 4 ) {
                        layoutParams.height = filterViewMin4Height
                        filterView.visibility = View.VISIBLE
                    }else if(elementos.size == 5 ) {
                        layoutParams.height = filterViewMin5Height
                        filterView.visibility = View.VISIBLE
                    }else if (elementos.size > 5) {
                        layoutParams.height = filterViewMaxHeight
                        filterView.visibility = View.VISIBLE
                    }else{
                        filterView.visibility = View.GONE
                    }
                    filterView.layoutParams = layoutParams
                }

                if (cancion.length > 4) {
                    spotifyViewModel.obtenerBusqueda(cancion, requireContext())
                    filterView.visibility = View.VISIBLE
                }
                else {
                    filterView.visibility = View.GONE
                }

            }
        })

        adapterFiltro.setOnItemClickListener {
                busqueda ->
            val cancionBusqueda = busqueda.busqueda
            spotifyViewModel.searchTracks(cancionBusqueda)
            val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            spotifyViewModel.traks.observe(viewLifecycleOwner) { elementos ->
                adapter.updateData(elementos as ArrayList<Track>)
                tracks = elementos
            }
            spotifyViewModel.insertEntity(cancionBusqueda, requireContext())
            filterView.visibility = View.GONE
            listView.visibility = View.VISIBLE
            txtCancion.setText("")


        }


        binding.Buscar.setOnClickListener {
            val cancion = txtCancion.text.toString()
            spotifyViewModel.searchTracks(cancion)
            val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

            spotifyViewModel.traks.observe(viewLifecycleOwner) { elementos ->
                adapter.updateData(elementos as ArrayList<Track>)
                tracks = elementos
            }
            spotifyViewModel.insertEntity(cancion, requireContext())
            filterView.visibility = View.GONE
            listView.visibility = View.VISIBLE
            txtCancion.setText("")
        }

        spotifyViewModel.traks.observe(viewLifecycleOwner) { elementos ->
            adapter.updateData(elementos as ArrayList<Track>)
            tracks = elementos
        }

    }
/*
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

 */

}
