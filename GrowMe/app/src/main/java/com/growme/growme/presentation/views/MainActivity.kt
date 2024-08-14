package com.growme.growme.presentation.views

import android.os.Bundle
import android.view.View
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

        val skinValue = intent.getIntExtra("skin", 1)
        val faceValue = intent.getIntExtra("face", 1)
        val hairValue = intent.getIntExtra("hair", 1)
        val clothesValue = intent.getIntExtra("clothes", 11)
        val nameValue = intent.getStringExtra("name") ?: "grow me"

        // 처음 로딩 시 HomeFragment를 기본으로 설정
        val homeFragment = HomeFragment().apply {
            arguments = Bundle().apply {
                putInt("skin", skinValue)
                putInt("face", faceValue)
                putInt("hair", hairValue)
                putInt("clothes", clothesValue)
                putString("name", nameValue)
            }
        }
        if (savedInstanceState == null) {
            replaceFragment(homeFragment, false)
        }

        setBottomNavi()
    }

    private fun setBottomNavi() {
        binding.bottomNavi.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
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

        binding.bottomNavi.setOnItemReselectedListener { } // 재요청 방지용
    }

    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean) {
        // MyPageFragment일 때는 Toolbar 숨기기
        if (fragment is MyPageFragment) {
            binding.tbMain.visibility = View.GONE
        } else {
            binding.tbMain.visibility = View.VISIBLE
        }

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_main, fragment)
        if (addToBackStack) fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun navigateToHomeFragment(bundle: Bundle) {
        val homeFragment = HomeFragment().apply {
            arguments = bundle
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_main, homeFragment)
            .addToBackStack(null)
            .commit()
    }
}