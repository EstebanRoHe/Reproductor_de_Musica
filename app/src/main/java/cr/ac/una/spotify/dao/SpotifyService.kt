package cr.ac.una.spotify.dao

import cr.ac.una.spotify.entity.*
import retrofit2.http.*
import retrofit2.Call

interface SpotifyService {
    @FormUrlEncoded
    @POST("api/token")
    fun getAccessToken(
        @Header("Authorization") authorization: String,
        @Field("grant_type") grantType: String
    ): Call<AccessTokenResponse>

    @GET("v1/search?type=track")
    fun searchTrack(
        @Header("Authorization") authorization: String,
        @Query("q") query: String,
        @Query("market") market: String
    ): Call<TrackResponse>

      @GET("v1/albums/{albumId}/tracks")
      fun searchAlbum(
          @Header("Authorization") authorization: String,
          @Path("albumId") albumId: String,
          @Query("market") market: String
      ): Call<AlbumResponse>

      @GET("v1/artists/{id}/top-tracks")
      fun searchTop(
          @Header("Authorization") authorization: String,
          @Path("id") id: String,
          @Query("market") market: String
      ): Call<TopSongsResponse>
      //v1/artists/{id}
      @GET("v1/artists/{id}")
      fun searchArtis(
          @Header("Authorization") authorization: String,
          @Path("id") id: String,
          @Query("market") market: String
      ): Call<ImagenResponse>

    @GET("v1/artists/{id}/related-artists")
    fun searchArtisRelacionado(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): Call<ArtistaResponse>


}