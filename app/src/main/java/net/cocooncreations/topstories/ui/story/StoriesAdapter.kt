package net.cocooncreations.topstories.ui.story

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import net.cocooncreations.topstories.AppExecutors
import net.cocooncreations.topstories.R
import net.cocooncreations.topstories.databinding.StoryItemBinding
import net.cocooncreations.topstories.ui.common.DataBoundListAdapter
import net.cocooncreations.topstories.vo.Story

/**
 * A RecyclerView adapter for [Story] class.
 */
class StoriesAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val storyClickCallback: ((Story) -> Unit)?,
    private val bookmarkClickCallback: ((Story) -> Unit)?
) : DataBoundListAdapter<Story, StoryItemBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Story>() {
        override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem.uri == newItem.uri
        }

        override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem.title == newItem.title
        }
    }
) {

    override fun createBinding(parent: ViewGroup): StoryItemBinding {
        val binding = DataBindingUtil.inflate<StoryItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.story_item,
            parent,
            false,
            dataBindingComponent
        )
        binding.root.setOnClickListener {
            binding.story?.let {
                storyClickCallback?.invoke(it)
            }
        }
        binding.bookmark.setOnClickListener {
            binding.story?.let {
                bookmarkClickCallback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: StoryItemBinding, item: Story) {
        binding.story = item
    }
}
