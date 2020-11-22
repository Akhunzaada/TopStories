package net.cocooncreations.topstories.api

import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * REST API access points
 */
interface NewsService {
    @GET("svc/topstories/v2/home.json")
    fun getTopStories(): LiveData<ApiResponse<TopStoriesResponse>>
}
