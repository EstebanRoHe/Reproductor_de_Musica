package cr.ac.una.spotify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.una.spotify.adapter.ArtistaRedateAdapter
import cr.ac.una.spotify.databinding.FragmentArtistaBinding
import cr.ac.una.spotify.entity.ArtistaRelated
import cr.ac.una.spotify.entity.Track
import cr.ac.una.spotify.viewModel.SpotifyViewModel


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ArtistaFragment : Fragment() {
    private var _binding: FragmentArtistaBinding? = null
    private lateinit var spotifyViewModel: SpotifyViewModel
    private lateinit var artistas: List<ArtistaRelated>

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_artista, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val trackAlbum = arguments?.getParcelable<Track>("track")
        spotifyViewModel= ViewModelProvider(requireActivity()).get(SpotifyViewModel::class.java)
        val listView = view.findViewById<RecyclerView>(R.id.relate_view )

        artistas = mutableListOf<ArtistaRelated>()
        val adapter = ArtistaRedateAdapter(artistas  as ArrayList<ArtistaRelated>)
        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(requireContext())

        spotifyViewModel.searchRelate(trackAlbum!!.album.artists[0].id)
        spotifyViewModel.relades.observe(viewLifecycleOwner){
                elementos->
            adapter.updateData(elementos as ArrayList<ArtistaRelated>)
            artistas = elementos
        }

    }

}