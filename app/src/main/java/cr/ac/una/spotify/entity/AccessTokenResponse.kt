package cr.ac.una.spotify.entity

import com.google.gson.annotations.SerializedName

class AccessTokenResponse (
    @SerializedName("access_token")
    val accessToken: String?
    )