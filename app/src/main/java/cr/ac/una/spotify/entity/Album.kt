package cr.ac.una.spotify.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Album (
    val images: List<Image>,
    val name:String,
    val artists: List<Artista>,
    val release_date : String,
    val id:String
    //val genres: List<Genero>
    )  : Parcelable