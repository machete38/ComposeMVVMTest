package com.machete3845.composemvvmtest

import android.content.Context
import com.machete3845.common.AndroidLogcatLogger
import com.machete3845.common.AppDispatchers
import com.machete3845.common.Logger
import com.machete3845.news_database.NewsDatabase
import com.machete3845.newsapi.NewsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient? {
        return if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor().setLevel(
                HttpLoggingInterceptor.Level.BODY
            )
            OkHttpClient.Builder().addInterceptor(logging).build()
        } else {
            null
        }
    }
    @Provides
    @Singleton
    fun provideNewsApi(okHttpClient: OkHttpClient?): NewsApi {
        return NewsApi(
            baseUrl = BuildConfig.NEWS_API_BASE_URL,
            apiKey = BuildConfig.NEWS_API_KEY,
            okHttpClient = okHttpClient
        )
    }

    @Provides
    @Singleton
    fun provideNewsDatabase(@ApplicationContext context: Context): NewsDatabase {
        return NewsDatabase(context)
    }

    @Provides
    @Singleton
    fun provideAppCoroutineDispatchers(): AppDispatchers = AppDispatchers()

    @Provides
    fun provideLogger(): Logger = AndroidLogcatLogger()

}