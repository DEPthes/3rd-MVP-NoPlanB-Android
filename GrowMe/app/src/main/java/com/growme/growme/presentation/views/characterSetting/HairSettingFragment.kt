package com.growme.growme.presentation.views.characterSetting

import android.content.Context
import android.os.Bundle
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