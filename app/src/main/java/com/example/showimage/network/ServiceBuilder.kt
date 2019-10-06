package com.example.showimage.network

import android.app.DownloadManager
import android.app.SearchableInfo
import com.example.showimage.Utils
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    private val clienOKHttpClient:OkHttpClient.Builder = OkHttpClient.Builder()
    fun<T> getApiService(serviceClass:Class<T>):T {
        var retrofit = retrofit2.Retrofit.Builder()
            .baseUrl(Utils.BASE_API)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(clienOKHttpClient.build())
            .build()
        return retrofit.create(serviceClass)
    }
}