package com.growme.growme.presentation.views.characterSetting

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.growme.growme.R
import com.growme.growme.data.LoggerUtils
import com.growme.growme.databinding.FragmentSettingCompleteBinding
import com.growme.growme.presentation.UiState
import com.growme.growme.presentation.views.MainActivity

class SettingCompleteFragment : Fragment() {
    private val binding by lazy {
        FragmentSettingCompleteBinding.inflate(layoutInflater)
    }
    private val characterSettingViewModel : CharacterSettingViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val skinValue = arguments?.getInt("skin", 1)
        // 피부색 정보 받아와서 설정
        when (skinValue) {
            1 -> binding.layerCharacter.setBackgroundResource(R.drawable.character_1)
            2 -> binding.layerCharacter.setBackgroundResource(R.drawable.character_2)
            3 -> binding.layerCharacter.setBackgroundResource(R.drawable.character_3)
            else -> LoggerUtils.e("skin value를 찾을 수 없습니다.: $skinValue")
        }

        val faceValue = arguments?.getInt("face", 1)
        // 표정 정보 받아와서 설정
        when (faceValue) {
            1 -> binding.layerFace.setBackgroundResource(R.drawable.face_1)
            2 -> binding.layerFace.setBackgroundResource(R.drawable.face_2)
            3 -> binding.layerFace.setBackgroundResource(R.drawable.face_3)
            else -> LoggerUtils.e("face value를 찾을 수 없습니다.: $faceValue")
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
        binding.txtName.text = "$nameValue 님!"

        Glide.with(requireContext())
            .load(R.raw.animation)
            .into(binding.gif)

        binding.btnStart.setOnClickListener {
            // MainActivity - HomeFragment로 이동
            val intent = Intent(requireContext(), MainActivity::class.java).apply {
                putExtra("skin", skinValue)
                putExtra("face", faceValue)
                putExtra("hair", hairValue)
                putExtra("clothes", clothesValue)
                putExtra("name", nameValue)
            }

            characterSettingViewModel.makeInitCharacter(nameValue!!, listOf(skinValue!!, faceValue!!+3, hairValue!!+6, clothesValue!!+9))

            startActivity(intent)
            activity?.finish()
        }

        return binding.root
    }

    private fun observer() {
        characterSettingViewModel.makeInitCharacterState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("초기 캐릭터 생성 실패")
                UiState.Loading -> {}
                is UiState.Success -> LoggerUtils.d("초기 캐릭터 생성 성공")
            }
        }
    }
}