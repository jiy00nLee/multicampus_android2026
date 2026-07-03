package com.example.tripapp.detail

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripapp.MyApplication
import com.example.tripapp.databinding.FragmentDetailNewsBinding
import com.example.tripapp.network.MyNewsAdapter
import com.example.tripapp.network.Page
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailNewsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailNewsBinding.inflate(inflater, container, false)

        val call = MyApplication.networkService.getList("swiss", MyApplication.API_KEY, 1, 10)
        call.enqueue(object : Callback<Page>{
            override fun onResponse(call: Call<Page?>, response: Response<Page?>) {
                if (response.isSuccessful && response.body() != null){
                    binding.detailNewsRecyclerView.layoutManager = LinearLayoutManager(activity)
                    binding.detailNewsRecyclerView.adapter = MyNewsAdapter(activity as Context, response.body()?.articles)
                }
            }
            override fun onFailure(call: Call<Page?>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return binding.root
    }
}