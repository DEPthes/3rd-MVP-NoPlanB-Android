package com.growme.growme.presentation.views.characterSetting

import android.content.Context
import android.os.Bundle
import android.util.Log
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
    private lateinit var characterSettingActivity : CharacterSettingActivity
    private var skinNum = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 피부색 정보 받아오기
        val skinValue = arguments?.getInt("skin", -1)
        Log.d("SKIN", "$skinValue")
        if (skinValue != null) {
            skinNum = skinValue
        }
        when (skinValue) {
            1 -> binding.layerHead.setBackgroundResource(R.drawable.head_1)
            2 -> binding.layerHead.setBackgroundResource(R.drawable.head_2)
            3 -> binding.layerHead.setBackgroundResource(R.drawable.head_3)
            else -> Log.e("SkinSettingFragment", "Unknown skin value: $skinValue")
        }

        binding.btnSkin1.setOnClickListener {
            setSkinSelection(1, R.drawable.head_1)
        }
        binding.btnSkin2.setOnClickListener {
            setSkinSelection(2, R.drawable.head_2)
        }
        binding.btnSkin3.setOnClickListener {
            setSkinSelection(3, R.drawable.head_3)
        }
        binding.btnNext.setOnClickListener {
            sendDataAndNavigate()
        }

        return binding.root
    }
    private fun setSkinSelection(skinNumber: Int, headDrawable: Int) {
        binding.btnSkin1.setBackgroundResource(if (skinNumber == 1) R.drawable.btn_mini_selected else R.drawable.btn_mini_default)
        binding.btnSkin2.setBackgroundResource(if (skinNumber == 2) R.drawable.btn_mini_selected else R.drawable.btn_mini_default)
        binding.btnSkin3.setBackgroundResource(if (skinNumber == 3) R.drawable.btn_mini_selected else R.drawable.btn_mini_default)
        binding.layerHead.setBackgroundResource(headDrawable)
        skinNum = skinNumber
    }
    private fun sendDataAndNavigate() {
        // Create a new instance of FaceSettingFragment with arguments
        val faceSettingFragment = FaceSettingFragment().apply {
            arguments = Bundle().apply {
                Log.d("skinNum", "$skinNum")
                putInt("skin", skinNum)
            }
        }
        // Navigate to FaceSettingFragment
        characterSettingActivity.replaceFragment(faceSettingFragment, true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CharacterSettingActivity) characterSettingActivity = context
    }
}