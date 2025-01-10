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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vkasurinen.vknews.domain.repository.NewsRepository
import com.vkasurinen.vknews.ui.theme.VKNewsTheme
import com.vkasurinen.vknews.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {


    private val newsRepository: NewsRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VKNewsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Box(modifier = Modifier.padding(innerPadding)) {
                        Text(text = "asd")
                    }


                }
            }
        }
        testNewsRepository()

    }


    private fun testNewsRepository() {
        CoroutineScope(Dispatchers.IO).launch {
            newsRepository.getArticles().collect { resource ->
                when (resource) {
                    is Resource.Loading<*> -> {
                        Log.d("MainActivity", "Loading data...")
                    }

                    is Resource.Success<*> -> {
                        Log.d("MainActivity", "Data loaded successfully: ${resource.data}")
                    }

                    is Resource.Error<*> -> {
                        Log.e("MainActivity", "Error loading data: ${resource.message}")
                    }

                    else -> {
                        Log.e("MainActivity", "Unknown resource state")
                    }
                }
            }

            newsRepository.getNews(listOf("source1", "source2")).collect { resource ->
                when (resource) {
                    is Resource.Loading<*> -> {
                        Log.d("MainActivity", "Loading news...")
                    }

                    is Resource.Success<*> -> {
                        Log.d("MainActivity", "News loaded successfully: ${resource.data}")
                    }

                    is Resource.Error<*> -> {
                        Log.e("MainActivity", "Error loading news: ${resource.message}")
                    }

                    else -> {
                        Log.e("MainActivity", "Unknown resource state")
                    }
                }
            }

            newsRepository.searchNews("query", listOf("source1", "source2")).collect { resource ->
                when (resource) {
                    is Resource.Loading<*> -> {
                        Log.d("MainActivity", "Searching news...")
                    }

                    is Resource.Success<*> -> {
                        Log.d(
                            "MainActivity",
                            "Search results loaded successfully: ${resource.data}"
                        )
                    }

                    is Resource.Error<*> -> {
                        Log.e("MainActivity", "Error searching news: ${resource.message}")
                    }

                    else -> {
                        Log.e("MainActivity", "Unknown resource state")
                    }
                }
            }
        }
    }
}


