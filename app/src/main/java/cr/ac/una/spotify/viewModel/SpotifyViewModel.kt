package cr.ac.una.spotify.viewModel

import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cr.ac.una.spotify.dao.SpotifyService
import cr.ac.una.spotify.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class SpotifyViewModel : ViewModel() {

    private val _track: MutableLiveData<List<Track>> = MutableLiveData()
    val traks: LiveData<List<Track>> = _track
    private val _album: MutableLiveData<List<Item>> = MutableLiveData()
    val albums: LiveData<List<Item>> = _album

    private val _topSong: MutableLiveData<List<topSong>> = MutableLiveData()
    val topSongs: LiveData<List<topSong>> = _topSong



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
                                                topResponse.tracks.forEach{element->
                                                    println(element.popularity)
                                                    println(element.album.name)
                                                    println(element.album.images[0])
                                                    println(element.artists.joinToString (", "){it.name})
                                                }
                                                _topSong.postValue(topList)
                                            }
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

///////hasta aqui
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
                        val searchRequest = spotifyService.searchTrack("Bearer $accessToken", query)
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
                            val searchRequestAlbum = spotifyService.searchAlbum("Bearer $accessToken", query)
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



    private fun displayTrackInfo( trackName: String, artistName: String) {
        val message = "Canción encontrada: $trackName - $artistName"
        //  Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    private fun displayErrorMessage( errorMessage: String) {
        println("error !!!!"+ errorMessage)
        //  Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

/*

fun prueba() {
    val clientId = "f13969da015a4f49bb1f1edef2185d4e"
    val clientSecret = "e3077426f4714315937111d5e82cd918"
    val base64Auth = Base64.encodeToString("$clientId:$clientSecret".toByteArray(), Base64.NO_WRAP)
    val tokenRequest = spotifyServiceToken.getAccessToken("Basic $base64Auth", "client_credentials")
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val tokenResponse = tokenRequest.execute()
            if (tokenResponse.isSuccessful) {
                val accessTokenResponse = tokenResponse.body()
                val accessToken = accessTokenResponse?.accessToken

                if (accessToken != null) {
                    val searchRequest = spotifyService.searchTop("Bearer $accessToken","0iEtIxbK0KxaSlF7G42ZOp","CA")
                    val client = OkHttpClient()
                    try {
                        val response = client.newCall(searchRequest.request()).execute()
                        if (response.isSuccessful) {
                            val rawResponse = response.body?.string()
                            println("!!!!!!!!!!!!!!!!!!!!mierda"+ rawResponse)
                        } else {
                            println("no risrvo esast de api")
                        }
                    } catch (e: IOException) {
                        println("error de mierdaaaaaa")

                    }
                } else {
                    println("toekn nulo")
                }
            } else {
                println("no se")
            }
        } catch (e: Exception) {
            println("mierdaaaaaa")
        }
    }
}


*/


}

