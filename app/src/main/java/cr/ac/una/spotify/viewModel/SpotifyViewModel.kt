package cr.ac.una.spotify.viewModel

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.util.Base64
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.*
import cr.ac.una.spotify.dao.BusquedaDAO
import cr.ac.una.spotify.dao.SpotifyService
import cr.ac.una.spotify.db.AppDatabase
import cr.ac.una.spotify.entity.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*


class SpotifyViewModel : ViewModel() {
    private lateinit var busquedaDAO: BusquedaDAO
    private val _track: MutableLiveData<List<Track>> = MutableLiveData()
    val traks: LiveData<List<Track>> = _track
    private val _album: MutableLiveData<List<Item>> = MutableLiveData()
    val albums: LiveData<List<Item>> = _album
    private val _topSong: MutableLiveData<List<topSong>> = MutableLiveData()
    val topSongs: LiveData<List<topSong>> = _topSong
    private val _artist: MutableLiveData<List<ImagenResponse>> = MutableLiveData()
    val imagenes: LiveData<List<ImagenResponse>> = _artist
    private val _busqueda: MutableLiveData<List<Busqueda>> = MutableLiveData()
    val busquedas: LiveData<List<Busqueda>> = _busqueda
    private val _isPlaying: MutableLiveData<Boolean> = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> = _isPlaying
    private val _relade: MutableLiveData<List<ArtistaRelated>> = MutableLiveData()
    val relades: LiveData<List<ArtistaRelated>> = _relade
    private var mediaPlayer: MediaPlayer? = null
    private var currentUrl : String? = null
    private val _currentPosition: MutableLiveData<Int> = MutableLiveData(0)
    val currentPosition: LiveData<Int> = _currentPosition
    private val _maxProgress: MutableLiveData<Int> = MutableLiveData()
    val maxProgress: LiveData<Int> = _maxProgress
    private var runnable: Runnable? = null
    private val handler = Handler()


