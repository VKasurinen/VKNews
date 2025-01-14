package com.vkasurinen.vknews.presentation.homescreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vkasurinen.vknews.domain.model.Article
import com.vkasurinen.vknews.domain.repository.NewsRepository
import com.vkasurinen.vknews.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private var _state = MutableStateFlow(HomeState())
    val homeState = _state.asStateFlow()

    private var _topHeadlinesState = MutableStateFlow(TopHeadlinesState())
    val topHeadlinesState = _topHeadlinesState.asStateFlow()

    init {
        fetchNews()
        fetchTopHeadlines()
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.Navigate -> {
                // Handle navigation event if needed
            }
        }
    }

    //die-zeit
    private fun fetchNews() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            newsRepository.getNews(listOf("bbc-news", "abc-news", "business-insider"))
                .collectLatest { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { articles ->
                                Log.d("HomeViewModel", "Fetched news: $articles")
                                _state.update {
                                    it.copy(
                                        articles = articles,
                                        isLoading = false
                                    )
                                }
                            }
                        }

                        is Resource.Error -> {
                            _state.update { it.copy(isLoading = false) }
                        }

                        is Resource.Loading -> {
                            _state.update { it.copy(isLoading = result.isLoading) }
                        }
                    }
                }
        }
    }

    private fun fetchTopHeadlines() {
        viewModelScope.launch {
            _topHeadlinesState.update { it.copy(isLoading = true) }

            newsRepository.getTopHeadlines("us").collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { articles ->
                            val filteredArticles = articles.filter {
                                !it.title.contains("[Removed]")
                            }
                            Log.d("HomeViewModel2", "Fetched top headlines: $filteredArticles")
                            _topHeadlinesState.update {
                                it.copy(
                                    topHeadlines = filteredArticles,
                                    isLoading = false
                                )
                            }
                        }
                    }
                    is Resource.Error -> {
                        _topHeadlinesState.update { it.copy(isLoading = false) }
                    }
                    is Resource.Loading -> {
                        _topHeadlinesState.update { it.copy(isLoading = result.isLoading) }
                    }
                }
            }
        }
    }
}