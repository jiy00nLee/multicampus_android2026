package com.example.tripapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
            moveToDetailActivity()
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

    fun moveToDetailActivity(){
        val intent = Intent(this, DetailActivity::class.java)
        startActivity(intent)
    }
}