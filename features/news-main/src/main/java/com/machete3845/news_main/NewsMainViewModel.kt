package com.machete3845.news_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.machete3845.news_data.RequestResult
import com.machete3845.news_main.models.ArticleUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
internal class NewsMainViewModel @Inject constructor(
    getAllArticlesUseCase: Provider<GetAllArticlesUseCase>,
) : ViewModel() {

    val state: StateFlow<State> = getAllArticlesUseCase.get().invoke("android")
    .map { it.toState() }
    .stateIn(viewModelScope, SharingStarted.Lazily, State.None)


}

private fun RequestResult<List<ArticleUI>>.toState(): State {
   return when (this){
       is RequestResult.Error<*> -> State.Error(data)
       is RequestResult.InProgress<*> -> State.Loading(data)
       is RequestResult.Success<*> -> State.Success(checkNotNull(data))
   }
}

internal sealed class State(val articles: List<ArticleUI>?){
    object None: State(articles = null)
    class Loading(articles: List<ArticleUI>? = null): State(articles)
    class Error(articles: List<ArticleUI>? = null): State(articles)
    class Success(articles: List<ArticleUI>): State(articles)
}

