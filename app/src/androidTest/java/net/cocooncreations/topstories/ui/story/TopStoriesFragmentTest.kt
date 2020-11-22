package net.cocooncreations.topstories.ui.story

import android.view.KeyEvent
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import net.cocooncreations.topstories.R
import net.cocooncreations.topstories.binding.FragmentBindingAdapters
import net.cocooncreations.topstories.util.CountingAppExecutorsRule
import net.cocooncreations.topstories.util.DataBindingIdlingResourceRule
import net.cocooncreations.topstories.util.RecyclerViewMatcher
import net.cocooncreations.topstories.util.TaskExecutorWithIdlingResourceRule
import net.cocooncreations.topstories.util.TestUtil
import net.cocooncreations.topstories.util.ViewModelUtil
import net.cocooncreations.topstories.util.disableProgressBarAnimations
import net.cocooncreations.topstories.util.mock
import net.cocooncreations.topstories.vo.Resource
import net.cocooncreations.topstories.vo.Story
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class TopStoriesFragmentTest {
    @Rule
    @JvmField
    val executorRule = TaskExecutorWithIdlingResourceRule()
    @Rule
    @JvmField
    val countingAppExecutors = CountingAppExecutorsRule()
    @Rule
    @JvmField
    val dataBindingIdlingResourceRule = DataBindingIdlingResourceRule()

    private lateinit var mockBindingAdapter: FragmentBindingAdapters
    private lateinit var viewModel: StoriesViewModel
    private val navController = mock<NavController>()
    private val stories = MutableLiveData<Resource<List<Story>>>()

    @Before
    fun init() {
        viewModel = mock(StoriesViewModel::class.java)
        `when`(viewModel.stories).thenReturn(stories)

        mockBindingAdapter = mock(FragmentBindingAdapters::class.java)

        val scenario = launchFragmentInContainer(
                themeResId = R.style.AppTheme) {
            TopStoriesFragment().apply {
                appExecutors = countingAppExecutors.appExecutors
                viewModelFactory = ViewModelUtil.createFor(viewModel)
                dataBindingComponent = object : DataBindingComponent {
                    override fun getFragmentBindingAdapters(): FragmentBindingAdapters {
                        return mockBindingAdapter
                    }
                }
            }
        }
        dataBindingIdlingResourceRule.monitorFragment(scenario)
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
            fragment.disableProgressBarAnimations()
        }
    }

    @Test
    fun loadStories() {
        val story = TestUtil.createStory(
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
        stories.postValue(Resource.success(arrayListOf(story)))
        onView(listMatcher().atPosition(0)).check(matches(hasDescendant(withText("Scientific Discovery"))))
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
    }

    @Test
    fun dataWithLoading() {
        val story = TestUtil.createStory(
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
        stories.postValue(Resource.loading(arrayListOf(story)))
        onView(listMatcher().atPosition(0)).check(matches(hasDescendant(withText("Scientific Discovery"))))
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
    }

    @Test
    fun error() {
        stories.postValue(Resource.error("failed to load", null))
        onView(withId(R.id.error_msg)).check(matches(isDisplayed()))
    }

    private fun listMatcher(): RecyclerViewMatcher {
        return RecyclerViewMatcher(R.id.stories_list)
    }
}