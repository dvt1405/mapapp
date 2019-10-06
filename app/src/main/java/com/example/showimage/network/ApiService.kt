package com.example.showimage.network

import com.example.showimage.Utils
import com.example.showimage.database.model.ListImageResponse
import io.reactivex.Observable
import okhttp3.Response
import retrofit2.Call
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("rest")
    fun searchImage(
        @Query("method") method: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String,
        @Query("nojsoncallback") nojsonCallback: String,
        @Query("page") page: String,
        @Query("text") text: String
        ): Call<ListImageResponse>
    @GET("rest")
    fun searchImageObservable(
        @Query("method") method: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String,
        @Query("nojsoncallback") nojsonCallback: String,
        @Query("page") page: String,
        @Query("lat") lat:String,
        @Query("lon") lon:String,
        @Query("per_page") perpage:String
    ): Observable<ListImageResponse>

    companion object Factory {
        fun create(): ApiService {
            val retrofit = retrofit2.Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Utils.BASE_API)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}