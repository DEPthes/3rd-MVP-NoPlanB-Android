package com.growme.growme.presentation.views.characterSetting

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.growme.growme.R
import com.growme.growme.databinding.FragmentHairSettingBinding

class HairSettingFragment : Fragment() {
    private val binding by lazy {
        FragmentHairSettingBinding.inflate(layoutInflater)
    }
    private lateinit var characterSettingActivity: CharacterSettingActivity
    private var hairNum = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 피부색 정보 받아오기
        val skinValue = arguments?.getInt("skin", -1) // Default value if the key is not found
        // 피부색 설정
        when (skinValue) {
            1 -> binding.layerHead.setBackgroundResource(R.drawable.head_1)
            2 -> binding.layerHead.setBackgroundResource(R.drawable.head_2)
            3 -> binding.layerHead.setBackgroundResource(R.drawable.head_3)
            else -> Log.e("HairSettingFragment", "Unknown skin value: $skinValue")
        }

        // 표정 정보 받아오기
        val faceValue = arguments?.getInt("face", -1)
        // 표정 설정
        when (faceValue) {
            1 -> binding.layerFace.setBackgroundResource(R.drawable.face_1)
            2 -> binding.layerFace.setBackgroundResource(R.drawable.face_2)
            3 -> binding.layerFace.setBackgroundResource(R.drawable.face_3)
            else -> Log.e("HairSettingFragment", "Unknown face value: $faceValue")
        }

        // Set up button click listeners
        binding.btnHair1.setOnClickListener {
            setHairSelection(1)
        }
        binding.btnHair2.setOnClickListener {
            setHairSelection(2)
        }
        binding.btnHair3.setOnClickListener {
            setHairSelection(3)
        }
        binding.btnNext.setOnClickListener {
            if (skinValue != null && faceValue != null) {
                sendDataAndNavigate(skinValue, faceValue)
            }
        }
        binding.btnBack.setOnClickListener {
            if (skinValue != null && faceValue != null) {
                getBackAndSendData(skinValue, faceValue)
            }
        }

        return binding.root
    }
    private fun setHairSelection(hairNumber: Int) {
        binding.btnHair1.setBackgroundResource(if (hairNumber == 1) R.drawable.btn_mini_selected else R.drawable.btn_mini_default)
        binding.btnHair2.setBackgroundResource(if (hairNumber == 2) R.drawable.btn_mini_selected else R.drawable.btn_mini_default)
        binding.btnHair3.setBackgroundResource(if (hairNumber == 3) R.drawable.btn_mini_selected else R.drawable.btn_mini_default)

        binding.layerHair1.visibility = if (hairNumber == 1) View.VISIBLE else View.GONE
        binding.layerHair2.visibility = if (hairNumber == 2) View.VISIBLE else View.GONE
        binding.layerHair3.visibility = if (hairNumber == 3) View.VISIBLE else View.GONE

        hairNum = hairNumber
    }
    private fun sendDataAndNavigate(skinValue: Int, faceValue: Int) {
        // Create a new instance of ClothesSettingFragment with arguments
        val clothesSettingFragment = ClothesSettingFragment().apply {
            arguments = Bundle().apply {
                Log.d("hairNum", "$hairNum")
                putInt("skin", skinValue)
                putInt("face", faceValue)
                putInt("hair", hairNum)
            }
        }
        characterSettingActivity.replaceFragment(clothesSettingFragment, true)
    }
    private fun getBackAndSendData(skinValue: Int, faceValue: Int) {
        val faceSettingFragment = FaceSettingFragment().apply {
            arguments = Bundle().apply {
                putInt("skin", skinValue)
                putInt("face", faceValue)
            }
        }
        characterSettingActivity.replaceFragment(faceSettingFragment, true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CharacterSettingActivity) characterSettingActivity = context
    }
}