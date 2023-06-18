package cr.ac.una.spotify

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import cr.ac.una.spotify.entity.Track
import cr.ac.una.spotify.viewModel.SpotifyViewModel


class Reproductor : Fragment() {

    private lateinit var spotifyViewModel: SpotifyViewModel
    private lateinit var playButton: Button
    private lateinit var retrocederBtn: Button
    private lateinit var adelantarBtn: Button
    private lateinit var progressBar: SeekBar
    private lateinit var imagen: ImageView
   // private lateinit var playButton: ImageButton


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_reproductor, container, false)
        playButton = view.findViewById(R.id.playButton)
        progressBar = view.findViewById(R.id.progressBar)
        retrocederBtn = view.findViewById(R.id.button)
        adelantarBtn = view.findViewById(R.id.button2)
        imagen = view.findViewById(R.id.imagenReproducir)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val trackAlbum = arguments?.getParcelable<Track>("track")
        val imagentext =  trackAlbum!!.album.images[0].url
        imagen.load(imagentext){
        }
        val preview_url =  trackAlbum!!.preview_url.toString()
        spotifyViewModel= ViewModelProvider(requireActivity()).get(SpotifyViewModel::class.java)
        spotifyViewModel.isPlaying.observe(viewLifecycleOwner) { isPlaying ->
            if (isPlaying) {
                playButton.text = "Pause"
                //android.R.drawable.ic_media_pause
               // playButton.setImageResource(android.R.drawable.ic_media_pause)
            } else {
                playButton.text = "Play"
               // android.R.drawable.ic_media_play
                //playButton.setImageResource(android.R.drawable.ic_media_play)
            }
          //  playButton.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
        }
        progressBar.max = 30
        progressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    spotifyViewModel.pruebaTouch(progress)

                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        spotifyViewModel.currentPosition.observe(viewLifecycleOwner, Observer { currentPosition ->
            progressBar.progress = currentPosition
            if(currentPosition == 30){
                spotifyViewModel.stopRunable()
            }

        })
        playButton.setOnClickListener {
            spotifyViewModel.playPlayer(preview_url)
        }
        retrocederBtn.setOnClickListener(){
            spotifyViewModel.retroceder(preview_url)
            progressBar.progress = spotifyViewModel.currentPosition.value!!

        }
        adelantarBtn.setOnClickListener(){
            spotifyViewModel.adelantar(preview_url)
            progressBar.progress = spotifyViewModel.currentPosition.value!!
        }
    }

}