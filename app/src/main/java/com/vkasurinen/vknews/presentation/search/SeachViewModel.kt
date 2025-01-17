package com.vkasurinen.vknews.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vkasurinen.vknews.domain.model.Article
import com.vkasurinen.vknews.domain.repository.NewsRepository
import com.vkasurinen.vknews.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class SearchViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {


    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state

    fun onEvent(event: SearchEvent) {
        // Handle events here
    }

    init {
        fetchNews()
    }

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

}
