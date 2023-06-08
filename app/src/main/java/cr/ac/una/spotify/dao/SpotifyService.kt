package cr.ac.una.spotify.dao

import cr.ac.una.spotify.entity.AccessTokenResponse
import cr.ac.una.spotify.entity.AlbumResponse
import cr.ac.una.spotify.entity.TopResponse
import cr.ac.una.spotify.entity.TrackResponse
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
        @Query("q") query: String
    ): Call<TrackResponse>

      @GET("v1/albums/{albumId}/tracks")
      fun searchAlbum(
          @Header("Authorization") authorization: String,
          @Path("albumId") albumId: String
      ): Call<AlbumResponse>
//I/System.out: !!! Id del artista !!!!! 0iEtIxbK0KxaSlF7G42ZOp, 1URnnhqYAYcrqrcwql10ft

    @GET("v1/artists/{id}/top-tracks")
    fun searchTop(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Query("market") market: String
    ): Call<TopResponse>




}