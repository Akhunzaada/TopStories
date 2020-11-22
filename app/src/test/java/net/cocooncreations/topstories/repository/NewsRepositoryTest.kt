package net.cocooncreations.topstories.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import net.cocooncreations.topstories.api.ApiResponse
import net.cocooncreations.topstories.api.NewsService
import net.cocooncreations.topstories.api.TopStoriesResponse
import net.cocooncreations.topstories.db.NewsDb
import net.cocooncreations.topstories.db.StoryDao
import net.cocooncreations.topstories.util.AbsentLiveData
import net.cocooncreations.topstories.util.ApiUtil.successCall
import net.cocooncreations.topstories.util.InstantAppExecutors
import net.cocooncreations.topstories.util.TestUtil
import net.cocooncreations.topstories.util.mock
import net.cocooncreations.topstories.vo.Resource
import net.cocooncreations.topstories.vo.Story
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.reset
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

@RunWith(JUnit4::class)
class NewsRepositoryTest {
    private lateinit var repository: NewsRepository
    private val dao = mock(StoryDao::class.java)
    private val service = mock(NewsService::class.java)
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        val db = mock(NewsDb::class.java)
        `when`(db.storyDao()).thenReturn(dao)
        `when`(db.runInTransaction(ArgumentMatchers.any())).thenCallRealMethod()
        repository = NewsRepository(InstantAppExecutors(), dao, service)
    }

    @Test
    fun loadTopStoriesFromNetwork() {
        val dbData = MutableLiveData<List<Story>>()
        `when`(dao.loadStories()).thenReturn(dbData)

        val stories = TestUtil.createStories(3,
                TestUtil.createStory(
                        "science",
                        "Scientific Discovery",
                        "https://www.abc.com",
                        "uri://abc.com",
                        "2020-11-22T11:23:21-05:00",
                        false,
                        TestUtil.createMultimedia(3,
                                "https://www.abc.com/image.jpg",
                                "normal",
                                "image",
                                512,
                                512)
                )
        )
        val call = successCall(TopStoriesResponse("OK", stories))
        `when`(service.getTopStories()).thenReturn(call)

        val data = repository.loadTopStories()
        verify(dao).loadStories()
        verifyNoMoreInteractions(service)

        val observer = mock<Observer<Resource<List<Story>>>>()
        data.observeForever(observer)
        verifyNoMoreInteractions(service)
        verify(observer).onChanged(Resource.loading(null))
        val updatedDbData = MutableLiveData<List<Story>>()
        `when`(dao.loadStories()).thenReturn(updatedDbData)

        dbData.postValue(null)
        verify(service).getTopStories()
        verify(dao).insertStories(stories)

        updatedDbData.postValue(stories)
        verify(observer).onChanged(Resource.success(stories))
    }

    @Test
    fun loadTopStoriesFromNetworkError() {
        `when`(dao.loadStories()).thenReturn(AbsentLiveData.create())
        val apiResponse = MutableLiveData<ApiResponse<TopStoriesResponse>>()
        `when`(service.getTopStories()).thenReturn(apiResponse)

        val observer = mock<Observer<Resource<List<Story>>>>()
        repository.loadTopStories().observeForever(observer)
        verify(observer).onChanged(Resource.loading(null))

        apiResponse.postValue(ApiResponse.create(Exception("idk")))
        verify(observer).onChanged(Resource.error("idk", null))
    }
}