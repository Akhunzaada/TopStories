package net.cocooncreations.topstories.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import net.cocooncreations.topstories.util.TestUtil
import net.cocooncreations.topstories.util.getOrAwaitValue
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StoryDaoTest : DbTest() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun insertAndRead() {
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
        db.storyDao().insert(story)
        val loaded = db.storyDao().load("uri://abc.com").getOrAwaitValue()
        assertThat(loaded, notNullValue())
        assertThat(loaded.section, `is`("science"))
        assertThat(loaded.title, `is`("Scientific Discovery"))
        assertThat(loaded.url, `is`("https://www.abc.com"))
        assertThat(loaded.saved, `is`(false))
        assertThat(loaded.multimedia.size, `is`(3))

        val first = loaded.multimedia[0]
        assertThat(first.url, `is`("https://www.abc.com/image.jpg"+0))
        assertThat(first.format, `is`("normal"+0))
        assertThat(first.type, `is`("image"))
    }

    @Test
    fun createIfNotExists_exists() {
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
        db.storyDao().insert(story)
        assertThat(db.storyDao().createStoryIfNotExists(story), `is`(-1L))
    }

    @Test
    fun createIfNotExists_doesNotExist() {
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
        assertThat(db.storyDao().createStoryIfNotExists(story), `is`(1L))
    }

    @Test
    fun insertStoryThenBookmarkStory() {
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
        db.storyDao().insert(story)
        val update = db.storyDao().load("uri://abc.com").getOrAwaitValue()
        update.saved = true
        db.storyDao().update(update)

        val loaded = db.storyDao().load("uri://abc.com").getOrAwaitValue()
        assertThat(loaded, notNullValue())
        assertThat(loaded.section, `is`("science"))
        assertThat(loaded.title, `is`("Scientific Discovery"))
        assertThat(loaded.url, `is`("https://www.abc.com"))
        assertThat(loaded.saved, `is`(true))
        assertThat(loaded.multimedia.size, `is`(3))

        val first = loaded.multimedia[0]
        assertThat(first.url, `is`("https://www.abc.com/image.jpg"+0))
        assertThat(first.format, `is`("normal"+0))
        assertThat(first.type, `is`("image"))
    }

    @Test
    fun bookmarkStoryThenRead() {
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
        db.storyDao().insert(story)
        assertThat(db.storyDao().loadSavedStories().getOrAwaitValue().size, `is`(0))

        val update = db.storyDao().load("uri://abc.com").getOrAwaitValue()
        update.saved = true
        db.storyDao().update(update)
        assertThat(db.storyDao().loadSavedStories().getOrAwaitValue().size, `is`(1))

        val loaded = db.storyDao().load("uri://abc.com").getOrAwaitValue()
        assertThat(loaded, notNullValue())
        assertThat(loaded.section, `is`("science"))
        assertThat(loaded.title, `is`("Scientific Discovery"))
        assertThat(loaded.url, `is`("https://www.abc.com"))
        assertThat(loaded.saved, `is`(true))
        assertThat(loaded.multimedia.size, `is`(3))

        val first = loaded.multimedia[0]
        assertThat(first.url, `is`("https://www.abc.com/image.jpg"+0))
        assertThat(first.format, `is`("normal"+0))
        assertThat(first.type, `is`("image"))
    }
}
