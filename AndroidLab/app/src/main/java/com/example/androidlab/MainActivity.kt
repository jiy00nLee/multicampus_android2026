package com.example.androidlab

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidlab.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //액티비티가 시스템 영역까지 차지하라
        enableEdgeToEdge()

        // XXXBinding 클래스에 일은 시켜야 한다. 객체를 메모리에 올려달라. (inflate)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        //화면 출력 명령
        setContentView(binding.root)

        binding.button.setOnClickListener {
            val data = binding.editView.text.toString()
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