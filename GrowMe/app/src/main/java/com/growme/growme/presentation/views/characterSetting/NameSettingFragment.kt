package com.growme.growme.presentation.views.characterSetting

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.growme.growme.R
import com.growme.growme.data.LoggerUtils
import com.growme.growme.databinding.FragmentNameSettingBinding

class NameSettingFragment : Fragment() {
    private val binding by lazy {
        FragmentNameSettingBinding.inflate(layoutInflater)
    }
    private lateinit var characterSettingActivity: CharacterSettingActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 피부색 정보 받아오기
        val skinValue = arguments?.getInt("skin", 1)
        // 피부색 설정
        when (skinValue) {
            1 -> binding.layerCharacter.setBackgroundResource(R.drawable.character_1)
            2 -> binding.layerCharacter.setBackgroundResource(R.drawable.character_2)
            3 -> binding.layerCharacter.setBackgroundResource(R.drawable.character_3)
            else -> LoggerUtils.e("skin value를 찾을 수 없습니다.: $skinValue")
        }

        // 표정 정보 받아오기
        val faceValue = arguments?.getInt("face", 1)
        // 표정 설정
        when (faceValue) {
            1 -> binding.layerFace.setBackgroundResource(R.drawable.face_1)
            2 -> binding.layerFace.setBackgroundResource(R.drawable.face_2)
            3 -> binding.layerFace.setBackgroundResource(R.drawable.face_3)
            else -> LoggerUtils.e("face value를 찾을 수 없습니다.: $faceValue")
        }

        // 머리 정보 받아오기
        val hairValue = arguments?.getInt("hair", 1)
        // 머리 설정
        when (hairValue) {
            1 -> binding.layerHair.setBackgroundResource(R.drawable.hair1_for_total_character)
            2 -> binding.layerHair.setBackgroundResource(R.drawable.hair2_for_total_character)
            3 -> binding.layerHair.setBackgroundResource(R.drawable.hair3_for_total_character)
            else -> LoggerUtils.e("hair value를 찾을 수 없습니다.: $hairValue")
        }

        // 옷 정보 받아오기
        val clothesValue = arguments?.getInt("clothes", 1)
        // 옷 설정
        when (clothesValue) {
            1 -> binding.layerClothes.setBackgroundResource(R.drawable.clothes_1)
            2 -> binding.layerClothes.setBackgroundResource(R.drawable.clothes_2)
            else -> LoggerUtils.e("clothes value를 찾을 수 없습니다.: $clothesValue")
        }

        binding.btnSubmit.setOnClickListener {
            val nameValue = binding.edtName.text.toString()

            if (nameValue == "") {
                Toast.makeText(requireContext(), "이름은 빈칸일 수 없습니다!", Toast.LENGTH_SHORT).show()
            }

            if (skinValue != null && faceValue != null && hairValue != null && clothesValue != null) {
                sendDataAndNavigate(skinValue, faceValue, hairValue, clothesValue)
            }
        }
        binding.btnBack.setOnClickListener {
            if (skinValue != null && faceValue != null && hairValue != null && clothesValue != null) {
                getBackAndSendData(skinValue, faceValue, hairValue, clothesValue)
            }
        }

        return binding.root
    }

    private fun sendDataAndNavigate(
        skinValue: Int,
        faceValue: Int,
        hairValue: Int,
        clothesValue: Int
    ) {
        val settingCompleteFragment = SettingCompleteFragment().apply {
            arguments = Bundle().apply {
                putInt("skin", skinValue)
                putInt("face", faceValue)
                putInt("hair", hairValue)
                putInt("clothes", clothesValue)
                putString("name", binding.edtName.text.toString())
            }
        }
        characterSettingActivity.replaceFragment(settingCompleteFragment, true)
    }

    private fun getBackAndSendData(
        skinValue: Int,
        faceValue: Int,
        hairValue: Int,
        clothesValue: Int
    ) {
        val clothesSettingFragment = ClothesSettingFragment().apply {
            arguments = Bundle().apply {
                putInt("skin", skinValue)
                putInt("face", faceValue)
                putInt("hair", hairValue)
                putInt("clothes", clothesValue)
            }
        }
        characterSettingActivity.replaceFragment(clothesSettingFragment, true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CharacterSettingActivity) characterSettingActivity = context
    }
}