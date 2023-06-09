package cr.ac.una.spotify.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cr.ac.una.spotify.converters.Converters
import cr.ac.una.spotify.dao.BusquedaDAO
import cr.ac.una.spotify.entity.Busqueda


@Database(entities = [Busqueda::class], version = 1)//se agregan las entitys
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun busquedaDao(): BusquedaDAO


    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "busqueda-database"
                    ).build()
                }
            }
            return instance!!
        }
    }
}