package com.vkasurinen.vknews.presentation.homescreen

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
    val state = _state.asStateFlow()

    init {
        fetchNews()
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.Navigate -> {
                // Handle navigation event if needed
            }
        }
    }

    private fun fetchNews() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            newsRepository.getNews(listOf("bbc-news", "abc-news", "die-zeit", "business-insider"))
                .collectLatest { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            resource.data?.let { articles ->
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
                            _state.update { it.copy(isLoading = resource.isLoading) }
                        }
                    }
                }
        }
    }
}

//    private fun searchNews(query: String) {
//        viewModelScope.launch {
//            _state.update { it.copy(isLoading = true) }
//
//            val filteredArticles = state.value.articles.filter {
//                it.title.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true)
//            }
//
//            _state.update {
//                it.copy(
//                    articles = filteredArticles,
//                    isLoading = false
//                )
//            }
//        }
//    }
//}