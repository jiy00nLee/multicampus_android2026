package com.example.tripapp.util

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.ImageView

fun uriToImageView(context: Context, uri: Uri, imageView: ImageView): Boolean{

    Log.d("jiy00nlee", uri.toString())
    val option = BitmapFactory.Options()
    option.inSampleSize = 10

    // uri의 이미지를 읽을 수 있는 stream을 바로 획득
    // gallery app 연동으로 uri을 획득하자 마자이미지를 읽기에는 stream 방식이 최선
    // uri를 db같은 곳에 저장했다 이후 필요한 순간 다시 읽을 수는 없음 (즉, 갤러리앱에서 넘어온 그 순간만 활용가능)
    var inputStream = context.contentResolver.openInputStream(uri)
    val bitmap = BitmapFactory.decodeStream(inputStream, null, option)
    inputStream!!.close()
    bitmap?.let {
        imageView.setImageBitmap(bitmap)
        return true

    }
    return false
}


fun fileToImageView(context: Context, filePath: String, imageView: ImageView): Boolean{
    val option = BitmapFactory.Options()
    option.inSampleSize = 10

    // 파일 경로로 이미지 읽어서 화면 출력
    // db 저장된 파일 경로
    val bitmap = BitmapFactory.decodeFile(filePath, option)
    bitmap?.let {
        imageView.setImageBitmap(bitmap)
        return true
    }
    return false
}