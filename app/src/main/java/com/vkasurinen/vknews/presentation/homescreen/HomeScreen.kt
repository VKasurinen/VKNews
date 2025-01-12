package com.vkasurinen.vknews.presentation.homescreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel
import com.vkasurinen.vknews.domain.model.Article
import com.vkasurinen.vknews.presentation.components.TopNews

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

    Column {
        if (state.articles.isNotEmpty()) {
            TopNews(
                articles = state.articles,
                article = state.articles.first(),
                navHostController = navHostController
            )
        }
        
        Spacer(modifier = Modifier.padding(4.dp))

        Divider(
        modifier = Modifier
            .padding(horizontal = 16.dp)
        )

    }

}