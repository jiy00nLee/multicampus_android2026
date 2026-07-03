package com.example.tripapp

import android.app.Application
import com.example.tripapp.network.NetworkService
import com.example.tripapp.util.Constant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 안드로이드 앱에 Application - manifest 등록해야 하며, 1개만 가능
// 프로세스가 구동되면서 최초에 singleton으로 생성되는 존재 => 앱이 구동되지마자 최초에 한번 무언가 초기화되어야 하는 코드
class MyApplication : Application() {
    // companion object 영역 내 변수를 마치 자바의 static 멤버처럼 이용 가능
    companion object{
        val API_KEY = Constant.newsApiKey
        val BASE_URL = Constant.newsApiUrl

        val retrofit : Retrofit
            get() = Retrofit.Builder().
                    baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val networkService = retrofit.create(NetworkService::class.java)
    }
}