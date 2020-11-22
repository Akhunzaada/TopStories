package net.cocooncreations.topstories.util

import net.cocooncreations.topstories.vo.*

object TestUtil {

    fun createStory(section: String, title: String, url: String, uri: String, updatedDate: String,
                    saved: Boolean, multimedia: List<Media>) = Story(
            section = section,
            title = title,
            url = url,
            uri = uri,
            updatedDate = updatedDate,
            saved = saved,
            multimedia = multimedia
    )

    fun createStories(count: Int, story: Story): List<Story> {
        return (0 until count).map {
            createStory(
                    section = story.section + it,
                    title = story.title + it,
                    url = story.url + it,
                    uri = story.uri + it,
                    updatedDate = story.updatedDate,
                    saved = story.saved,
                    multimedia = story.multimedia
            )
        }
    }

    fun createMultimedia(count: Int, url: String, format: String, type: String, height: Int, width: Int) =
            (0 until count).map {
                Media(
                        url = url + it,
                        format = format + it,
                        type = type,
                        height = height + it,
                        width = width + it
                )
            }
}
