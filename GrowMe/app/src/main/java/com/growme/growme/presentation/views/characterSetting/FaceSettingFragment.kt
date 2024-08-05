package com.growme.growme.presentation.views.characterSetting

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.growme.growme.R
import com.growme.growme.databinding.FragmentFaceSettingBinding

class FaceSettingFragment : Fragment() {
    private val binding by lazy {
        FragmentFaceSettingBinding.inflate(layoutInflater)
    }
    private lateinit var characterSettingActivity: CharacterSettingActivity
    private var faceNum = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 피부색 정보 받아오기
        val skinValue = arguments?.getInt("skin", -1)
        Log.d("SKIN", "$skinValue")
        // 피부색 설정
        when (skinValue) {
            1 -> binding.layerHead.setBackgroundResource(R.drawable.head_1)
            2 -> binding.layerHead.setBackgroundResource(R.drawable.head_2)
            3 -> binding.layerHead.setBackgroundResource(R.drawable.head_3)
            else -> Log.e("FaceSettingFragment", "Unknown skin value: $skinValue")
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

        binding.btnFace1.setOnClickListener {
            setFaceSelection(1, R.drawable.face_1)
        }
        binding.btnFace2.setOnClickListener {
            setFaceSelection(2, R.drawable.face_2)
        }
        binding.btnFace3.setOnClickListener {
            setFaceSelection(3, R.drawable.face_3)
        }
        binding.btnNext.setOnClickListener {
            if (skinValue != null) {
                sendDataAndNavigate(skinValue)
            }
        }
        binding.btnBack.setOnClickListener {
            if (skinValue != null) {
                getBackAndSendData(skinValue)
            }
        }

        return binding.root
    }
    private fun setFaceSelection(faceNumber: Int, faceDrawable: Int) {
        binding.btnFace1.setBackgroundResource(if (faceNumber == 1) R.drawable.btn_mini_selected else R.drawable.btn_mini_default)
        binding.btnFace2.setBackgroundResource(if (faceNumber == 2) R.drawable.btn_mini_selected else R.drawable.btn_mini_default)
        binding.btnFace3.setBackgroundResource(if (faceNumber == 3) R.drawable.btn_mini_selected else R.drawable.btn_mini_default)
        binding.layerFace.setBackgroundResource(faceDrawable)
        faceNum = faceNumber
    }
    private fun sendDataAndNavigate(skinValue : Int) {
        val hairSettingFragment = HairSettingFragment().apply {
            arguments = Bundle().apply {
                Log.d("faceNum", "$faceNum")
                putInt("skin", skinValue)
                putInt("face", faceNum)
            }
        }
        // fragment 이동
        characterSettingActivity.replaceFragment(hairSettingFragment, true)
    }
    private fun getBackAndSendData(skinValue: Int) {
        val skinSettingFragment = SkinSettingFragment().apply {
            arguments = Bundle().apply {
                putInt("skin", skinValue)
            }
        }
        characterSettingActivity.replaceFragment(skinSettingFragment, true)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CharacterSettingActivity) characterSettingActivity = context
    }
}