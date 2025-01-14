package com.vkasurinen.vknews

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vkasurinen.vknews.data.remote.NewsApi
import com.vkasurinen.vknews.domain.repository.NewsRepository
import com.vkasurinen.vknews.presentation.details.DetailsScreenRoot
import com.vkasurinen.vknews.ui.theme.VKNewsTheme
import com.vkasurinen.vknews.util.Resource
import com.vkasurinen.vknews.util.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val newsRepository: NewsRepository by inject()

    private val newsApi: NewsApi by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VKNewsTheme {
                SetBarColor(color = MaterialTheme.colorScheme.inverseOnSurface)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Main.route
                    ) {
                        composable(Screen.Main.route) {
                            MainScreen(navController)
                        }
                        composable(Screen.Details.route) {
                            DetailsScreenRoot(navController = navController)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun SetBarColor(color: Color) {
        val systemUiController = rememberSystemUiController()
        LaunchedEffect(key1 = color) {
            systemUiController.setSystemBarsColor(color)
        }
    }

//    private fun testNewsRepository() {
//        lifecycleScope.launch {
//            newsRepository.getNews(listOf("bbc-news", "abc-news", "die-zeit", "business-insider"))
//                .collectLatest { resource ->
//                    when (resource) {
//                        is Resource.Success -> {
//                            Log.d("MainActivity", "News fetched successfully: ${resource.data}")
//                            resource.data?.forEach { article ->
//                                CoroutineScope(Dispatchers.IO).launch {
//                                    newsRepository.upsertArticle(article)
//                                    Log.d("MainActivity", "Inserted article: $article")
//                                }
//                            }
//                        }
//                        is Resource.Error -> {
//                            Log.e("MainActivity", "Error fetching news: ${resource.message}")
//                        }
//                        is Resource.Loading -> {
//                            Log.d("MainActivity", "Loading news...")
//                        }
//                    }
//                }
//        }
//    }

    private fun testNewsApi() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = newsApi.searchNews(searchQuery = "bitcoin", sources = "", page = 1)
                Log.d("MainActivity", "API response: ${response.articles}")
            } catch (e: Exception) {
                Log.e("MainActivity", "API call failed: ${e.message}")
            }
        }
    }




}



