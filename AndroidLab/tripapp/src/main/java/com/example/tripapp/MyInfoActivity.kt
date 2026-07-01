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
                    Toast.makeText(this, "DB 저장 성공", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            else{
                Toast.makeText(this, "이메일은 필수 입력 데이터입니다.", Toast.LENGTH_SHORT).show()
            }
        }


    }
}