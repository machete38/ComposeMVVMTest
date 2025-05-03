package com.machete3845.news_main

import com.machete3845.news_data.ArticlesRepository
import com.machete3845.news_data.RequestResult
import com.machete3845.news_data.map
import com.machete3845.news_data.models.Article
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class GetAllArticlesUseCase @Inject constructor(
    private val repository: ArticlesRepository){
    operator fun invoke(): Flow<RequestResult<List<com.machete3845.news_main.models.Article>>>{
        return repository.getAll()
            .map { requestResult ->
                requestResult.map { articles ->
                    articles.map {
                        it.toUiArticle()
                    }
                }
            }
    }
}


private fun Article.toUiArticle(): com.machete3845.news_main.models.Article {
    TODO("Not yet implemented")
}
