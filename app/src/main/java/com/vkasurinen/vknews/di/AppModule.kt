package com.vkasurinen.vknews.di

import androidx.room.Room
import com.vkasurinen.vknews.data.local.NewsDatabase
import com.vkasurinen.vknews.data.remote.NewsApi
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
            "gamedb.db"
        ).build()
    }
}