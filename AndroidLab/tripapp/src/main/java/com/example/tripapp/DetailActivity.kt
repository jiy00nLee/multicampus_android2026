package com.example.tripapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tripapp.databinding.ActivityDetailBinding
import com.example.tripapp.detail.DetailMainFragment
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    // Viewpager의 항목(화면)을 만들어주는 역할자
    // 만드는 항목 하나가 fragment로 만들어진다면, FragmentStateAdapter를 상속받아서 작성
    // fragment가 아닌 일반 뷰로 준비된다면, RecyclerView.Adapter 상속
    class MyFragmentPagerAdapter(activity : FragmentActivity) : FragmentStateAdapter(activity){
        val fragments : List<Fragment>

        init{
            fragments = listOf(DetailMainFragment(), DetailMainFragment())
        }
        // 항목 갯수를 판단하기 위해서 자동 호출
        override fun getItemCount(): Int {
            return fragments.size
        }
        // 각 항목을 위한 fragment를 결정하기 위해서 자동 호출. 반복 호출. 매개변수가 항목 index
        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityDetailBinding.inflate((layoutInflater))
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.detailToolbar)

        // viewpager에게 adapter 적용
        val adapter = MyFragmentPagerAdapter(this)
        binding.detailViewpager.adapter = adapter

        TabLayoutMediator(binding.detailTabs, binding.detailViewpager){ tab, position ->
            // 지정된 viewpager의 항목 갯수만큼 해당 함수 자동 호출
            when(position){
                0 -> tab.text = "상품"
                1 -> tab.text = "뉴스"
            }
        }.attach()

    }
}