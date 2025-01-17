package com.vkasurinen.vknews.di

import androidx.room.Room
import com.vkasurinen.vknews.data.local.NewsDatabase
import com.vkasurinen.vknews.data.remote.NewsApi
import com.vkasurinen.vknews.data.repository.NewsRepositoryImpl
import com.vkasurinen.vknews.domain.repository.NewsRepository
import com.vkasurinen.vknews.presentation.details.DetailsViewModel
import com.vkasurinen.vknews.presentation.homescreen.HomeViewModel
import com.vkasurinen.vknews.presentation.search.SearchViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    single {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(NewsApi.BASE_URL)
            .client(get())
            .build()
            .create(NewsApi::class.java)
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            NewsDatabase::class.java,
            "news.db"
        ).build()
    }

    single { get<NewsDatabase>().newsDao }
    single { get<NewsDatabase>().topHeadlinesDao }

    single<NewsRepository> {
        NewsRepositoryImpl(
            newsApi = get(),
            newsDao = get(),
            topHeadlinesDao = get()
        )
    }

    viewModel { HomeViewModel(get()) }
    viewModel { DetailsViewModel(get(), get()) }
    viewModel { SearchViewModel(get())}

}