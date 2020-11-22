package net.cocooncreations.topstories.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.cocooncreations.topstories.repository.NewsRepository
import net.cocooncreations.topstories.testing.OpenForTesting
import net.cocooncreations.topstories.util.AbsentLiveData
import net.cocooncreations.topstories.vo.Resource
import net.cocooncreations.topstories.vo.Story
import javax.inject.Inject

@OpenForTesting
class StoriesViewModel @Inject constructor(val newsRepository: NewsRepository) : ViewModel() {

    private val _query = MutableLiveData<Boolean>()

    val listUpdate = MutableLiveData<Boolean>()

    val stories: LiveData<Resource<List<Story>>> = _query.switchMap {
        if (it.not()) {
            AbsentLiveData.create()
        } else {
            newsRepository.loadTopStories()
        }
    }

    val savedStories: LiveData<Resource<List<Story>>> = _query.switchMap {
        if (it.not()) {
            AbsentLiveData.create()
        } else {
            newsRepository.loadSavedStories()
        }
    }

    fun refresh() {
        _query.value = true
    }

    fun bookmarkStory(story: Story, refresh: Boolean = false) {
        GlobalScope.launch(Dispatchers.Main) {
            newsRepository.bookmarkStory(story)
            listUpdate.value = true
            if (refresh) refresh()
        }
    }
}
