package com.example.tripapp.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface NetworkService {
    @GET("/v2/everything")
    fun getList(
        @Query("q")
        q: String,
        @Query("apiKey")
        apiKey: String,
        @Query("page")
        page: Long,
        @Query("pageSize")
        pageSize: Int
    ): Call<Page>

    @GET
    fun getNetworkImage(
        @Url url : String
    ): Call<ResponseBody> // 원본데이터 리턴 (No converting)
}