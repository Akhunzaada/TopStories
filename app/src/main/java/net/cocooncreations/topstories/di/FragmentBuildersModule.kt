package net.cocooncreations.topstories.di

import net.cocooncreations.topstories.ui.story.TopStoriesFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.cocooncreations.topstories.ui.story.BookmarksFragment

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeTopStoriesFragment(): TopStoriesFragment

    @ContributesAndroidInjector
    abstract fun contributeBookmarkFragment(): BookmarksFragment
}