    fun insertEntity(cancion: String, context : Context) {
        busquedaDAO = AppDatabase.getInstance(context).busquedaDao()
        val entity = Busqueda(null,cancion, Date())
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                busquedaDAO.insert(entity)
            }
        }
    }

    fun obtenerBusqueda(cancion: String, context : Context) {
        busquedaDAO = AppDatabase.getInstance(context).busquedaDao()
        GlobalScope.launch(Dispatchers.Main) {
            val busquedaList = withContext(Dispatchers.IO) {
                busquedaDAO.getAll(cancion)
            }
            busquedaList?.let {
                _busqueda.postValue(it)
            }
        }
    }

    fun playSong(url : String){
        currentUrl = url
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(url)
        mediaPlayer?.setOnPreparedListener { mp ->
            mp.start()
            _isPlaying.postValue(true)
            _currentPosition.postValue(0)
            handler.postDelayed(updatePositionRunnable, 1000)
        }
        mediaPlayer?.prepareAsync()
    }

    fun stopSong(){
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        mediaPlayer?.release()
        mediaPlayer = null
        _isPlaying.postValue(false)
        _currentPosition.postValue(0)
        handler.removeCallbacks(updatePositionRunnable)
    }

    fun stopRunable(){
        mediaPlayer = null
        _isPlaying.postValue(false)
        _currentPosition.value = 0
        handler.removeCallbacks(updatePositionRunnable)
    }

    fun pauseSong() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            handler.removeCallbacks(updatePositionRunnable)
        } else {
            mediaPlayer?.start()
            handler.postDelayed(updatePositionRunnable, 1000)
        }
    }

    fun pruebaTouch(seg : Int){
        mediaPlayer!!.seekTo(seg * 1000)
        _currentPosition.value = seg
    }

    fun playPlayer(url: String?){
        if(url === null){
            println("No tiene url")
        }
        else if(currentUrl != url) {
            stopSong()
            playSong(url)
        }else{
            if (_isPlaying.value!!) {
                pauseSong()
            }else {
                playSong(url)
            }
        }
    }

    fun adelantar(url : String) {
        if (mediaPlayer != null && url != null) {

            if (_currentPosition.value!! + 5 < mediaPlayer!!.duration) {
                _currentPosition.value = _currentPosition.value!! + 5
                val currentPrueba = mediaPlayer!!.currentPosition
                val prueba = currentPrueba + 5000
                mediaPlayer!!.seekTo(prueba)
            }
        }
    }

    fun retroceder(url : String) {
        if (mediaPlayer != null && url != null) {
            if (_currentPosition.value!! - 5 > 0) {
                _currentPosition.value = _currentPosition.value!! - 5
                val currentPrueba = mediaPlayer!!.currentPosition
                val prueba = currentPrueba - 5000
                mediaPlayer!!.seekTo(prueba)
            }else {
                _currentPosition.postValue(0)
                mediaPlayer!!.seekTo(_currentPosition.value!!)

            }
        }
    }

    private val updatePositionRunnable = object : Runnable {
        override fun run() {

            if (_isPlaying.value == true) {
                val seconds = _currentPosition.value!! / 1000
                _currentPosition.value = _currentPosition.value!! + 1
                handler.postDelayed(this, 1000)
            }
        }
    }

    private val spotifyServiceToken: SpotifyService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://accounts.spotify.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(SpotifyService::class.java)
    }

    private val spotifyService: SpotifyService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.spotify.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(SpotifyService::class.java)
    }

    fun searchArtist(query: String) : LiveData<List<ImagenResponse>>{
        val clientId = "f13969da015a4f49bb1f1edef2185d4e"
        val clientSecret = "e3077426f4714315937111d5e82cd918"
        val base64Auth =
            Base64.encodeToString("$clientId:$clientSecret".toByteArray(), Base64.NO_WRAP)

        val tokenRequest = spotifyServiceToken.getAccessToken(
            "Basic $base64Auth",
            "client_credentials"
        )
        tokenRequest.enqueue(object : Callback<AccessTokenResponse> {
            override fun onResponse(
                call: Call<AccessTokenResponse>,
                response: Response<AccessTokenResponse>
            ) {
                if (response.isSuccessful) {
                    val accessTokenResponse = response.body()
                    val accessToken = accessTokenResponse?.accessToken

                    if (accessToken != null) {
                        val searchRequest = spotifyService.searchArtis("Bearer $accessToken", query,"CA")
                        searchRequest.enqueue(object : Callback<ImagenResponse> {
                            override fun onResponse(
                                call: Call<ImagenResponse>,
                                response: Response<ImagenResponse>
                            ) {
                                if (response.isSuccessful) {
                                    val artistResponse = response.body()
                                    val artistList = mutableListOf<ImagenResponse>()
                                    if (artistResponse != null && artistResponse.images.isNotEmpty() ) {
                                        artistList.add(artistResponse)
                                        _artist.postValue(artistList)

                                    } else {
                                        displayErrorMessage("No se encontraron canciones.")
                                    }


                                } else {
                                    System.out.println("Mensaje:    " + response.raw())
                                    displayErrorMessage("Error en la respuesta del servidor.")
                                }
                            }

                            override fun onFailure(call: Call<ImagenResponse>, t: Throwable) {
                                displayErrorMessage("Error en la solicitud de búsqueda.")
                            }
                        })
                    } else {
                        displayErrorMessage("Error al obtener el accessToken.")
                    }
                } else {
                    System.out.println("Mensaje:    " + response.raw())
                    displayErrorMessage("Error en la respuesta del servidor.")
                }
            }

            override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {
                displayErrorMessage("Error en la solicitud de accessToken.")
            }
        })

        return imagenes
    }

    fun searchTops(query: String): LiveData<List<topSong>> {
        val clientId = "f13969da015a4f49bb1f1edef2185d4e"
        val clientSecret = "e3077426f4714315937111d5e82cd918"
        val base64Auth = Base64.encodeToString("$clientId:$clientSecret".toByteArray(), Base64.NO_WRAP)

        val tokenRequest = spotifyServiceToken.getAccessToken(
            "Basic $base64Auth",
            "client_credentials"
        )

        tokenRequest.enqueue(object : Callback<AccessTokenResponse> {
            override fun onResponse(
                call: Call<AccessTokenResponse>,
                response: Response<AccessTokenResponse>
            ) {

                if (response.isSuccessful) {
                    try {
                        val accessTokenResponse = response.body()
                        val accessToken = accessTokenResponse?.accessToken

                        if (accessToken != null) {
                            val searchRequestAlbum = spotifyService.searchTop("Bearer $accessToken",query ,"CA")
                            searchRequestAlbum.enqueue(object : Callback<TopSongsResponse> {
                                override fun onResponse(
                                    call: Call<TopSongsResponse>,
                                    response: Response<TopSongsResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        val topResponse = response.body()
                                        val topList = mutableListOf<topSong>()
                                        if (topResponse != null && topResponse.tracks.isNotEmpty()) {
                                            for (topSong in topResponse!!.tracks) {
                                                val cancionTop = topSong
                                                cancionTop?.let{
                                                    topList.add(it)
                                                }

                                            }
                                            _topSong.postValue(topList)
                                        } else {
                                            displayErrorMessage("No se encontraron álbumes.")
                                        }

                                    } else {
                                        displayErrorMessage("Error en la respuesta del servidor.")
                                    }
                                }
                                override fun onFailure(call: Call<TopSongsResponse>, t: Throwable) {
                                    displayErrorMessage("Error en la solicitud de búsqueda.")
                                }
                            })
                        } else {
                            displayErrorMessage("Error al obtener el accessToken.")
                        }
                    } catch (e: Exception) {
                        displayErrorMessage("Error durante el procesamiento de la respuesta: ${e.message}")
                    }
                } else {
                    displayErrorMessage("Error en la respuesta del servidor.")
                }
            }

            override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {
                displayErrorMessage("Error en la solicitud de accessToken.")
            }
        })

        return topSongs
    }

    fun searchTracks(query: String) : LiveData<List<Track>>{
        val clientId = "f13969da015a4f49bb1f1edef2185d4e"
        val clientSecret = "e3077426f4714315937111d5e82cd918"
        val base64Auth =
            Base64.encodeToString("$clientId:$clientSecret".toByteArray(), Base64.NO_WRAP)

        val tokenRequest = spotifyServiceToken.getAccessToken(
            "Basic $base64Auth",
            "client_credentials"
        )
        tokenRequest.enqueue(object : Callback<AccessTokenResponse> {
            override fun onResponse(
                call: Call<AccessTokenResponse>,
                response: Response<AccessTokenResponse>
            ) {
                if (response.isSuccessful) {
                    val accessTokenResponse = response.body()
                    val accessToken = accessTokenResponse?.accessToken

                    if (accessToken != null) {
                        val searchRequest = spotifyService.searchTrack("Bearer $accessToken", query,"CA")
                        searchRequest.enqueue(object : Callback<TrackResponse> {
                            override fun onResponse(
                                call: Call<TrackResponse>,
                                response: Response<TrackResponse>
                            ) {

                                if (response.isSuccessful) {
                                    val trackResponse = response.body()
                                    val trackList = mutableListOf<Track>()
                                    if (trackResponse != null && trackResponse.tracks.items.isNotEmpty()) {
                                        for (track in trackResponse!!.tracks.items) {
                                            val cancion = track
                                            cancion?.let{
                                                trackList.add(it)
                                            }
                                        }
                                        _track.postValue(trackList)
                                    } else {
                                        displayErrorMessage("No se encontraron canciones.")
                                    }
                                } else {
                                    System.out.println("Mensaje:    " + response.raw())
                                    displayErrorMessage("Error en la respuesta del servidor.")
                                }
                            }

                            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                                displayErrorMessage("Error en la solicitud de búsqueda.")
                            }
                        })
                    } else {
                        displayErrorMessage("Error al obtener el accessToken.")
                    }
                } else {
                    System.out.println("Mensaje:    " + response.raw())
                    displayErrorMessage("Error en la respuesta del servidor.")
                }
            }

            override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {
                displayErrorMessage("Error en la solicitud de accessToken.")
            }
        })

        return traks
    }

    fun searchAlbums(query: String): LiveData<List<Item>> {
        val clientId = "f13969da015a4f49bb1f1edef2185d4e"
        val clientSecret = "e3077426f4714315937111d5e82cd918"
        val base64Auth = Base64.encodeToString("$clientId:$clientSecret".toByteArray(), Base64.NO_WRAP)

        val tokenRequest = spotifyServiceToken.getAccessToken(
            "Basic $base64Auth",
            "client_credentials"
        )

        tokenRequest.enqueue(object : Callback<AccessTokenResponse> {
            override fun onResponse(
                call: Call<AccessTokenResponse>,
                response: Response<AccessTokenResponse>
            ) {

                if (response.isSuccessful) {
                    try {
                        val accessTokenResponse = response.body()
                        val accessToken = accessTokenResponse?.accessToken

                        if (accessToken != null) {//"2256qKBSQdt53T5dz4Kdcs"
                            val searchRequestAlbum = spotifyService.searchAlbum("Bearer $accessToken", query,"CA")
                            searchRequestAlbum.enqueue(object : Callback<AlbumResponse> {
                                override fun onResponse(
                                    call: Call<AlbumResponse>,
                                    response: Response<AlbumResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        val albumResponse = response.body()
                                        val albumList = mutableListOf<Item>()
                                        if (albumResponse != null && albumResponse.href.isNotEmpty()) {
                                           for (album in albumResponse!!.items) {
                                                val cancion = album
                                                cancion?.let{
                                                    albumList.add(it)
                                                }

                                               _album.postValue(albumList)
                                            }
                                        } else {
                                            displayErrorMessage("No se encontraron álbumes.")
                                        }

                                    } else {
                                        displayErrorMessage("Error en la respuesta del servidor.")
                                    }
                                }
                                override fun onFailure(call: Call<AlbumResponse>, t: Throwable) {
                                    displayErrorMessage("Error en la solicitud de búsqueda.")
                                }
                            })
                        } else {
                            displayErrorMessage("Error al obtener el accessToken.")
                        }
                    } catch (e: Exception) {
                        displayErrorMessage("Error durante el procesamiento de la respuesta: ${e.message}")
                    }
                } else {
                    displayErrorMessage("Error en la respuesta del servidor.")
                }
            }

            override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {
                displayErrorMessage("Error en la solicitud de accessToken.")
            }
        })

        return albums
    }

    fun searchRelate(query: String) : LiveData<List<ArtistaRelated>>{
        val clientId = "f13969da015a4f49bb1f1edef2185d4e"
        val clientSecret = "e3077426f4714315937111d5e82cd918"
        val base64Auth = Base64.encodeToString("$clientId:$clientSecret".toByteArray(), Base64.NO_WRAP)

        val tokenRequest = spotifyServiceToken.getAccessToken(
            "Basic $base64Auth",
            "client_credentials"
        )

        tokenRequest.enqueue(object : Callback<AccessTokenResponse> {
            override fun onResponse(
                call: Call<AccessTokenResponse>,
                response: Response<AccessTokenResponse>
            ) {

                if (response.isSuccessful) {
                    try {
                        val accessTokenResponse = response.body()
                        val accessToken = accessTokenResponse?.accessToken

                        if (accessToken != null) {
                            val searchRequestAlbum = spotifyService.searchArtisRelacionado("Bearer $accessToken",query)
                            searchRequestAlbum.enqueue(object : Callback<ArtistaResponse> {
                                override fun onResponse(
                                    call: Call<ArtistaResponse>,
                                    response: Response<ArtistaResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        val topResponse = response.body()
                                        val topList = mutableListOf<ArtistaRelated>()
                                        if (topResponse != null ) {
                                            for (topSong in topResponse!!.artists) {
                                                val cancionTop = topSong
                                                cancionTop?.let{
                                                    topList.add(it)
                                                }
                                            }
                                            _relade.postValue(topList)
                                        } else {
                                            displayErrorMessage("No se encontraron álbumes.")
                                        }

                                    } else {
                                        displayErrorMessage("Error en la respuesta del servidor.")
                                    }
                                }
                                override fun onFailure(call: Call<ArtistaResponse>, t: Throwable) {
                                    displayErrorMessage("Error en la solicitud de búsqueda.")
                                }
                            })
                        } else {
                            displayErrorMessage("Error al obtener el accessToken.")
                        }
                    } catch (e: Exception) {
                        displayErrorMessage("Error durante el procesamiento de la respuesta: ${e.message}")
                    }
                } else {
                    displayErrorMessage("Error en la respuesta del servidor.")
                }
            }

            override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {
                displayErrorMessage("Error en la solicitud de accessToken.")
            }
        })

        return relades
    }

    private fun displayTrackInfo( trackName: String, artistName: String) {
        val message = "Canción encontrada: $trackName - $artistName"
        //  Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun displayErrorMessage( errorMessage: String) {
        println("error !!!!"+ errorMessage)
        //  Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

}



