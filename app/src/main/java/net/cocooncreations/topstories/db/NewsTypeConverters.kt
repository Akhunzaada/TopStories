package net.cocooncreations.topstories.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import net.cocooncreations.topstories.vo.Media
import net.cocooncreations.topstories.vo.Story
import timber.log.Timber

object NewsTypeConverters {

    @TypeConverter
    @JvmStatic
    fun stringToMediaList(data: String?): List<Media>? {
        return Gson().fromJson(data, Array<Media>::class.java).toList()
    }

    @TypeConverter
    @JvmStatic
    fun mediaListToString(mediaList: List<Media>?): String? {
        return Gson().toJson(mediaList)
    }
}
