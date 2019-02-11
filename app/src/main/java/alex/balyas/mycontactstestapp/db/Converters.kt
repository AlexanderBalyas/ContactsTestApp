package alex.balyas.mycontactstestapp.db

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson

/**
 * Created by Alex Balyas on 09.02.2019.
 */
class Converters {
    @TypeConverter
    fun listToJson(value: List<String>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<String>? {
        val objects = Gson().fromJson(value, Array<String>::class.java) as Array<String>
        return objects.toList()
    }
}