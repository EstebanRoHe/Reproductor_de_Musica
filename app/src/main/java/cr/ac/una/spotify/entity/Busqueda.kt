package cr.ac.una.spotify.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class Busqueda (
    @PrimaryKey(autoGenerate = true) val id: Long?,
    val busqueda: String,
    val fecha: Date,
        )