package utils

import androidx.room.TypeConverter
import java.text.DateFormat
import java.util.Date

internal class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): Date? = value?.let { DateFormat.getDateTimeInstance().parse(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): String? = date?.time?.let { DateFormat.getDateTimeInstance().format(it) }
}
