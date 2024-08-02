package com.growme.growme.presentation.views.characterSetting

import android.content.Context
import android.os.Bundle
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
    lateinit var characterSettingActivity: CharacterSettingActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.btnFace1.setOnClickListener {
            binding.btnFace1.setBackgroundResource(R.drawable.btn_mini_selected)
            binding.btnFace2.setBackgroundResource(R.drawable.btn_mini_default)
            binding.btnFace3.setBackgroundResource(R.drawable.btn_mini_default)
            binding.layerFace.setBackgroundResource(R.drawable.face_1)
        }
        binding.btnFace2.setOnClickListener {
            binding.btnFace1.setBackgroundResource(R.drawable.btn_mini_default)
            binding.btnFace2.setBackgroundResource(R.drawable.btn_mini_selected)
            binding.btnFace3.setBackgroundResource(R.drawable.btn_mini_default)
            binding.layerFace.setBackgroundResource(R.drawable.face_2)
        }
        binding.btnFace3.setOnClickListener {
            binding.btnFace1.setBackgroundResource(R.drawable.btn_mini_default)
            binding.btnFace2.setBackgroundResource(R.drawable.btn_mini_default)
            binding.btnFace3.setBackgroundResource(R.drawable.btn_mini_selected)
            binding.layerFace.setBackgroundResource(R.drawable.face_3)
        }
        binding.btnNext.setOnClickListener {
            characterSettingActivity.replaceFragment(HairSettingFragment(), true)
        }
        binding.btnBack.setOnClickListener {
            characterSettingActivity.replaceFragment(SkinSettingFragment(), false)
        }

        return binding.root
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CharacterSettingActivity) characterSettingActivity = context
    }
}