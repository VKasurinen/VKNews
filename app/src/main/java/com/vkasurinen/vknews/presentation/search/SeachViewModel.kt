package com.vkasurinen.vknews.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vkasurinen.vknews.domain.repository.NewsRepository
import com.vkasurinen.vknews.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state

    init {
        fetchNews()
    }

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.Search -> {
                searchNews(event.query)
            }
        }
    }

    private fun fetchNews() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            newsRepository.getNews(listOf("bbc-news", "abc-news", "business-insider"))
                .collectLatest { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { articles ->
                                _state.update {
                                    it.copy(
                                        articles = articles,
                                        filteredArticles = articles,
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

    private fun searchNews(query: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val filteredArticles = state.value.articles.filter {
                it.title.contains(query, ignoreCase = true) || it.source.name.contains(query, ignoreCase = true)
            }

            _state.update {
                it.copy(
                    filteredArticles = filteredArticles,
                    isLoading = false
                )
            }
        }
    }
}