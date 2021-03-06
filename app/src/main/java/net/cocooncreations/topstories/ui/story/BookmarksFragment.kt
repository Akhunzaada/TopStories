package net.cocooncreations.topstories.ui.story

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import net.cocooncreations.topstories.AppExecutors
import net.cocooncreations.topstories.R
import net.cocooncreations.topstories.binding.FragmentDataBindingComponent
import net.cocooncreations.topstories.databinding.StoriesFragmentBinding
import net.cocooncreations.topstories.di.Injectable
import net.cocooncreations.topstories.ui.common.RetryCallback
import net.cocooncreations.topstories.util.autoCleared
import javax.inject.Inject

class BookmarksFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<StoriesFragmentBinding>()

    var adapter by autoCleared<StoriesAdapter>()

    val bookmarksViewModel: StoriesViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.stories_fragment,
            container,
            false,
            dataBindingComponent
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = viewLifecycleOwner
        val rvAdapter = StoriesAdapter(
                dataBindingComponent = dataBindingComponent,
                appExecutors = appExecutors,
                storyClickCallback = { story ->
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(story.url)))
                })
        { story ->
            bookmarksViewModel.bookmarkStory(story, true)
        }
        binding.storiesList.adapter = rvAdapter
        adapter = rvAdapter

        binding.callback = object : RetryCallback {
            override fun retry() {
                bookmarksViewModel.refresh()
            }
        }
        initRecyclerView()
        bookmarksViewModel.refresh()
    }

    private fun initRecyclerView() {
        binding.topStories = bookmarksViewModel.stories
        bookmarksViewModel.savedStories.observe(viewLifecycleOwner, Observer { result ->
            adapter.submitList(result?.data)
        })
    }
}
