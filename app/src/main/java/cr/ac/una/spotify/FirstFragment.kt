package cr.ac.una.spotify

import android.content.Context
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.una.spotify.adapter.TrackAdapter
import cr.ac.una.spotify.dao.SpotifyService
import cr.ac.una.spotify.databinding.FragmentFirstBinding
import cr.ac.una.spotify.entity.AccessTokenResponse
import cr.ac.una.spotify.entity.Track
import cr.ac.una.spotify.entity.TrackResponse
import cr.ac.una.spotify.viewModel.SpotifyViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private lateinit var searchButton: Button
    private lateinit var searchEditText: EditText
    private lateinit var spotifyViewModel : SpotifyViewModel
    private lateinit var tracks :List<Track>

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,

        savedInstanceState: Bundle?

    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val txtCancion = view.findViewById<EditText>(R.id.cancion)

        val listView = view.findViewById<RecyclerView>(R.id.list_view)
        tracks = mutableListOf<Track>()
        val adapter = TrackAdapter(tracks  as ArrayList<Track>)
        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(requireContext())
        spotifyViewModel= ViewModelProvider(requireActivity()).get(SpotifyViewModel::class.java)

        binding.Buscar.setOnClickListener {

            val cancion = txtCancion.text.toString()
            spotifyViewModel.searchTracks(cancion)

            val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

            spotifyViewModel.traks.observe(viewLifecycleOwner){
                elementos->
                adapter.updateData(elementos as ArrayList<Track>)
                tracks = elementos
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}