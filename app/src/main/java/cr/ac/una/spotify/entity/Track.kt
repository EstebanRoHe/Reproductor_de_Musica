package cr.ac.una.spotify.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
@Parcelize
data class Track (
    val name: String,
    val album: Album,
    val uri: String
    ): Parcelable