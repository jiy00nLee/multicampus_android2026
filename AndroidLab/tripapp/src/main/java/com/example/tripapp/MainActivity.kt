package com.example.tripapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tripapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var initTime= 0L // backButton이 눌린 시간

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 액티비티 윈도우의 액션바를 개발자가 지정한 툴바에 적용 필요.
        setSupportActionBar(binding.toolbar)

        // 1번
        // object : 익명 클래스 선언 예약어.
        // class A {} ==> object {}
        // class A : B () {} ==> object : b() {}
//        binding.mainCardview1.setOnClickListener (object : View.OnClickListener{
//            override fun onClick(v: View?) {
//            }
//        })

        // 2번
        // 추상 함수가 하나인 인터페이스를구현한 익명 클래스를 선언할 때는 추상 함수 내용만 람다함수로.
//        binding.mainCardview1.setOnClickListener({
//
//        })

        // 어느 함수를 호출 시, 함수의 매개변수 중 마지막 매개변수가 함수 타입이라면. () 외부에 선언가능.
//        fun some(arg1 : Int, arg2 : (Int) -> Int){ }
//        some(10, {10})
//        some(10){10}


        binding.mainCardview1.setOnClickListener{
            moveToActivity(DetailActivity::class.java)
        }

        binding.btnMoveToAboutActivity.setOnClickListener {
            moveToActivity(AboutActivity::class.java)
        }

        binding.btnMoveToMyInfoActivity.setOnClickListener {
            moveToActivity(MyInfoActivity::class.java)
        }

        // back button 이벤트 처리의 기본은 onKeyDown()으로 키 이벤트 처리가 기본
        // 허나 앱에서 백버튼 이벤트 처리의 비율이 높고 이벤트 처리 로직이여러개이경우가 있어,
        // api 33에서 onKeyDown()으로 백버튼 이벤트 처리가 deprecated 되었고 addCallback으로 별도 callback 등록 권장
        // api 36부터는 onKeyDown()으로 백버튼 이벤트 처리는 더이상 지원 X.
        onBackPressedDispatcher.addCallback(this){
            val now = System.currentTimeMillis()
            if (now - initTime > 3000){
                Toast.makeText(this@MainActivity, "종료하려면 한번 더 누르세요.", Toast.LENGTH_SHORT).show()
                initTime = now
            }else{
            // 현 액티비티 종료 (!!)
               finish()
            }
        }
    }

    // Menu View --------------------------------------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // 메뉴 출력
        menuInflater.inflate(R.menu.menu_main, menu)
        // SearchView가 적용된 MenuItem 객체 획득
        val menuItem = menu?.findItem(R.id.menu_search)
        val searchView = menuItem?.actionView as SearchView
        // SearchView Event Listener 등록
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            // 검색을 위해 나온 키보드에 존재하는 검색 버튼 클릭 시
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                searchView.setQuery("", false)
                searchView.isIconified = true // 아이콘으로 복귀
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    // 메뉴 이벤트 처리 자동 호출
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_setting -> moveToActivity(SettingActivity::class.java)
        }
        return super.onOptionsItemSelected(item)
    }
    
    // Menu View --------------------------------------------------------------------------------------------------------

    fun moveToActivity(activityClass: Class<*>){
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }
}