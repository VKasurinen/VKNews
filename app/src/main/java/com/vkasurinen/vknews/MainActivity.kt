package com.vkasurinen.vknews

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.vkasurinen.vknews.data.remote.NewsApi
import com.vkasurinen.vknews.domain.model.Article
import com.vkasurinen.vknews.domain.model.Source
import com.vkasurinen.vknews.domain.repository.NewsRepository
import com.vkasurinen.vknews.presentation.homescreen.HomeScreen
import com.vkasurinen.vknews.ui.theme.VKNewsTheme
import com.vkasurinen.vknews.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Box(modifier = Modifier.padding(innerPadding)) {
                        HomeScreen()
                    }


                }
            }
        }

        //testNewsApi2()
        testNewsRepository()
    }


    private fun testNewsRepository() {
        lifecycleScope.launch {
            newsRepository.getNews(listOf("bbc-news", "abc-news", "die-zeit", "business-insider"))
                .collectLatest { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            Log.d("MainActivity", "News fetched successfully: ${resource.data}")
                            resource.data?.forEach { article ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    newsRepository.upsertArticle(article)
                                    Log.d("MainActivity", "Inserted article: $article")
                                }
                            }
                        }
                        is Resource.Error -> {
                            Log.e("MainActivity", "Error fetching news: ${resource.message}")
                        }
                        is Resource.Loading -> {
                            Log.d("MainActivity", "Loading news...")
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


