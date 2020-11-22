package net.cocooncreations.topstories.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.cocooncreations.topstories.ui.story.StoriesViewModel
import net.cocooncreations.topstories.viewmodel.NewsViewModelFactory

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(StoriesViewModel::class)
    abstract fun bindStoriesViewModel(storiesViewModel: StoriesViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: NewsViewModelFactory): ViewModelProvider.Factory
}
