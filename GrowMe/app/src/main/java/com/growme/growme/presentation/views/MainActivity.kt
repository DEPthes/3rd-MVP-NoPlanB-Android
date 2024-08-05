package com.growme.growme.presentation.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.growme.growme.R
import com.growme.growme.databinding.ActivityMainBinding
import com.growme.growme.presentation.views.calendar.CalendarFragment
import com.growme.growme.presentation.views.home.HomeFragment
import com.growme.growme.presentation.views.item.ItemFragment
import com.growme.growme.presentation.views.mypage.MyPageFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 처음 로딩 시 HomeFragment를 기본으로 설정
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment(), false)
        }

        setBottomNavi()
    }

    private fun setBottomNavi(){
        binding.bottomNavi.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.menu_home-> {
                    replaceFragment(HomeFragment(), false)
                    return@setOnItemSelectedListener true
                }
                R.id.menu_calendar -> {
                    replaceFragment(CalendarFragment(), false)
                    return@setOnItemSelectedListener true
                }
                R.id.menu_item -> {
                    replaceFragment(ItemFragment(), false)
                    return@setOnItemSelectedListener true
                }
                R.id.menu_mypage -> {
                    replaceFragment(MyPageFragment(), false)
                    return@setOnItemSelectedListener true
                }
                else -> return@setOnItemSelectedListener false
            }
        }

        binding.bottomNavi.setOnItemReselectedListener {  } // 재요청 방지용
    }

    fun replaceFragment(fragment: Fragment, addToBackStack: Boolean) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_main, fragment)
        if (addToBackStack) fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

}