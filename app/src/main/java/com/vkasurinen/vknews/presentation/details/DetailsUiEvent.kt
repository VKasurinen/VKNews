package com.vkasurinen.vknews.presentation.details

import com.vkasurinen.vknews.domain.model.Article

sealed interface DetailsUiEvent {

    data class UpsertDeleteArticle(val article: Article) : DetailsUiEvent

}