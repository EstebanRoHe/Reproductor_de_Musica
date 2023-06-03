package cr.ac.una.spotify.viewModel

import android.content.Context
import android.util.Base64
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cr.ac.una.spotify.dao.SpotifyService
import cr.ac.una.spotify.entity.AccessTokenResponse
import cr.ac.una.spotify.entity.Track
import cr.ac.una.spotify.entity.TrackResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SpotifyViewModel : ViewModel() {

    private val _track: MutableLiveData<List<Track>> = MutableLiveData()
    val traks: LiveData<List<Track>> = _track

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
                                            /*
                                            track.album.images.map {
                                                println(it.url)
                                            }.joinToString(", ")

                                            System.out.println("| nombre : "+track.name + " | album : "+track.album.name+" | artista : ")//crar una entity imagen
                                            */

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

    private fun displayTrackInfo( trackName: String, artistName: String) {
        val message = "Canción encontrada: $trackName - $artistName"
      //  Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun displayErrorMessage( errorMessage: String) {
        println("error !!!!"+ errorMessage)
      //  Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }



}