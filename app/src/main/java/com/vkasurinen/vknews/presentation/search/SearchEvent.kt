package com.vkasurinen.vknews.presentation.search

sealed interface SearchEvent {
    data class Search(val query: String) : SearchEvent
//    data class QueryChanged(val query: String) : SearchEvent()
}