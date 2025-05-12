package com.machete3845.news_main

import com.machete3845.news_data.ArticlesRepository
import com.machete3845.news_data.RequestResult
import com.machete3845.news_data.map
import com.machete3845.news_data.models.Article
import com.machete3845.news_main.models.ArticleUI
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map


internal class GetAllArticlesUseCase @Inject constructor(
    private val repository: ArticlesRepository
) {
    operator fun invoke(query: String): Flow<RequestResult<List<ArticleUI>>> {
        return repository.getAll(query)
            .map { requestResult ->
                requestResult.map { articles ->
                    articles.map {
                        it.toUiArticle()
                    }
                }
            }
            .flowOn(Dispatchers.IO)
    }
}


private fun Article.toUiArticle(): ArticleUI {
    return ArticleUI(
        id = id,
        title = title,
        description = description,
        imageUrl = urlToImage,
        url = url
    )
}
