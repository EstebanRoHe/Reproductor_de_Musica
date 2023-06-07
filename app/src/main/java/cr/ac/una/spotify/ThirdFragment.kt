package cr.ac.una.spotify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.una.spotify.adapter.AlbumAdapter
import cr.ac.una.spotify.adapter.ArtistaAdapter
import cr.ac.una.spotify.adapter.TrackAdapter
import cr.ac.una.spotify.databinding.FragmentSecondBinding
import cr.ac.una.spotify.databinding.FragmentThirdBinding
import cr.ac.una.spotify.entity.Track
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ThirdFragment : Fragment() {
    private var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.root.findViewById(R.id.thirdView)
        val track = arguments?.getParcelable<Track>("track")
        track?.let {
            showTrackDetails(it)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun showTrackDetails(track: Track) {
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        val adapter = ArtistaAdapter(track)
        recyclerView.adapter = adapter
    }

}