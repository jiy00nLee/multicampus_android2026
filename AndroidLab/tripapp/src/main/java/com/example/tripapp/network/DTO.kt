package com.example.tripapp.network

class News {
    var id : Long = 0
    var author : String? = null
    var title : String? = null
    var description : String? = null
    var urlToImage : String? = null     // 이미지 다운로드 url
    var publishedAt : String? = null
}

class Page {
    var articles : MutableList<News>? = null
}