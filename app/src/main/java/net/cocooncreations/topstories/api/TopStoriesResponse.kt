package net.cocooncreations.topstories.api

import net.cocooncreations.topstories.vo.Story

data class TopStoriesResponse(
        val status: String,
        val results: List<Story>
)