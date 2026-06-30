package com.example.androidlab

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var button: Button
    lateinit var editView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //액티비티가 시스템 영역까지 차지하라
        enableEdgeToEdge()

        //화면 출력 명령 : inflate(xml 에 명시된 뷰 객체 생성, 메모리에 올리는 작업) + 화면 출력
        setContentView(R.layout.activity_main)
        //필요한 뷰 객체 획득
        button = findViewById(R.id.button)
        editView = findViewById(R.id.editView)
        //뷰에 이벤트 등록
        button.setOnClickListener {
            //유저 입력값 획득
            val data = editView.text.toString()
            // (옵션) 런타임 로그
            Log.d("MainActivity", data)
        }


        //시스템 영역에서 액티비티 컨텐츠를 보호해서 출력하기 위한 설정
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}