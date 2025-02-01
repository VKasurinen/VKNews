package com.vkasurinen.vknews.presentation.bookmark

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.vkasurinen.vknews.presentation.search.components.ArticleCard
import com.vkasurinen.vknews.presentation.search.navigateToDetails
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookMarkScreenRoot(
    navController: NavHostController,
    sharedViewModel: BookMarkViewModel = koinViewModel()
) {
    val state = sharedViewModel.state.collectAsState().value
    BookMarkScreen(
        state = state,
        navHostController = navController,
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun BookMarkScreen(
    state: BookMarkState,
    navHostController: NavHostController
) {
    Column(
        Modifier.fillMaxSize()
    ) {
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn {
                items(state.articles) { article ->
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

//@RequiresApi(Build.VERSION_CODES.O)
//@Preview
//@Composable
//private fun BookMarkScreenPreview() {
//    BookMarkScreen(
//        state = BookMarkState(),
//        navHostController = NavHostController(LocalContext.current)
//    )
//}
