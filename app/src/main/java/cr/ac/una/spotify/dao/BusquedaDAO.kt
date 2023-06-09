package cr.ac.una.spotify.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cr.ac.una.spotify.entity.Busqueda

@Dao
interface BusquedaDAO {
    @Insert
    fun insert(entity: Busqueda)

    @Query("SELECT * FROM busqueda WHERE busqueda LIKE '%' || :searchString || '%' ORDER BY fecha DESC")
    fun getAll(searchString: String): List<Busqueda>?


}