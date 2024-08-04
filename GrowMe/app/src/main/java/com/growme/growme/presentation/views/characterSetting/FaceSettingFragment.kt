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
        // Retrieve the arguments
        val intValue = arguments?.getInt("skin", -1) // Default value if the key is not found
        Log.d("SKIN", "$intValue")

        // Set the background resource based on the received value
        when (intValue) {
            1 -> binding.layerHead.setBackgroundResource(R.drawable.head_1)
            2 -> binding.layerHead.setBackgroundResource(R.drawable.head_2)
            3 -> binding.layerHead.setBackgroundResource(R.drawable.head_3)
            else -> Log.e("FaceSettingFragment", "Unknown skin value: $intValue")
        }

        // Set up button click listeners
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
            sendDataAndNavigate()
        }
        binding.btnBack.setOnClickListener {
            characterSettingActivity.replaceFragment(SkinSettingFragment(), false)
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
    private fun sendDataAndNavigate() {
        // Create a new instance of FaceSettingFragment with arguments
        val hairSettingFragment = HairSettingFragment().apply {
            arguments = Bundle().apply {
                Log.d("faceNum", "$faceNum")
//                putInt("skin", requireArguments().getInt("skin", -1))
                putInt("face", faceNum)
            }
        }
        // Navigate to FaceSettingFragment
        characterSettingActivity.replaceFragment(hairSettingFragment, true)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CharacterSettingActivity) characterSettingActivity = context
    }
}