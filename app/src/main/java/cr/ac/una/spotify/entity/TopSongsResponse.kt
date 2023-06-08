package cr.ac.una.spotify.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TopSongsResponse(
    val tracks : List<topSong>
): Parcelable