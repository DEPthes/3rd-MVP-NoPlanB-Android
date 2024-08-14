package com.growme.growme.presentation.views.characterSetting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.growme.growme.R
import com.growme.growme.databinding.ActivityCharacterSettingBinding

class CharacterSettingActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityCharacterSettingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val skinSettingFragment = SkinSettingFragment().apply {
            arguments = Bundle().apply {
                putInt("skinValue", 1)
            }
        }
        replaceFragment(skinSettingFragment, true)
    }

    fun replaceFragment(fragment: Fragment, addToBackStack: Boolean) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_ch_setting, fragment)
        if (addToBackStack) fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}