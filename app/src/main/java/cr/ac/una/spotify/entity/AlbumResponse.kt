package cr.ac.una.spotify.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AlbumResponse (
    val items: List<Item>,
    val href : String
): Parcelable