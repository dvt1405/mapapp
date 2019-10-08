package com.example.showimage.network

import com.example.showimage.Utils
import retrofit2.Call
import com.example.showimage.database.model.ListImageResponse
import io.reactivex.Observable
import okhttp3.ResponseBody


class ApiCall {

    val apiService = ServiceBuilder.getApiService(ApiService::class.java)

    fun requestSearchImage(text: String, page: String): Call<ListImageResponse> =
        apiService.searchImage(
            Utils.SEARCH_METHOD,
            Utils.KEY_API_FLICKR,
            Utils.JSON_FORMAT,
            Utils.NO_JSON_CALL_BACK,
            page,
            text
        )

    fun requestSearchImageForLocation(
        lat: String,
        lon: String,
        page: String,
        perpage: String
    ): Observable<ListImageResponse> = apiService.searchImageObservable(
        Utils.SEARCH_METHOD,
        Utils.KEY_API_FLICKR,
        Utils.JSON_FORMAT,
        Utils.NO_JSON_CALL_BACK,
        page,
        lat,
        lon,
        perpage
    )
    fun downloadImage(url:String):Call<ResponseBody> = apiService.downloadImage(url)

}