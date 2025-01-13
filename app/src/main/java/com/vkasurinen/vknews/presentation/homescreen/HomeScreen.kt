package com.vkasurinen.vknews.presentation.homescreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel
import com.vkasurinen.vknews.domain.model.Article
import com.vkasurinen.vknews.presentation.components.CoilImage
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
        if (state.topHeadlines.isNotEmpty()) {
            TopNews(
                articles = state.topHeadlines,
                navHostController = navHostController
            )
        }

        Spacer(modifier = Modifier.padding(4.dp))

        Divider(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Recent News",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp)
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
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
            ) {
                items(state.articles.size) { index ->
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        CoilImage(
                            url = state.articles[index].urlToImage,
                            contentDescription = state.articles[index].title
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            text = state.articles[index].title,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}