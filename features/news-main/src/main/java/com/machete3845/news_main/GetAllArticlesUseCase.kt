package com.machete3845.news_main

import com.machete3845.news_data.ArticlesRepository
import com.machete3845.news_data.models.Article
import kotlinx.coroutines.flow.Flow

class GetAllArticlesUseCase(private  val repository: ArticlesRepository){
    operator fun invoke(): Flow<Article>{
        return repository.getAll()
    }
}