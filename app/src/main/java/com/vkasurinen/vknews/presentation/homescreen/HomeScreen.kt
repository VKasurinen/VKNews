package com.vkasurinen.vknews.presentation.homescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.androidx.compose.koinViewModel
import com.vkasurinen.vknews.domain.model.Article

@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()

    if (state.isLoading) {
        Text(text = "Loading news...")
    } else {
        LazyColumn {
            items(state.articles) { article ->
                ArticleItem(article = article)
            }
        }
    }
}

@Composable
fun ArticleItem(article: Article) {
    Column {
        Text(text = article.title)
        Text(text = article.description)
    }
}