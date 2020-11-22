package net.cocooncreations.topstories.db

import androidx.room.Database
import androidx.room.RoomDatabase
import net.cocooncreations.topstories.vo.*

/**
 * Main database description.
 */
@Database(
    entities = [Story::class],
    version = 1,
    exportSchema = false
)
abstract class NewsDb : RoomDatabase() {
    abstract fun storyDao(): StoryDao
}
