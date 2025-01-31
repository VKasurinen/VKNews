package com.vkasurinen.vknews

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
        enableEdgeToEdge()
        setContent {
            VKNewsTheme {
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
                            val isTopNews = navController.previousBackStackEntry?.savedStateHandle?.get<Boolean>("isTopNews") ?: false
                            DetailsScreenRoot(navController = navController, isTopNews = isTopNews)
                        }
                    }
                }
            }
        }
        testGetArticle()
    }

    @Composable
    private fun SetBarColor(color: Color) {
        val systemUiController = rememberSystemUiController()
        LaunchedEffect(key1 = color) {
            systemUiController.setSystemBarsColor(color)
        }
    }

    private fun testGetArticle() {
        lifecycleScope.launch {
            newsRepository.getTopHeadlineArticle("https://www.cnn.com/2025/01/12/travel/india-maha-kumbh-mela-2025-intl-hnk/index.html")?.collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        Log.d("MainActivity", "Top headlines fetched successfully: ${resource.data}")
                    }
                    is Resource.Error -> {
                        Log.e("MainActivity", "Error fetching top headlines: ${resource.message}")
                    }
                    is Resource.Loading -> {
                        Log.d("MainActivity", "Loading top headlines...")
                    }
                }
            }
        }
    }

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



