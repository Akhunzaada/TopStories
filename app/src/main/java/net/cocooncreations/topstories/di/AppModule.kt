package net.cocooncreations.topstories.di

import android.app.Application
import androidx.room.Room
import net.cocooncreations.topstories.api.NewsService
import net.cocooncreations.topstories.db.NewsDb
import net.cocooncreations.topstories.util.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import net.cocooncreations.topstories.BuildConfig
import net.cocooncreations.topstories.api.ApiKeyInterceptor
import net.cocooncreations.topstories.db.StoryDao
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideNewsService(): NewsService {
        val client = OkHttpClient.Builder()
                .addInterceptor(ApiKeyInterceptor())
                .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(client)
            .build()
            .create(NewsService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): NewsDb {
        return Room
            .databaseBuilder(app, NewsDb::class.java, "news.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideStoryDao(db: NewsDb): StoryDao {
        return db.storyDao()
    }
}
