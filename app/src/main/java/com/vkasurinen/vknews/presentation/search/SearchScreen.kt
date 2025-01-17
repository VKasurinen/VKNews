package com.vkasurinen.vknews.presentation.search

import NewsCategories
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.vkasurinen.vknews.domain.model.Article
import com.vkasurinen.vknews.presentation.search.components.ArticleCard
import com.vkasurinen.vknews.presentation.search.components.SearchBar
import com.vkasurinen.vknews.util.Screen
import org.koin.androidx.compose.koinViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SearchScreenRoot(
    navController: NavHostController,
    viewModel: SearchViewModel = koinViewModel(),
) {
    val state = viewModel.state.collectAsState().value
    SearchScreen(
        state = state,
        onAction = viewModel::onEvent,
        navHostController = navController
    )
}

@Composable
private fun SearchScreen(
    state: SearchState,
    onAction: (SearchEvent) -> Unit,
    navHostController: NavHostController
) {

    val searchQuery = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        SearchBar(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            text = searchQuery.value,
            onValueChange = {
                searchQuery.value = it
                onAction(SearchEvent.Search(it))
            },
            onSearch = {
                onAction(SearchEvent.Search(searchQuery.value))
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        NewsCategories(
            articles = state.articles,
            onCategorySelected = { category ->
                // Handle category selection
            }
        )

        Spacer(modifier = Modifier.height(8.dp))


        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn {
                items(state.articles) {article ->
                    ArticleCard(
                        article = article,
                        navHostController = navHostController,
                        navigateToDetails = { navController, article ->
                            navigateToDetails(navController, article)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}


private fun navigateToDetails(navController: NavHostController, article: Article) {
    navController.currentBackStackEntry?.savedStateHandle?.set("articleUrl", article.url)
    navController.navigate(Screen.Details.route)
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    SearchScreen(
        state = SearchState(),
        onAction = {},
        navHostController = rememberNavController()
    )
}