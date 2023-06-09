package cr.ac.una.spotify.converters

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class Converters {
    private val formato = SimpleDateFormat("dd/MM/yyyy")

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun fromFormattedDate(value: String?): Date? {
        return value?.let { formato.parse(it) }
    }

    @TypeConverter
    fun dateToFormattedDate(date: Date?): String? {
        return date?.let { formato.format(it) }
    }
}