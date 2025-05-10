package com.machete3845.news_main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.machete3845.news_main.models.ArticleUI

@Composable
fun NewsMainScreen(){
    NewsMainScreen(viewModel = viewModel())
}

@Composable
internal fun NewsMainScreen(viewModel: NewsMainViewModel){
    val state by viewModel.state.collectAsState()
    when (val currentState = state){
        is State.Success -> Articles(currentState.articles)
        is State.Error -> ArticlesWithError(currentState.articles)
        is State.Loading -> ArticlesDuringUpdate(currentState.articles)
        State.None -> NewsEmpty()
    }
}

@Composable
private fun ArticlesWithError(articles: List<ArticleUI>?) {
    Box(Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.error),
        contentAlignment = Alignment.Center,)
    {
       Text("Error during update", color = MaterialTheme.colorScheme.onError)
    }
    if (articles != null)
    {
        Articles(articles)
    }
}

@Composable
private fun ArticlesDuringUpdate(articles: List<ArticleUI>?) {
   Box{
       Box(Modifier.fillMaxSize(),
           contentAlignment = Alignment.Center)
       {
           CircularProgressIndicator()
       }
       if (articles != null)
       {
           Articles(articles)
       }
   }
}

@Composable
private fun NewsEmpty() {
    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center){
        Text("No news")
    }
}

@Composable
private fun Articles(articles: List<ArticleUI>) {
    LazyColumn {
        items(articles){
            article ->
            key(article.id){
                ArticleItem(article)
            }
        }
    }
}

@Preview
@Composable
internal fun ArticleItem(@PreviewParameter(ArticlesPreviewProvider::class) article: ArticleUI){
    Column(modifier = Modifier.padding(8.dp)){
        Text(text = article.title ?: "No title", style = MaterialTheme.typography.headlineMedium, maxLines = 1)
        Spacer(modifier = Modifier.size(4.dp))
        Text(text = article.description ?: "No description", style = MaterialTheme.typography.bodyMedium, maxLines = 3)
    }
}


internal class ArticlePreviewProvider : PreviewParameterProvider<ArticleUI>{

    override val values =
        sequenceOf(
            ArticleUI(
                1,
                "Anroid studio Iguana is stable!",
                "new stable version of Android IDE has been released",
                null,
                ""),
            ArticleUI(
                2,
                "Gemini 1.5 Release",
                "Google AI Updated",
                null,
                ""),
            ArticleUI(
                3,
                "Shape animations (10 min)",
                "How to use shape animations",
                null,
                "")
        )
}

private class ArticlesPreviewProvider : PreviewParameterProvider<List<ArticleUI>>
{
    private val articleProivder = ArticlePreviewProvider()

    override val values = sequenceOf(
        articleProivder.values
            .toList()
    )

}
