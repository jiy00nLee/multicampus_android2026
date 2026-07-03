package com.example.tripapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tripapp.databinding.ActivityMainBinding
import com.example.tripapp.databinding.NavigationHeaderBinding
import com.example.tripapp.db.selectInfo

class MainActivity : AppCompatActivity() {

    var initTime= 0L // backButton이 눌린
    lateinit var toggle : ActionBarDrawerToggle
    lateinit var headerBinding: NavigationHeaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // inflate가 아닌 bind로 묶는 이유?
        // xml이 별개여서 별도로 메모리에 올리기는 하지만 독립적으로 사용되는 뷰 객체의 계층이 아니라
        // 다른 뷰 객체 계층에 추가되어야 해서
        headerBinding = NavigationHeaderBinding.bind(binding.mainDrawerView.getHeaderView(0))

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // toolbar height를 상태바 높이만큼 늘리고 padding 적용
            val params = binding.toolbar.layoutParams
            params.height = systemBars.top + resources.getDimensionPixelSize(
                androidx.appcompat.R.dimen.abc_action_bar_default_height_material
            )
            binding.toolbar.layoutParams = params
            binding.toolbar.setPadding(0, systemBars.top, 0, 0)
            WindowInsetsCompat.CONSUMED
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

        // 매개변수에 지정한 문자열. 화면 출력과는 관련없이 상태를 표현하는 문자열.
        toggle = ActionBarDrawerToggle(this, binding.main,
            R.string.drawer_opened, R.string.drawer_closed)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        // [비즈니스로직] 인텐트 설정 처리 코드 ------------------------------------------------------------------------------------------
        // 액티비티 인텐트를 발생시키고, 되돌아올때 사후처리를 위한 런처 설정
        val activityLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){ activityResult ->
            // 되돌아 올 때 콜백 사후처리 로직
            val email = activityResult.data?.getStringExtra("email")
            email?.let {
                headerBinding.userEmailView.text = email
            }
        }

        // 퍼미션 조정 다이얼로그를 띄우고, 닫혔을 때 사후처리
        val requestpermission = ActivityResultContracts.RequestPermission()
        val permissionLauncher = registerForActivityResult(requestpermission){
            if(it) noti()
            else Toast.makeText(this, "permission 거부", Toast.LENGTH_SHORT).show()

        }
        // [비즈니스로직] 인텐트 설정 처리 코드 ------------------------------------------------------------------------------------------

        // NavigationView의 항목 이벤트 처리
        binding.mainDrawerView.setNavigationItemSelectedListener { it ->
            when(it.itemId){
                R.id.main_navigation_about -> moveToActivity(AboutActivity::class.java)
                R.id.main_navigation_edit_info -> moveToActivityWithLauncher(MyInfoActivity::class.java, activityLauncher)
                R.id.main_navigation_notification -> {
                    // 1. API 33 이상일 경우 - 퍼미션 체크 필요
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                        // 1-1. 퍼미션 허용상태 (PERMISSION_GRANTED)
                        if (ContextCompat.checkSelfPermission(
                                this, "android.permission.POST_NOTIFICATIONS"
                            ) == PackageManager.PERMISSION_GRANTED
                        ){
                            noti()
                        }
                        else{
                            // 1-2. 퍼미션 거부상태 (PERMISSION_DENIED) > 조정 다이어로그 띄움
                            permissionLauncher.launch("android.permission.POST_NOTIFICATIONS")
                        }
                    // 2. API 33 밑일 경우
                    }else{
                        noti()
                    }
                }
            }
            true
        }

        // 액티비티 출력되면서 DB에 저장된 myinfo 데이터 출력
        val cursor = selectInfo(this)
        cursor?.let {
            if (cursor.moveToFirst()){
                headerBinding.run {
                    userEmailView.setText(cursor.getString(1))
                }
            }
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
        // ActionBarDrawerToggle > 내부적으로 메뉴로 준비된다. 이벤트시 메뉴 이벤트 함수 호출.
        // 자체 준비된 drawer 제어 로직 실행되도록 호출해주어야 한다.
        if (toggle.onOptionsItemSelected(item)) return true
        else if (item.itemId == R.id.menu_setting) {
            moveToActivity(SettingActivity::class.java)
        }
        return super.onOptionsItemSelected(item)
    }
    
    // Menu View --------------------------------------------------------------------------------------------------------

    fun moveToActivity(activityClass: Class<*>){
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }

    fun moveToActivityWithLauncher(activityClass: Class<*>, launcher:ActivityResultLauncher<Intent>){
        val intent = Intent(this, activityClass)
        launcher.launch(intent)
    }

    fun noti(){
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val builder : NotificationCompat.Builder

        // API 26 이상일 경우 - NotificationChannel 객체 생성 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "one-channel",
                "OneChannel",
                NotificationManager.IMPORTANCE_LOW
            )
            // 반드시 NotificationManager에 채널을 등록해야 함.
            // 앱이 실행되며 최초 한번만 등록하면 되긴 하나, 성능에 문제가 없어 띄우기전에 반복적 등록을 권장함.
            manager.createNotificationChannel(channel)
            // 등록된 채널의 id를 명시하여 Builder를 생성
            builder = NotificationCompat.Builder(this, "one-channel")
        }else{
            builder = NotificationCompat.Builder(this)
        }

        builder.setSmallIcon(android.R.drawable.ic_notification_overlay)
        builder.setWhen(System.currentTimeMillis())
        builder.setContentTitle("메세지 도착")
        builder.setContentText("항공기 특가 할인 쿠폰이 도착했습니다.")
        // 이벤트 의뢰 등록
        val intent = Intent(this, MyReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        builder.setContentIntent(pendingIntent)

        // 이벤트 발생
        manager.notify(11, builder.build())
    }

}