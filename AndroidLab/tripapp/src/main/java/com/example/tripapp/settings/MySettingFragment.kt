package com.example.tripapp.settings

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.tripapp.R
import kotlin.math.log

// 설정 xml을 등록시키기 위한 개발자 프레그먼트 View
// 이 클래스를 액티비티가 화면에 출력시키도록 개발자가 지정해줘야 한다.
class MySettingFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // (필수) 화면 출력되고, 설정내용 저장까지 다 되는 기본 코드.
        setPreferencesFromResource(R.xml.settings, rootKey)

        // (옵션) 동적 제어를 위한, key로 설정 객체 획득 코드.
        val idPreference : EditTextPreference? = findPreference("id")
        val colorPreference : ListPreference? = findPreference("color")

        // summary를 동적으로 지정하고 싶을 경우 코드.
        // case 1. 유저 설정 값을 그대로 summary에 출력.
        colorPreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()

        // case 2. 설정 값을 참조해서 알고리즘으로 summary 지정.
        idPreference?.summaryProvider = Preference.SummaryProvider<EditTextPreference>{ preference ->
            // 설정 값 획득.
            val text = preference.text
            if(TextUtils.isEmpty(text)){
                "설정이 되지 않았습니다."
            }else{
                "설정된 ID는 $text 입니다." // 리턴시킨 문자열이 summary에 출력.
            }
        }

        // case 3. 설정 값 변경 순간의 이벤트.
        // 값 저장은 자동으로 되지만 그 순간 추가 업무 진행 코드가 필요할 경우.
        idPreference?.setOnPreferenceChangeListener{ preference, newValue ->
            Log.d("Fragment-MySettingFragment", "key : ${preference.key}, value : ${newValue}")
            true
        }

    }
}