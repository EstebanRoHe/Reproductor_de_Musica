package cr.ac.una.spotify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import cr.ac.una.spotify.adapter.AlbumAdapter
import cr.ac.una.spotify.databinding.FragmentSecondBinding
import cr.ac.una.spotify.entity.Album
import cr.ac.una.spotify.entity.Genero
import cr.ac.una.spotify.entity.Track

class SecondFragment : Fragment() {
    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.root.findViewById(R.id.secondView)
        val track = arguments?.getParcelable<Track>("track")



        track?.let {
            showTrackDetails(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
   /* private fun showTrackDetails(track: Track) {
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        val adapter = AlbumAdapter(track)
        recyclerView.adapter = adapter
    }*/
    private fun showTrackDetails(track: Track) {
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        val adapter = AlbumAdapter(track)
        recyclerView.adapter = adapter
       // val generos = track.album.genres
       // val generosTexto = generos.map { it.name }
      //  val generosAdapter = AlbumAdapter(track)

       // val fichaRecyclerView = binding.root.findViewById<RecyclerView>(R.id.secondViewFicha)
        //fichaRecyclerView.layoutManager = LinearLayoutManager(requireContext())
       // fichaRecyclerView.adapter = generosAdapter
    }
}
