package net.cocooncreations.topstories.api

import net.cocooncreations.topstories.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalUrl = original.url()
        val newUrl = originalUrl.newBuilder()
                .addQueryParameter("api-key", BuildConfig.API_KEY)
                .build()

        val requestBuilder = original.newBuilder().url(newUrl)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}