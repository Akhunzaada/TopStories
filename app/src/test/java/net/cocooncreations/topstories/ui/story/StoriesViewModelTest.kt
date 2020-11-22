package net.cocooncreations.topstories.ui.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import net.cocooncreations.topstories.repository.NewsRepository
import net.cocooncreations.topstories.util.mock
import net.cocooncreations.topstories.vo.Resource
import net.cocooncreations.topstories.vo.Story
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.reset
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

@RunWith(JUnit4::class)
class StoriesViewModelTest {
    @Rule
    @JvmField
    val instantExecutor = InstantTaskExecutorRule()
    private val repository = mock(NewsRepository::class.java)
    private lateinit var viewModel: StoriesViewModel

    @Before
    fun init() {
        // need to init after instant executor rule is established.
        viewModel = StoriesViewModel(repository)
    }

    @Test
    fun empty() {
        val result = mock<Observer<Resource<List<Story>>>>()
        viewModel.stories.observeForever(result)
        verifyNoMoreInteractions(repository)
    }

    @Test
    fun basic() {
        val result = mock<Observer<Resource<List<Story>>>>()
        viewModel.stories.observeForever(result)
        viewModel.refresh()
        verify(repository).loadTopStories()
        verify(repository, never()).loadSavedStories()
    }

    @Test
    fun refresh() {
        viewModel.refresh()
        verifyNoMoreInteractions(repository)
        viewModel.stories.observeForever(mock())
        verify(repository).loadTopStories()
        reset(repository)
        viewModel.refresh()
        verify(repository).loadTopStories()
    }

    @Test
    fun resetSameQuery() {
        viewModel.stories.observeForever(mock())
        viewModel.refresh()
        verify(repository).loadTopStories()
        reset(repository)
        viewModel.refresh()
        verify(repository).loadTopStories()
    }
}
