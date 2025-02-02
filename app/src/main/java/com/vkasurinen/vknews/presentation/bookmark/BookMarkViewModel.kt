package com.vkasurinen.vknews.presentation.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vkasurinen.vknews.domain.repository.NewsRepository
import com.vkasurinen.vknews.presentation.details.DetailsState
import com.vkasurinen.vknews.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookMarkViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BookMarkState())
    val state = _state.asStateFlow()

    init {
        loadBookmarkedArticles()
    }

    fun refreshBookmarkedArticles() {
        loadBookmarkedArticles()
    }

    private fun loadBookmarkedArticles() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            newsRepository.getBookmarkedArticles().collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { articles ->
                            _state.update {
                                it.copy(
                                    articles = articles.filter { it.isBookmarked },
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