package com.growme.growme.presentation.views.characterSetting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.growme.growme.R
import com.growme.growme.databinding.FragmentClothesSettingBinding

class ClothesSettingFragment : Fragment() {
    private val binding by lazy {
        FragmentClothesSettingBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.btnCloth1.setOnClickListener {
            binding.btnCloth1.setBackgroundResource(R.drawable.btn_mini_selected)
            binding.btnCloth2.setBackgroundResource(R.drawable.btn_mini_default)
        }
        binding.btnCloth2.setOnClickListener {
            binding.btnCloth1.setBackgroundResource(R.drawable.btn_mini_default)
            binding.btnCloth2.setBackgroundResource(R.drawable.btn_mini_selected)
        }

        return binding.root
    }
}