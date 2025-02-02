package com.vkasurinen.vknews.presentation.homescreen

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel
import com.vkasurinen.vknews.domain.model.Article
import com.vkasurinen.vknews.presentation.homescreen.components.ArticlesList
import com.vkasurinen.vknews.presentation.homescreen.components.CoilImage
import com.vkasurinen.vknews.presentation.homescreen.components.TopNews
import com.vkasurinen.vknews.util.Screen

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomeScreenRoot(
    navController: NavHostController,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val homeState by viewModel.homeState.collectAsState()
    val topHeadlinesState by viewModel.topHeadlinesState.collectAsState()
    HomeScreen(
        homeState = homeState,
        topHeadlinesState = topHeadlinesState,
        onAction = viewModel::onEvent,
        navHostController = navController
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    homeState: HomeState,
    topHeadlinesState: TopHeadlinesState,
    onAction: (HomeUiEvent) -> Unit,
    navHostController: NavHostController
) {
    Column() {
        Text(
            text = "Top News",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(2.dp))

        if (topHeadlinesState.topHeadlines.isNotEmpty()) {
            TopNews(
                topHeadlines = topHeadlinesState.topHeadlines,
                navHostController = navHostController,
                navigateToDetails = ::navigateToDetails
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Divider(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(1.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = "Recent News",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            ArticlesList(
                state = homeState,
                onClick = { article, isTopNews -> navigateToDetails(navHostController, article, isTopNews) }
            )
        }
    }
}

private fun navigateToDetails(navController: NavHostController, article: Article, isTopNews: Boolean) {
    navController.currentBackStackEntry?.savedStateHandle?.set("articleUrl", article.url)
    navController.currentBackStackEntry?.savedStateHandle?.set("isTopNews", isTopNews)
    navController.navigate(Screen.Details.route)
}