package com.vkasurinen.vknews.presentation.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vkasurinen.vknews.domain.model.Article
import com.vkasurinen.vknews.domain.repository.NewsRepository
import com.vkasurinen.vknews.presentation.homescreen.HomeUiEvent
import com.vkasurinen.vknews.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val newsRepository: NewsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val articleUrl = savedStateHandle.get<String>("articleUrl")

    private var _detailsState = MutableStateFlow(DetailsState())
    val detailsState = _detailsState.asStateFlow()

    init {
        articleUrl?.let { getArticle(it) }
    }

    fun onEvent(event: DetailsUiEvent) {
        when (event) {
            is DetailsUiEvent.UpsertDeleteArticle -> toggleFavorite(event.article.url)
        }
    }

    fun getArticle(url: String) {
        viewModelScope.launch {
            _detailsState.update { it.copy(isLoading = true) }

            newsRepository.getArticle(url)?.collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _detailsState.update { it.copy(isLoading = false) }
                    }
                    is Resource.Loading -> {
                        _detailsState.update { it.copy(isLoading = result.isLoading) }
                    }
                    is Resource.Success -> {
                        result.data?.let { article ->
                            _detailsState.update { it.copy(article = article) }
                        }
                    }
                }
            }
        }
    }

    fun getTopHeadlineArticle(url: String) {
        viewModelScope.launch {
            _detailsState.update { it.copy(isLoading = true) }

            newsRepository.getTopHeadlineArticle(url).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _detailsState.update { it.copy(isLoading = false) }
                    }
                    is Resource.Loading -> {
                        _detailsState.update { it.copy(isLoading = result.isLoading) }
                    }
                    is Resource.Success -> {
                        result.data?.let { article ->
                            _detailsState.update { it.copy(article = article) }
                        }
                    }
                }
            }
        }
    }

    private fun toggleFavorite(articleUrl: String) {
        viewModelScope.launch {
            try {
                val currentArticle = _detailsState.value.article
                if (currentArticle != null && currentArticle.url == articleUrl) {
                    val updatedArticle = currentArticle.copy(isBookmarked = !currentArticle.isBookmarked)
                    _detailsState.update { it.copy(article = updatedArticle) }
                    newsRepository.upsertArticle(updatedArticle)
                    savedStateHandle.set("refreshBookmarks", true) // Trigger refresh
                }
            } catch (e: Exception) {
                Log.d("DetailsViewModel", "Error on toggling favorite")
                _detailsState.update { it.copy(isLoading = false) }
            }
        }
    }
}