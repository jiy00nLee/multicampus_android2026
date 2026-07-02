package com.example.tripapp.detail

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripapp.databinding.FragmentDetailMainBinding
import com.example.tripapp.databinding.ItemRecyclerviewDetailBinding
import com.example.tripapp.util.Constant

// (필수) 1. 각 데이터 항목 추상화
class Product(val photo:Int, val title : String, val desc : String)

// (필수) 2. 항목을 구성하기 위한 뷰를 가지는 역할자
class MyHolder(val binding : ItemRecyclerviewDetailBinding)
    : RecyclerView.ViewHolder(binding.root)

// (필수) 3. 각 항목을 ViewHolder를 이용해 만드는 역할자
class MyAdapter(val dataList : MutableList<Product>) : RecyclerView.Adapter<MyHolder>() {
    // 항목 갯수 판단 위해 자동 호출
    override fun getItemCount(): Int {
        return dataList.size
    }

    // 항목 구성을 위한 뷰홀더를 결정하기 위해서 자동 호출
    // If. getItemCount에서 data 사이즈를 1000으로 리턴되더라도, onBindViewHolder를 1000번 호출하지 않고 화면에 출력되는 갯수만큼만.
    // 같이 나오지 않는 항목별 holder객체를 재사용 (중요)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(ItemRecyclerviewDetailBinding.inflate(LayoutInflater.from(parent.context)))
    }

    // 각각의 항목을 구성하기 위해서 자동 호출
    // - holder - onCreateViewHolder에서 리턴시킨 객체
    // - position : 항목 index
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val product = dataList[position]
        holder.binding.run {
            itemTitleView.text = product.title
            itemDescView.text = product.desc
            itemImageView.setImageResource(product.photo)
        }
    }
}

// (옵션) 4. Decoration
class MyDecoration(val context: Context) : RecyclerView.ItemDecoration(){
    // 각 항목을 꾸미기 위해서 호출
    // - outRect : 항목 구성 사각형 정보
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val index = parent.getChildAdapterPosition(view) + 1
        if (index%3 == 0) outRect.set(10, 10, 10, 60)
        else outRect.set(10, 10, 10, 0)

    }
}


class DetailMainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailMainBinding.inflate(inflater, container, false)

        binding.detailRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.detailRecyclerView.adapter = MyAdapter(Constant.testDataList)
        binding.detailRecyclerView.addItemDecoration(MyDecoration(activity as Context))

        return binding.root
    }
}