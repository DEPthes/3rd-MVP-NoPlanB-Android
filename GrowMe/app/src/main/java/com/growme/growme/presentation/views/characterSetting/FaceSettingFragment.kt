package com.growme.growme.presentation.views.characterSetting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growme.growme.R
import com.growme.growme.databinding.FragmentFaceSettingBinding

class FaceSettingFragment : Fragment() {
    private val binding by lazy {
        FragmentFaceSettingBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.btnFace1.setOnClickListener {
            binding.btnFace1.setBackgroundResource(R.drawable.btn_mini_selected)
            binding.btnFace2.setBackgroundResource(R.drawable.btn_mini_default)
            binding.btnFace3.setBackgroundResource(R.drawable.btn_mini_default)
        }
        binding.btnFace2.setOnClickListener {
            binding.btnFace1.setBackgroundResource(R.drawable.btn_mini_default)
            binding.btnFace2.setBackgroundResource(R.drawable.btn_mini_selected)
            binding.btnFace3.setBackgroundResource(R.drawable.btn_mini_default)
        }
        binding.btnFace3.setOnClickListener {
            binding.btnFace1.setBackgroundResource(R.drawable.btn_mini_default)
            binding.btnFace2.setBackgroundResource(R.drawable.btn_mini_default)
            binding.btnFace3.setBackgroundResource(R.drawable.btn_mini_selected)
        }

        return binding.root
    }
}