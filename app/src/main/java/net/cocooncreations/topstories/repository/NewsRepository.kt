package net.cocooncreations.topstories.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.cocooncreations.topstories.AppExecutors
import net.cocooncreations.topstories.api.*
import net.cocooncreations.topstories.db.NewsDb
import net.cocooncreations.topstories.db.StoryDao
import net.cocooncreations.topstories.testing.OpenForTesting
import net.cocooncreations.topstories.util.AbsentLiveData
import net.cocooncreations.topstories.util.RateLimiter
import net.cocooncreations.topstories.vo.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles Story instances.
 */
@Singleton
@OpenForTesting
class NewsRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val storyDao: StoryDao,
        private val newsService: NewsService
) {

    fun loadTopStories(): LiveData<Resource<List<Story>>> {
        return object : NetworkBoundResource<List<Story>, TopStoriesResponse>(appExecutors) {

            override fun saveCallResult(item: TopStoriesResponse) {
                storyDao.insertStories(item.results)
            }

            override fun shouldFetch(data: List<Story>?) = true

            override fun loadFromDb(): LiveData<List<Story>> {
                return storyDao.loadStories()
            }

            override fun createCall() = newsService.getTopStories()
        }.asLiveData()
    }

    fun loadSavedStories(): LiveData<Resource<List<Story>>> {
        return object : NetworkBoundResource<List<Story>, TopStoriesResponse>(appExecutors) {

            override fun saveCallResult(item: TopStoriesResponse) {}

            override fun shouldFetch(data: List<Story>?) = false

            override fun loadFromDb(): LiveData<List<Story>> {
                return storyDao.loadSavedStories()
            }

            override fun createCall(): LiveData<ApiResponse<TopStoriesResponse>> = AbsentLiveData.create()
        }.asLiveData()
    }

    suspend fun bookmarkStory(story: Story) =
            GlobalScope.launch {
                story.saved = !story.saved
                storyDao.update(story)
            }
}
