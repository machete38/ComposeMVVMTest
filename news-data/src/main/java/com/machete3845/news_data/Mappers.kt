package com.machete3845.news_data

import com.machete3845.news_data.models.Article
import com.machete3845.news_data.models.Source
import com.machete3845.news_database.models.ArticleDBO
import com.machete3845.news_database.models.SourceDBO
import com.machete3845.newsapi.models.ArticleDTO
import com.machete3845.newsapi.models.SourceDTO

internal fun ArticleDBO.toArticle(): Article =
    Article(
        id = id,
        source = sourceDBO?.toSource(),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content
    )

internal fun SourceDBO.toSource(): Source = Source(id = id, name = name)

internal fun SourceDTO.toSource(): Source = Source(id = id ?: name, name = name)

internal fun SourceDTO.toSourceDbo(): SourceDBO = SourceDBO(id = id ?: name, name = name)

internal fun ArticleDTO.toArticleDbo(): ArticleDBO =
    ArticleDBO(
        sourceDBO = source?.toSourceDbo(),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content
    )

internal fun ArticleDTO.toArticle(): Article =
    Article(
        source = source?.toSource(),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content
    )
