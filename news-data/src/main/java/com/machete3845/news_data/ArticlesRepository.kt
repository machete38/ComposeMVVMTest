package com.machete3845.news_data

import com.machete3845.news_data.models.Article
import com.machete3845.news_database.NewsDatabase
import com.machete3845.news_database.RoomNewsDatabase
import com.machete3845.newsapi.NewsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ArticlesRepository(
    private val database: NewsDatabase,
    private val api: NewsApi
) {

    fun getAll(): RequestResult<Flow<List<Article>>>{
        return RequestResult.InProgress(
            database.articlesDao
                .getAll()
                .map { articles -> articles.map { it.toArticle() } }
        )
    }

    suspend fun search(query: String): Flow<Article>{
        api.everything()
        TODO("Not implemented")
    }

    sealed class RequestResult<E>(protected val data: E?) {
        class InProgress<E>(data: E?): RequestResult<E>(data)
        class Success<E>(data: E?): RequestResult<E>(data)
        class Error<E>(data: E?): RequestResult<E>(data)
    }
}