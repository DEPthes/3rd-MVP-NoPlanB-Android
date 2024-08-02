package com.growme.growme.presentation.views.characterSetting

import android.content.Context
import android.media.FaceDetector.Face
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.growme.growme.R
import com.growme.growme.databinding.FragmentSkinSettingBinding

class SkinSettingFragment : Fragment() {
    private val binding by lazy {
        FragmentSkinSettingBinding.inflate(layoutInflater)
    }
    lateinit var characterSettingActivity : CharacterSettingActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.btnSkin1.setOnClickListener {
            binding.btnSkin1.setBackgroundResource(R.drawable.btn_mini_selected)
            binding.btnSkin2.setBackgroundResource(R.drawable.btn_mini_default)
            binding.btnSkin3.setBackgroundResource(R.drawable.btn_mini_default)
            binding.layerHead.setBackgroundResource(R.drawable.head_1)
        }
        binding.btnSkin2.setOnClickListener {
            binding.btnSkin1.setBackgroundResource(R.drawable.btn_mini_default)
            binding.btnSkin2.setBackgroundResource(R.drawable.btn_mini_selected)
            binding.btnSkin3.setBackgroundResource(R.drawable.btn_mini_default)
            binding.layerHead.setBackgroundResource(R.drawable.head_2)
        }
        binding.btnSkin3.setOnClickListener {
            binding.btnSkin1.setBackgroundResource(R.drawable.btn_mini_default)
            binding.btnSkin2.setBackgroundResource(R.drawable.btn_mini_default)
            binding.btnSkin3.setBackgroundResource(R.drawable.btn_mini_selected)
            binding.layerHead.setBackgroundResource(R.drawable.head_3)
        }
        binding.btnNext.setOnClickListener {
            characterSettingActivity.replaceFragment(FaceSettingFragment(), true)
        }

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CharacterSettingActivity) characterSettingActivity = context
    }
}