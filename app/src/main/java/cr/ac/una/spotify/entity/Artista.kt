package cr.ac.una.spotify.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Artista(
    val name:String,
    val id:String,
    //val images : List<Image>
    //val imagenes : List<Image>
): Parcelable
