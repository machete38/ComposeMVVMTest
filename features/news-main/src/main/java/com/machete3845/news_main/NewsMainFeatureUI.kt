package com.machete3845.news_main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.machete3845.news_main.models.ArticleUI
import com.machete3845.news_uikit.ComposeMVVMTheme

@Composable
fun NewsMainScreen() {
    NewsMainScreen(viewModel = viewModel())
}

@Composable
internal fun NewsMainScreen(viewModel: NewsMainViewModel) {
    val state by viewModel.state.collectAsState()
    val currentState = state
    if (currentState != State.None) {
        Column {
            if (currentState is State.Loading) {
                ProgressIndicator(currentState)
            }
            if (currentState is State.Error) {
                ErrorMessage(currentState)
            }
            if (currentState.articles != null) {
                Articles(articles = currentState.articles)
            }
        }
    }
}

@Composable
private fun ProgressIndicator(state: State.Loading) {
    Box(
        Modifier
            .fillMaxWidth()
            .size(100.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorMessage(state: State.Error) {
    Box(
        Modifier
            .fillMaxWidth()
            .size(100.dp)
            .background(ComposeMVVMTheme.colorScheme.error),
        contentAlignment = Alignment.Center
    ) {
        Text("Error during update", color = ComposeMVVMTheme.colorScheme.onError)
    }
}

@Composable
private fun Articles(articles: List<ArticleUI>) {
    LazyColumn {
        items(articles) { article ->
            key(article.id) {
                ArticleItem(article)
            }
        }
    }
}

@Preview
@Composable
internal fun ArticleItem(
    @PreviewParameter(ArticlesPreviewProvider::class) article: ArticleUI
) {
    Row(Modifier.padding(8.dp)) {
        if (article.imageUrl != null) {
            var isImageVisible by remember { mutableStateOf(true) }
            AsyncImage(
                model = article.imageUrl,
                onState = { state ->
                    if (state is AsyncImagePainter.State.Error) {
                        isImageVisible = false
                    }
                },
                contentDescription = stringResource(R.string.article_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.size(4.dp))
        }
        Column(Modifier.padding(vertical = 4.dp, horizontal = 8.dp)) {
            Text(
                text = article.title ?: stringResource(R.string.no_title),
                style = ComposeMVVMTheme.typography.headlineMedium,
                maxLines = 2,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.2.sp
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = article.description ?: stringResource(R.string.no_description),
                style = ComposeMVVMTheme.typography.bodyMedium,
                maxLines = 4,
                fontSize = 14.sp
            )
        }
    }
}

internal class ArticlePreviewProvider : PreviewParameterProvider<ArticleUI> {
    override val values =
        sequenceOf(
            ArticleUI(
                1,
                "Anroid studio Iguana is stable!",
                "new stable version of Android IDE has been released",
                null,
                ""
            ),
            ArticleUI(
                2,
                "Gemini 1.5 Release",
                "Google AI Updated",
                null,
                ""
            ),
            ArticleUI(
                3,
                "Shape animations (10 min)",
                "How to use shape animations",
                null,
                ""
            )
        )
}

private class ArticlesPreviewProvider : PreviewParameterProvider<List<ArticleUI>> {
    private val articleProivder = ArticlePreviewProvider()

    override val values =
        sequenceOf(
            articleProivder.values
                .toList()
        )
}
