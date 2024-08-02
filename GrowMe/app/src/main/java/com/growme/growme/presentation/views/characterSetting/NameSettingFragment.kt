package com.growme.growme.presentation.views.characterSetting

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.growme.growme.databinding.FragmentNameSettingBinding

class NameSettingFragment : Fragment() {
    private val binding by lazy {
        FragmentNameSettingBinding.inflate(layoutInflater)
    }
    lateinit var characterSettingActivity: CharacterSettingActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.btnSubmit.setOnClickListener {
            characterSettingActivity.replaceFragment(SettingCompleteFragment(), true)
        }
        binding.btnBack.setOnClickListener {
            characterSettingActivity.replaceFragment(ClothesSettingFragment(), true)
        }

        return binding.root
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CharacterSettingActivity) characterSettingActivity = context
    }
}