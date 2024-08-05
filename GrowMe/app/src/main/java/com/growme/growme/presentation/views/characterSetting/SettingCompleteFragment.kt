package com.growme.growme.presentation.views.characterSetting

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growme.growme.R
import com.growme.growme.databinding.FragmentSettingCompleteBinding

class SettingCompleteFragment : Fragment() {
    private val binding by lazy {
        FragmentSettingCompleteBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 피부색 정보 받아오기
        val skinValue = arguments?.getInt("skin", -1)
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

        // 옷 정보 받아오기
        val clothesValue = arguments?.getInt("clothes", -1)
        when (clothesValue) {
            1 -> binding.layerClothes.setBackgroundResource(R.drawable.clothes_1)
            2 -> binding.layerClothes.setBackgroundResource(R.drawable.clothes_2)
        }

        // 이름 정보 받아오기
        val nameValue = arguments?.getString("name", "grow me")
        Log.d("name", nameValue.toString())
        binding.txtName.text = nameValue

        return binding.root
    }
}