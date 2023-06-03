package cr.ac.una.spotify.dao

import cr.ac.una.spotify.entity.AccessTokenResponse
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
}