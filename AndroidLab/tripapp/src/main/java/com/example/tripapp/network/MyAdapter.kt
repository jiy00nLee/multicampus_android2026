package com.example.tripapp.network

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tripapp.MyApplication
import com.example.tripapp.databinding.ItemNewsBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyNewsViewHolder(val binding: ItemNewsBinding): RecyclerView.ViewHolder(binding.root)

class MyNewsAdapter(val context: Context, val datas: MutableList<News>?): RecyclerView.Adapter<MyNewsViewHolder>(){

    override fun getItemCount(): Int{
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyNewsViewHolder
            = MyNewsViewHolder(ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MyNewsViewHolder, position: Int) {
        // 항목 데이터 추출. 문자열 데이터는 바로 뷰에 출력
        val news = datas!![position]
        holder.binding.run {
            itemTitle.text = news.title
            itemDesc.text = news.description
            itemTime.text = "${news.author} At ${news.publishedAt}"
            news.urlToImage?.let {
                // 네트워킹을 통해서 이미지 다운로드
                val call = MyApplication.networkService.getNetworkImage(it)
                // callback 등록하면서 네트워킹 시도
                call.enqueue(object : Callback<ResponseBody>{
                    override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                        if (response.isSuccessful && response.body() != null){
                            val bitmap = BitmapFactory.decodeStream(response.body()!!.byteStream())
                            itemImage.setImageBitmap(bitmap)
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                        t.printStackTrace()
                    }
                })

            }
        }
    }
}