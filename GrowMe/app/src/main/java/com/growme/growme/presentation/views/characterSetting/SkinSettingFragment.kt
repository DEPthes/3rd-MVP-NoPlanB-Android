package com.growme.growme.presentation.views.characterSetting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growme.growme.R
import com.growme.growme.databinding.FragmentSkinSettingBinding

class SkinSettingFragment : Fragment() {
    private val binding by lazy {
        FragmentSkinSettingBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.btnSkin1.setOnClickListener {
            binding.btnSkin1.setBackgroundResource(R.drawable.btn_mini_selected)
            binding.btnSkin2.setBackgroundResource(R.drawable.btn_mini_default)
            binding.btnSkin3.setBackgroundResource(R.drawable.btn_mini_default)
        }
        binding.btnSkin2.setOnClickListener {
            binding.btnSkin1.setBackgroundResource(R.drawable.btn_mini_default)
            binding.btnSkin2.setBackgroundResource(R.drawable.btn_mini_selected)
            binding.btnSkin3.setBackgroundResource(R.drawable.btn_mini_default)
        }
        binding.btnSkin3.setOnClickListener {
            binding.btnSkin1.setBackgroundResource(R.drawable.btn_mini_default)
            binding.btnSkin2.setBackgroundResource(R.drawable.btn_mini_default)
            binding.btnSkin3.setBackgroundResource(R.drawable.btn_mini_selected)
        }

        return binding.root
    }
}