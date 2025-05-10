package com.machete3845.news_data

import com.machete3845.common.Logger
import com.machete3845.news_data.models.Article
import com.machete3845.news_database.NewsDatabase
import com.machete3845.news_database.models.ArticleDBO
import com.machete3845.newsapi.NewsApi
import com.machete3845.newsapi.models.ArticleDTO
import com.machete3845.newsapi.models.ResponseDTO
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach


class ArticlesRepository @Inject constructor(
    private val database: NewsDatabase,
    private val api: NewsApi,
    private val logger: Logger
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAll(
        query: String,
        mergeStrategy: MergeStrategy<RequestResult<List<Article>>> = DefaultRequestResponseMergeStrategy(),
    ): Flow<RequestResult<List<Article>>> {


        val cachedAllArticles: Flow<RequestResult<List<Article>>> = getAllFromDatabase()
        val remoteArticles: Flow<RequestResult<List<Article>>> = getAllFromServer(query)


        return cachedAllArticles.combine(remoteArticles, mergeStrategy::merge)
            .flatMapLatest { result ->
                if (result is RequestResult.Success)
                {
                    database.articlesDao.observeAll()
                        .map { dbos -> dbos.map { it.toArticle() } }
                        .map { RequestResult.Success(it) }
                }
                else
                {
                  flowOf(result)
                }
            }
    }

    private fun getAllFromServer(query: String): Flow<RequestResult<List<Article>>> {
        val apiRequest = flow { emit(api.everything(query))}
            .onEach { result ->
                when{
                    result.isSuccess -> saveArticlesToCache(result.getOrThrow().articles)
                    result.isFailure -> logger.e(LOG_TAG, "Error getting from server: ${result.exceptionOrNull()}")
                }

            }
            .map { it.toRequestResult() }
       val start =  flowOf<RequestResult<ResponseDTO<ArticleDTO>>>((RequestResult.InProgress()))

        return merge(apiRequest, start).map {
                result -> result.map {
                response ->
            response.articles.map { it.toArticle() }
        }
        }
    }

    private suspend fun saveArticlesToCache(data: List<ArticleDTO>) {
        val dbos = data.map { articleDto -> articleDto.toArticleDbo() }
        database.articlesDao.insert(dbos)
    }

    private fun getAllFromDatabase(): Flow<RequestResult<List<Article>>> {
        val dbRequest: Flow<RequestResult<List<ArticleDBO>>> = database.articlesDao::getAll.asFlow()
            .map { RequestResult.Success(it) }
            .catch {
                RequestResult.Error<List<ArticleDBO>>(error = it)
                logger.e(LOG_TAG, "Error getting from DB: ${it.message}")
            }
        val start = flowOf<RequestResult<List<ArticleDBO>>>(RequestResult.InProgress())
        return merge(start, dbRequest).map {
                result ->
            result.map { articlesDbos ->
                articlesDbos.map { it.toArticle() }
            }
        }
    }

    suspend fun search(query: String): Flow<Article> {
        api.everything()
        TODO("Not implemented")
    }

    private companion object {
        const val LOG_TAG = "ArticlesRepository"
    }
}



