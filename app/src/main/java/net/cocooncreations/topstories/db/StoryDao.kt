package net.cocooncreations.topstories.db

import androidx.lifecycle.LiveData
import androidx.room.*
import net.cocooncreations.topstories.testing.OpenForTesting
import net.cocooncreations.topstories.vo.Story

/**
 * Interface for database access on Repo related operations.
 */
@Dao
@OpenForTesting
abstract class StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg story: Story)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertStories(stories: List<Story>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun update(story: Story)

    @Delete
    abstract fun delete(story: Story)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun createStoryIfNotExists(story: Story): Long

    @Query("SELECT * FROM Story WHERE uri = :uri")
    abstract fun load(uri: String): LiveData<Story>

    @Query("SELECT * FROM Story")
    abstract fun loadStories(): LiveData<List<Story>>

    @Query("SELECT * FROM Story WHERE saved = 1")
    abstract fun loadSavedStories(): LiveData<List<Story>>
}
