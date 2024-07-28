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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.btnHair1.setOnClickListener {
            binding.btnHair1.setBackgroundResource(R.drawable.btn_mini_selected)
            binding.btnHair2.setBackgroundResource(R.drawable.btn_mini_default)
            binding.btnHair3.setBackgroundResource(R.drawable.btn_mini_default)
        }
        binding.btnHair2.setOnClickListener {
            binding.btnHair1.setBackgroundResource(R.drawable.btn_mini_default)
            binding.btnHair2.setBackgroundResource(R.drawable.btn_mini_selected)
            binding.btnHair3.setBackgroundResource(R.drawable.btn_mini_default)
        }
        binding.btnHair3.setOnClickListener {
            binding.btnHair1.setBackgroundResource(R.drawable.btn_mini_default)
            binding.btnHair2.setBackgroundResource(R.drawable.btn_mini_default)
            binding.btnHair3.setBackgroundResource(R.drawable.btn_mini_selected)
        }

        return binding.root
    }
}