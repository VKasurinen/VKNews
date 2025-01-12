package com.vkasurinen.vknews.presentation.homescreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel
import com.vkasurinen.vknews.domain.model.Article

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomeScreenRoot(
    navController: NavHostController,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state = viewModel.state.collectAsState().value
    HomeScreen(
        state = state,
        onAction = viewModel::onEvent,
        navHostController = navController
    )
}




@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeUiEvent) -> Unit,
    navHostController: NavHostController
) {




}

@Composable
fun ArticleItem(article: Article) {
    Column {
        Text(text = article.title)
        Text(text = article.description)
    }
}