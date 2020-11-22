package net.cocooncreations.topstories.vo

import androidx.room.Entity
import androidx.room.Index
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import net.cocooncreations.topstories.db.NewsTypeConverters

@Entity(
        indices = [Index("uri")],
        primaryKeys = ["uri"]
)
@TypeConverters(NewsTypeConverters::class)
data class Story(
        @field:SerializedName("section")
        val section: String,
        @field:SerializedName("title")
        val title: String,
        @field:SerializedName("url")
        val url: String,
        @field:SerializedName("uri")
        val uri: String,
        @field:SerializedName("updated_date")
        val updatedDate: String,
        @field:SerializedName("saved")
        var saved: Boolean = false,
        @field:SerializedName("multimedia")
        val multimedia: List<Media>
)