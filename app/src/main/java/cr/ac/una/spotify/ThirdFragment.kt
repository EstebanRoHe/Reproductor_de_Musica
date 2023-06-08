package cr.ac.una.spotify

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.una.spotify.adapter.ArtistaAdapter
import cr.ac.una.spotify.databinding.FragmentThirdBinding
import cr.ac.una.spotify.entity.Track
import cr.ac.una.spotify.entity.topSong
import cr.ac.una.spotify.viewModel.SpotifyViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ThirdFragment : Fragment() {
    private var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!
    private lateinit var spotifyViewModel: SpotifyViewModel
    private lateinit var trackTops :List<topSong>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val track = arguments?.getParcelable<Track>("track")
        track?.let {

        }
        //val imagen = track!!.artistas[0].imagen[0].url
        //println("IMAGEEEEEEEEEEEEEEENNNNNNNNNNNNNNNNNNNNNNNNN "+imagen)
        spotifyViewModel= ViewModelProvider(requireActivity()).get(SpotifyViewModel::class.java)

        val listView = view.findViewById<RecyclerView>(R.id.thirdView)

        trackTops = mutableListOf<topSong>()
        val adapter = ArtistaAdapter(trackTops  as ArrayList<topSong>)
        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(requireContext())
        spotifyViewModel= ViewModelProvider(requireActivity()).get(SpotifyViewModel::class.java)
        var id = track!!.album.artists[0].id
        spotifyViewModel.searchTops(id.toString()) //("0TnOYISbd1XYRBk9myaseg")


            val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

            spotifyViewModel.topSongs.observe(viewLifecycleOwner){
                    elementos->
                adapter.updateData(elementos as ArrayList<topSong>)
                trackTops = elementos
            }


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}