package com.example.tripapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tripapp.databinding.*
import com.example.tripapp.db.insertInfo
import com.example.tripapp.db.selectInfo
import com.example.tripapp.util.fileToImageView
import com.example.tripapp.util.uriToImageView
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class MyInfoActivity : AppCompatActivity() {

    lateinit var binding: ActivityMyInfoBinding

    var email : String? = null
    var phone : String? = null
    var photo : String? = null

    // 외장 앱별 파일 경로, 카메라앱에 전달할 파일 경로
    lateinit var cameraFilePath : String

    @SuppressLint("Recycle")
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

        // 카메라/갤러리 기능 로직 --------------------------------------------------------------------------------------------


        val requestGalleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){ activityResult ->
            try {
                // gallery 목록에서 사진 선택 되돌아 올 때 결과
                // gallery provider 이용 url, url 마지막이 선택된 사진의 식별자
                // 바로 화면 출력하겠다면 stream 방식으로 충분
                // 이후 출력하려면 file 경로가 있어야 하기에, 원하는 정보(파일 경로)를 gallery provider에게 획득
                val cursor = contentResolver.query(
                    activityResult.data!!.data!!,
                    arrayOf(MediaStore.Images.Media.DATA), // 원하는 데이터 조건 (Ex. 파일경로)
                    null, null, null
                )
                cursor?.let {
                    if (cursor.moveToFirst()){
                        photo = cursor.getString(0)
                    }
                }
                // 화면 출력
                uriToImageView(this, activityResult.data!!.data!!, binding.userImageView)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }

        // 버전별로 퍼미션 획득

        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                val intent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                requestGalleryLauncher.launch(intent)
            } else {
                Toast.makeText(this, "permission denied...", Toast.LENGTH_SHORT).show()
            }
        }

        binding.galleryButton.setOnClickListener {

            if(Build.VERSION.SDK_INT >= 33) {
                if (ContextCompat.checkSelfPermission(this, "android.permission.READ_MEDIA_IMAGES") == PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    intent.type = "image/*"
                    requestGalleryLauncher.launch(intent)
                } else {
                    permissionLauncher.launch("android.permission.READ_MEDIA_IMAGES")
                }

            }else {
                if (ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    intent.type = "image/*"
                    requestGalleryLauncher.launch(intent)
                } else {
                    permissionLauncher.launch("android.permission.READ_EXTERNAL_STORAGE")
                }
            }

        }


        val requestCameraFileLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        {
            // 카메라 앱
            fileToImageView(this, cameraFilePath, binding.userImageView)
            photo = cameraFilePath

        }
        binding.cameraButton.setOnClickListener {
            // 카메라 앱
            // 파일 준비
            val timeStamp: String =
                SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            // getExternalFilesDir(null) : 외장 앱별 루트
            // getExternalFilesDir(type) : 각 타입 파일 저장 디렉토리
            val storageDir: File? =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                storageDir
            )
            cameraFilePath = file.absolutePath
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.example.tripapp.fileprovider", file
            )
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // 카메라 앱에게 파일 정보 전달 (이때, manifest에 provider에 meta-data에 externalPath 설정 필요)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            requestCameraFileLauncher.launch(intent)

        }

        // 카메라/갤러리 기능 로직 --------------------------------------------------------------------------------------------



    }
}