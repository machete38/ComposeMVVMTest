package com.machete3845.news_main

import androidx.lifecycle.ViewModel
import com.machete3845.news_main.models.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class NewsMainViewModel : ViewModel() {

    private val _state = MutableStateFlow(State.None)

    val state: StateFlow<State>
        get() = _state.asStateFlow()
}

sealed class State{
    object None: State()
    class Loading: State()
    class Error: State()
    class Success(val articles: List<Article>): State()
}