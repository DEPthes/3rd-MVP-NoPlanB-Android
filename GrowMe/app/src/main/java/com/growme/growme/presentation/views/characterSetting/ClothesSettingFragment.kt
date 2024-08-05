package com.growme.growme.presentation.views.characterSetting

import android.content.Context
import android.os.Bundle
import android.util.Log
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
    private lateinit var characterSettingActivity: CharacterSettingActivity
    private var clothesNum = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 피부색 정보 받아오기
        val skinValue = arguments?.getInt("skin", -1) // Default value if the key is not found
        // 피부색 설정
        when (skinValue) {
            1 -> binding.layerCharacter.setBackgroundResource(R.drawable.character_1)
            2 -> binding.layerCharacter.setBackgroundResource(R.drawable.character_2)
            3 -> binding.layerCharacter.setBackgroundResource(R.drawable.character_3)
            else -> Log.e("ClothesSettingFragment", "Unknown skin value: $skinValue")
        }

        // 표정 정보 받아오기
        val faceValue = arguments?.getInt("face", -1)
        // 표정 설정
        when (faceValue) {
            1 -> binding.layerFace.setBackgroundResource(R.drawable.face_1)
            2 -> binding.layerFace.setBackgroundResource(R.drawable.face_2)
            3 -> binding.layerFace.setBackgroundResource(R.drawable.face_3)
            else -> Log.e("ClothesSettingFragment", "Unknown face value: $faceValue")
        }

        // 머리 정보 받아오기
        val hairValue = arguments?.getInt("hair", -1)
        // 머리 설정
        when (hairValue) {
            1 -> binding.layerHair.setBackgroundResource(R.drawable.hair1_for_total_character)
            2 -> binding.layerHair.setBackgroundResource(R.drawable.hair2_for_total_character)
            3 -> binding.layerHair.setBackgroundResource(R.drawable.hair3_for_total_character)
        }

        binding.btnCloth1.setOnClickListener {
            setClothSelection(1, R.drawable.clothes_1)
        }
        binding.btnCloth2.setOnClickListener {
            setClothSelection(2, R.drawable.clothes_2)
        }
        binding.btnNext.setOnClickListener {
            if (skinValue != null && faceValue != null && hairValue != null) {
                sendDataAndNavigate(skinValue, faceValue, hairValue)
            }
        }
        binding.btnBack.setOnClickListener {
            if (skinValue != null && faceValue != null && hairValue != null) {
                getBackAndSendData(skinValue, faceValue, hairValue)
            }
        }

        return binding.root
    }
    private fun setClothSelection(clothNumber: Int, clothDrawable: Int) {
        binding.btnCloth1.setBackgroundResource(if (clothNumber == 1) R.drawable.btn_mini_selected else R.drawable.btn_mini_default)
        binding.btnCloth2.setBackgroundResource(if (clothNumber == 2) R.drawable.btn_mini_selected else R.drawable.btn_mini_default)
        binding.layerClothes.setBackgroundResource(clothDrawable)
        clothesNum = clothNumber
    }
    private fun sendDataAndNavigate(skinValue: Int, faceValue: Int, hairValue: Int) {
        val nameSettingFragment = NameSettingFragment().apply {
            arguments = Bundle().apply {
                putInt("skin", skinValue)
                putInt("face", faceValue)
                putInt("hair", hairValue)
                putInt("clothes", clothesNum)
            }
        }
        characterSettingActivity.replaceFragment(nameSettingFragment, true)
    }
    private fun getBackAndSendData(skinValue: Int, faceValue: Int, hairValue: Int) {
        val hairSettingFragment = HairSettingFragment().apply {
            arguments = Bundle().apply {
                putInt("skin", skinValue)
                putInt("face", faceValue)
                putInt("hair", hairValue)
            }
        }
        characterSettingActivity.replaceFragment(hairSettingFragment, true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CharacterSettingActivity) characterSettingActivity = context
    }
}