package com.machete3845.news_data

import com.machete3845.news_data.models.Article
import com.machete3845.news_database.NewsDatabase
import com.machete3845.news_database.models.ArticleDBO
import com.machete3845.newsapi.NewsApi
import com.machete3845.newsapi.models.ArticleDTO
import com.machete3845.newsapi.models.ResponseDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

class ArticlesRepository(
    private val database: NewsDatabase,
    private val api: NewsApi,
    private val mergeStrategy: RequestResponseMergeStrategy<List<Article>>,
) {

    fun getAll(): Flow<RequestResult<List<Article>>> {

        val cachedAllArticles = getAllFromDatabase()
            .map {
                result ->
                result.map { articlesDbos ->
                    articlesDbos.map { it.toArticle() }
                }
            }

        val remoteArticles: Flow<RequestResult<List<Article>>> = getAllFromServer().map {
            result -> result.map {
                response ->
                response.articles.map { it.toArticle() }
        }
        }


        return cachedAllArticles.combine(remoteArticles){ dbos: RequestResult<List<Article>>, dtos: RequestResult<List<Article>> ->
            mergeStrategy.merge(dbos, dtos)
        }
    }

    private fun getAllFromServer(): Flow<RequestResult<ResponseDTO<ArticleDTO>>> {
        val apiRequest = flow { emit(api.everything())}
            .onEach { result ->
                if (result.isSuccess) {
                    saveNetResopnseToCache(checkNotNull(result.getOrThrow()).articles)
                }
            }
            .map { it.toRequestResult() }
       val start =  flowOf<RequestResult<ResponseDTO<ArticleDTO>>>((RequestResult.InProgress()))

        return merge(apiRequest, start)
    }

    private suspend fun saveNetResopnseToCache(data: List<ArticleDTO>) {
        val dbos = data.map { articleDto -> articleDto.toArticleDbo() }
        database.articlesDao.insert(dbos)
    }

    private fun getAllFromDatabase(): Flow<RequestResult<List<ArticleDBO>>> {
        val dbRequest: Flow<RequestResult<List<ArticleDBO>>> = database.articlesDao
            .getAll()
            .map { RequestResult.Success(it) }
        val start = flowOf<RequestResult<List<ArticleDBO>>>(RequestResult.InProgress())
        return merge(start, dbRequest)
    }

    suspend fun search(query: String): Flow<Article> {
        api.everything()
        TODO("Not implemented")
    }
}



