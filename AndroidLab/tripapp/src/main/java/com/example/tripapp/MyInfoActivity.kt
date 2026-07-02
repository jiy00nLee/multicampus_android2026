package com.example.tripapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tripapp.databinding.*
import com.example.tripapp.db.insertInfo
import com.example.tripapp.db.selectInfo

class MyInfoActivity : AppCompatActivity() {

    lateinit var binding: ActivityMyInfoBinding

    var email : String? = null
    var phone : String? = null
    var photo : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMyInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val cursor = selectInfo(this)
        // let{}, run{}, with{}, apply{}
        cursor?.let {
            if (cursor.moveToFirst()){
                binding.run {
                    email = cursor.getString(1)
                    photo = cursor.getString(2)
                    phone = cursor.getString(3)

                    myInfoEmail.setText(email)
                    myInfoPhone.setText(phone)
                }
            }
        }

        binding.testSaveButton.setOnClickListener{
            binding.run{
                email = myInfoEmail.text.toString()
                phone = myInfoPhone.text.toString()
            }

            if (email?.isNotEmpty() ?: false){
                if (insertInfo(this, email?:"", phone, photo)){
                    Toast.makeText(this, "내정보 저장 성공", Toast.LENGTH_SHORT).show()
                    // 자신을 실행시킨 곳으로 화면을 되돌리면서 결과 데이터 포함
                    // 액티비티내에서 intent는 자신을 실행시킨 인텐트 정보를 말한다.
                    intent.putExtra("phone", phone)
                    intent.putExtra("email", email)
                    intent.putExtra("photo", photo)
                    // 되돌리기 전에 어떤 상태인지를 명시해야 한다.
                    setResult(RESULT_OK, intent)
                    // 액티비티 종료시켜서 이전화면으로 자동 전환되게
                    finish()
                }
            }
            else{
                Toast.makeText(this, "이메일은 필수 입력 데이터입니다.", Toast.LENGTH_SHORT).show()
            }
        }


    }
}