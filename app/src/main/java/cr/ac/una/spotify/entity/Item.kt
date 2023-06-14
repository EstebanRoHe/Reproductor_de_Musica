package cr.ac.una.spotify.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item (
       val href:String,
       val name : String,
       val preview_url:String
): Parcelable