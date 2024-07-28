package com.growme.growme.presentation.views.characterSetting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growme.growme.R
import com.growme.growme.databinding.FragmentClothesSettingBinding
import com.growme.growme.databinding.FragmentNameSettingBinding

class NameSettingFragment : Fragment() {
    private val binding by lazy {
        FragmentNameSettingBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }
}