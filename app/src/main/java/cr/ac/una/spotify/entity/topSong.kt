package cr.ac.una.spotify.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class topSong (
    val album : albumTop,
    val popularity : Int,
    val artists : List<Artista>
):Parcelable