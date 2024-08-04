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
    lateinit var characterSettingActivity: CharacterSettingActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 피부색 정보 받아오기
        val skinValue = arguments?.getInt("skin", -1) // Default value if the key is not found
        Log.d("SKIN", "$skinValue")
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

        binding.btnHair1.setOnClickListener {
            binding.btnHair1.setBackgroundResource(R.drawable.btn_mini_selected)
            binding.btnHair2.setBackgroundResource(R.drawable.btn_mini_default)
            binding.btnHair3.setBackgroundResource(R.drawable.btn_mini_default)
            binding.layerHair1.visibility = View.VISIBLE
            binding.layerHair2.visibility = View.GONE
            binding.layerHair3.visibility = View.GONE
        }
        binding.btnHair2.setOnClickListener {
            binding.btnHair1.setBackgroundResource(R.drawable.btn_mini_default)
            binding.btnHair2.setBackgroundResource(R.drawable.btn_mini_selected)
            binding.btnHair3.setBackgroundResource(R.drawable.btn_mini_default)
            binding.layerHair1.visibility = View.GONE
            binding.layerHair2.visibility = View.VISIBLE
            binding.layerHair3.visibility = View.GONE
        }
        binding.btnHair3.setOnClickListener {
            binding.btnHair1.setBackgroundResource(R.drawable.btn_mini_default)
            binding.btnHair2.setBackgroundResource(R.drawable.btn_mini_default)
            binding.btnHair3.setBackgroundResource(R.drawable.btn_mini_selected)
            binding.layerHair1.visibility = View.GONE
            binding.layerHair2.visibility = View.GONE
            binding.layerHair3.visibility = View.VISIBLE
        }
        binding.btnNext.setOnClickListener {
            characterSettingActivity.replaceFragment(ClothesSettingFragment(), true)
        }
        binding.btnBack.setOnClickListener {
            characterSettingActivity.replaceFragment(FaceSettingFragment(), true)
        }

        return binding.root
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CharacterSettingActivity) characterSettingActivity = context
    }
}