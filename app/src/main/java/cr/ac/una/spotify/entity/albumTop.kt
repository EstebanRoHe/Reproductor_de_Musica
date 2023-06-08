package cr.ac.una.spotify.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class albumTop (
    val name: String,
    val images : List<Image>
):Parcelable