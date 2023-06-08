package cr.ac.una.spotify.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TrackResponse (
    val tracks: Tracks
    ):Parcelable