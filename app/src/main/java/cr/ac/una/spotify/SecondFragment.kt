package cr.ac.una.spotify

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import cr.ac.una.spotify.adapter.AlbumAdapter
import cr.ac.una.spotify.databinding.FragmentSecondBinding
import cr.ac.una.spotify.entity.Item
import cr.ac.una.spotify.entity.Track
import cr.ac.una.spotify.viewModel.SpotifyViewModel

    class SecondFragment : Fragment() {
        private var _binding: FragmentSecondBinding? = null
        private val binding get() = _binding!!
        private lateinit var spotifyViewModel: SpotifyViewModel
        private lateinit var items: List<Item>


        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            _binding = FragmentSecondBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val trackAlbum = arguments?.getParcelable<Track>("track")
            val imagen = view.findViewById<ImageView>(R.id.imagenAlbum)
            val nombre= view.findViewById<TextView>(R.id.nombreAlbum)
            val nombreArtista = view.findViewById<TextView>(R.id.altistaAlbum)
            val fecha = view.findViewById<TextView>(R.id.fechaDelAlbum)
            val genero = view.findViewById<TextView>(R.id.genero)

            val imagentext =  trackAlbum!!.album.images[0].url
            imagen.load(imagentext){
            }
            nombre.text = " Álbum : "+trackAlbum.album.name
            nombreArtista.text  = " Artista : " + trackAlbum.album.artists.joinToString(", ") { it.name }
            fecha.text = " Publicacion : "+trackAlbum.album.release_date
            genero.text = " Género : Egg punk, Noise rock, Pop"



            spotifyViewModel = ViewModelProvider(this).get(SpotifyViewModel::class.java)
            val listView = view.findViewById<RecyclerView>(R.id.viewFicha)

            items = mutableListOf<Item>()
            val adapter = AlbumAdapter(items  as ArrayList<Item>)
            listView.adapter = adapter
            listView.layoutManager = LinearLayoutManager(requireContext())
            spotifyViewModel= ViewModelProvider(requireActivity()).get(SpotifyViewModel::class.java)
            spotifyViewModel.searchAlbums(trackAlbum.album.id)
           // println("!!! Id del artista !!!!! "+ trackAlbum.album.artists[0].id)
            println("!!! Id del artista !!!!! "+ trackAlbum.album.artists.joinToString(", ") { it.id })
            val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                 spotifyViewModel.albums.observe(viewLifecycleOwner){
                        elementos->
                    adapter.updateData(elementos as ArrayList<Item>)
                    items = elementos
            }

        }


        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

    }
