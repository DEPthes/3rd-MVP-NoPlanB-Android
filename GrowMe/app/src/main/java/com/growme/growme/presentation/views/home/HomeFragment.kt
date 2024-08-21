package com.growme.growme.presentation.views.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.bumptech.glide.request.transition.Transition
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.CustomTarget
import com.growme.growme.R
import com.growme.growme.data.LoggerUtils
import com.growme.growme.databinding.DialogAddQuestBinding
import com.growme.growme.databinding.DialogDoneAllQuestBinding
import com.growme.growme.databinding.DialogDoneQuestBinding
import com.growme.growme.databinding.DialogLevelMaxBinding
import com.growme.growme.databinding.DialogLevelupBinding
import com.growme.growme.databinding.DialogLevelupUnlockBinding
import com.growme.growme.databinding.DialogModifyQuestBinding
import com.growme.growme.databinding.FragmentHomeBinding
import com.growme.growme.domain.model.home.ItemData
import com.growme.growme.domain.model.quest.QuestInfo
import com.growme.growme.presentation.UiState
import com.growme.growme.presentation.base.GlobalApplication
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var questRvAdapter: QuestRvAdapter
    private val viewModel: HomeViewModel by viewModels()

    private var questList = mutableListOf<QuestInfo>()
    private val today = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN).format(Date())
    private var todayGetExp = 0
    private var todayTotalExp = 0
    private var isQuestDone = false
    private var isQuestAllDone = false
    private var chatImageIndex = 0
    private val chatImages = arrayOf(
        R.drawable.bubble_chat1,
        R.drawable.bubble_chat2,
        R.drawable.bubble_chat3,
        R.drawable.bubble_chat4
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivChat.visibility = View.GONE
        setObserver()
        initListener()
        fetchData()
    }

    private fun fetchData() {
        viewModel.fetchCharacterInfo()
        viewModel.fetchExpInfo()
        viewModel.fetchQuestInfo(today)
    }

    private fun initListener() {
        binding.ivAddQuest.setOnClickListener {
            if (todayTotalExp >= 10) {
                Toast.makeText(requireContext(), "하루에 얻을 수 있는 경험치는 최대 10입니다!", Toast.LENGTH_LONG)
                    .show()
            } else {
                showAddQuestDialog(todayTotalExp)
            }
        }

        binding.ivCharacter.setOnClickListener {
            binding.ivChat.visibility = View.VISIBLE
            chatImageIndex = (chatImageIndex + 1) % chatImages.size
            binding.ivChat.setImageResource(chatImages[chatImageIndex])
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.expState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("Home Data 조회 실패: ${it.error}")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    todayTotalExp = it.data.totQuestExp
                    todayGetExp = it.data.todayExp
                    if (todayTotalExp == 10) {
                        binding.ivAddQuest.visibility = View.GONE
                    }

                    val acquireExp = it.data.acquireExp
                    val needExp = it.data.needExp
                    val result = ((acquireExp.toDouble() / needExp.toDouble()) * 10).toInt()
                    showExpProgress(result)

                    GlobalApplication.userLevel = it.data.level
                    binding.tvMyLevel.text = "LV ${GlobalApplication.userLevel}"
                    binding.tvExp.text = "${acquireExp}/${needExp}"
                }
            }
        }

        viewModel.characterState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("Character Data 조회 실패: ${it.error}")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    val nickname = it.data.characterName
                    GlobalApplication.nickname = nickname
                    binding.tvNickname.text = nickname

                    // 장착된 캐릭터 로드
                    val itemList = it.data.myCharaterDetailResList
                    itemList.forEach { item ->
                        handleItem(ItemData(item.itemType, item.itemImage))
                    }
                }
            }
        }

        viewModel.questState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("Quest Data 조회 실패: ${it.error}")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    questList = it.data.toMutableList()
                    questList.sortBy { data -> data.isComplete }

                    questRvAdapter = QuestRvAdapter(
                        { position -> showModifyQuestDialog(position) },
                        { position -> viewModel.completeQuest(questList[position].id, position) },
                        false,
                        today
                    )
                    questRvAdapter.setData(questList)
                    binding.rvTodayQuest.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = questRvAdapter
                    }
                }
            }
        }

        viewModel.addState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("Add Quest 실패: ${it.error}")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    LoggerUtils.d(it.data.msg)
                    updateUI()
                }
            }
        }

        viewModel.updateState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("Quest 수정 실패: ${it.error}")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    LoggerUtils.d(it.data.msg)
                    updateUI()
                }
            }
        }

        viewModel.completeState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("Complete Quest 실패: ${it.error}")
                is UiState.Loading -> {}
                is UiState.Success -> {

                    val status = it.data.first.questType
                    when (status) {
                        "해금" -> {
                            val itemList = it.data.first.itemImageUrls

                            val dialog = showDoneQuestDialog(it.data.second)
                            dialog.setOnDismissListener {
                                if (GlobalApplication.userLevel == 29) {
                                    val levelMaxDialog = showLevelMaxDialog()
                                    levelMaxDialog.setOnDismissListener {
                                        if (todayGetExp == 10) {
                                            val d = showDoneAllQuestDialog()
                                            d.setOnDismissListener {
                                                updateUI()
                                            }
                                        }
                                    }
                                } else {
                                    val levelUpDialog = showLevelUpUnlockDialog(
                                        GlobalApplication.userLevel + 1,
                                        itemList
                                    )
                                    levelUpDialog.setOnDismissListener {
                                        if (todayGetExp == 10) {
                                            val d = showDoneAllQuestDialog()
                                            d.setOnDismissListener {
                                                updateUI()
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        "레벨업" -> {
                            val dialog = showDoneQuestDialog(it.data.second)
                            dialog.setOnDismissListener {
                                val levelUpDialog =
                                    showLevelUpDialog(GlobalApplication.userLevel + 1)
                                levelUpDialog.setOnDismissListener {
                                    if (todayGetExp == 10) {
                                        val d = showDoneAllQuestDialog()
                                        d.setOnDismissListener {
                                            updateUI()
                                        }
                                    }
                                }
                            }
                        }

                        else -> {
                            // 그냥 퀘스트 완료일 때
                            val dialog = showDoneQuestDialog(it.data.second)
                            dialog.setOnDismissListener {
                                if (todayGetExp == 10) {
                                    val d = showDoneAllQuestDialog()
                                    d.setOnDismissListener {
                                        updateUI()
                                    }
                                }
                            }
                        }
                    }

                    isQuestDone = true
                    updateUI()
                }
            }
        }

        viewModel.deleteState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> LoggerUtils.e("Delete Quest 실패: ${it.error}")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    LoggerUtils.d(it.data.msg)
                    updateUI()
                }
            }
        }
    }

    private fun showExpProgress(ratio: Int) {
        val parentLayout = binding.ivExpBar
        parentLayout.removeAllViews() // 이전에 추가된 모든 뷰 제거

        val linearLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        for (i in 1..ratio) {
            val imageView = ImageView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(18.dpToPx(), 9.dpToPx()).apply {
                    if (i == 1) {
                        marginStart = 6.dpToPx()
                    }
                }
                setImageResource(R.drawable.img_exp_one)
            }
            linearLayout.addView(imageView)
        }
        parentLayout.addView(linearLayout)
    }

    // dp를 px로 변환하는 함수
    private fun Int.dpToPx(): Int {
        val density = resources.displayMetrics.density
        return (this * density).toInt()
    }

    @SuppressLint("SetTextI18n")
    private fun showAddQuestDialog(todayExp: Int) {
        val dialog = Dialog(requireContext())
        dialog.setCancelable(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = DialogAddQuestBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.etQuestDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.length > 15) {
                        binding.etQuestDesc.setText(it.substring(0, 15))
                        binding.etQuestDesc.setSelection(15)
                        Toast.makeText(binding.root.context, "15자를 넘을 수 없어요!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })

        var newExp = 1
        binding.tvExp.text = "EXP 1"

        binding.ivExpUp.setOnClickListener {
            if (newExp + todayExp > 9) {
                Toast.makeText(requireContext(), "하루에 얻을 수 있는 경험치는 최대 10입니다", Toast.LENGTH_SHORT)
                    .show()
            } else {
                newExp += 1
                binding.tvExp.text = "EXP $newExp"
            }
        }

        binding.ivExpDown.setOnClickListener {
            if (newExp > 1) {
                newExp -= 1
                binding.tvExp.text = "EXP $newExp"
            }
        }

        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnOk.setOnClickListener {
            val newQuestDesc = binding.etQuestDesc.text.toString().trim()

            if (newQuestDesc.isEmpty()) {
                Toast.makeText(requireContext(), "퀘스트를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.addQuest(newQuestDesc, newExp)
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun showModifyQuestDialog(position: Int) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = DialogModifyQuestBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.etQuestDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.length > 15) {
                        binding.etQuestDesc.setText(it.substring(0, 15))
                        binding.etQuestDesc.setSelection(15)
                        Toast.makeText(binding.root.context, "15자를 넘을 수 없어요!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })

        binding.etQuestDesc.setText(questList[position].contents)
        binding.tvExpText.text = "EXP ${questList[position].exp}"

        binding.btnModify.setOnClickListener {
            val newQuestDesc = binding.etQuestDesc.text.toString().trim()

            if (newQuestDesc.isEmpty()) {
                Toast.makeText(requireContext(), "퀘스트를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.updateQuest(questList[position].id, newQuestDesc)
            }
            dialog.dismiss()
        }

        binding.btnDelete.setOnClickListener {
            viewModel.deleteQuest(questList[position].id)
            dialog.dismiss()
        }
        dialog.show()

    }

    @SuppressLint("SetTextI18n")
    private fun showDoneQuestDialog(position: Int): Dialog {
        val dialog = Dialog(requireContext())
        dialog.setCancelable(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = DialogDoneQuestBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.tvDoneExp.text = "EXP ${questList[position].exp}"

        binding.btnGet.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

        return dialog
    }

    @SuppressLint("SetTextI18n")
    private fun showDoneAllQuestDialog(): Dialog {
        val dialog = Dialog(requireContext())
        dialog.setCancelable(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        isQuestDone = false
        isQuestAllDone = true

        val binding = DialogDoneAllQuestBinding.inflate(LayoutInflater.from(requireContext()))
        binding.tvNickname.text = "모험가 ${GlobalApplication.nickname}님!"
        binding.btnOk.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        return dialog
    }

    @SuppressLint("SetTextI18n")
    private fun showLevelUpDialog(myLevel: Int): Dialog {
        val dialog = Dialog(requireContext())
        dialog.setCancelable(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = DialogLevelupBinding.inflate(LayoutInflater.from(requireContext()))
        binding.tvRequireExpText.text = "LV.${myLevel}까지 필요한 EXP"
        binding.tvRequireExp.text = "${(myLevel - 1) * 10}"
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.btnOk.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

        return dialog
    }

    @SuppressLint("SetTextI18n")
    private fun showLevelUpUnlockDialog(myLevel: Int, itemList: List<String>): Dialog {
        val dialog = Dialog(requireContext())
        dialog.setCancelable(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = DialogLevelupUnlockBinding.inflate(LayoutInflater.from(requireContext()))
        binding.tvRequireExpText.text = "LV.${myLevel}까지 필요한 EXP"
        binding.tvRequireExp.text = "${(myLevel - 1) * 10}"
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.ivUnlockMore.visibility = if (itemList.size > 3) View.GONE else View.VISIBLE

        val imageViews = listOf(binding.ivUnlock1, binding.ivUnlock2, binding.ivUnlock3)

        for (i in imageViews.indices) {
            if (i < itemList.size) {
                Glide.with(binding.root.context)
                    .load(itemList[i])
                    .override(79, 79)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .into(imageViews[i])
            }
        }

        binding.btnOk.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

        return dialog
    }

    @SuppressLint("SetTextI18n")
    private fun showLevelMaxDialog(): Dialog {
        val dialog = Dialog(requireContext())
        dialog.setCancelable(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = DialogLevelMaxBinding.inflate(LayoutInflater.from(requireContext()))
        binding.tvNickname.text = "${GlobalApplication.nickname}님의\n모든 성장을 마쳤어요!"
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.btnOk.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

        return dialog
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateUI() {
        if (isQuestAllDone) {
            Glide.with(requireContext())
                .asGif()
                .load(R.raw.gif_done_all_quest)
                .into(object : CustomTarget<GifDrawable>() {
                    override fun onResourceReady(
                        resource: GifDrawable,
                        transition: Transition<in GifDrawable>?,
                    ) {
                        // GIF 3번 반복 설정
                        resource.setLoopCount(3)
                        binding.ivDoneQuestGif.setImageDrawable(resource)
                        resource.start()
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // 리소스를 해제할 때 실행될 코드 (필요하면 추가)
                    }
                })
        } else if (isQuestDone) {
            Glide.with(requireContext())
                .asGif()
                .load(R.raw.gif_done_quest)
                .into(object : CustomTarget<GifDrawable>() {
                    override fun onResourceReady(
                        resource: GifDrawable,
                        transition: Transition<in GifDrawable>?,
                    ) {
                        // GIF 3번 반복 설정
                        resource.setLoopCount(3)
                        binding.ivDoneQuestGif.setImageDrawable(resource)
                        resource.start()
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // 리소스를 해제할 때 실행될 코드 (필요하면 추가)
                    }
                })
        }

        viewModel.fetchExpInfo()
        viewModel.fetchQuestInfo(today)
        questRvAdapter.notifyDataSetChanged()
    }

    private fun loadImage(view: ImageView, itemImage: String, widthDp: Int, heightDp: Int) {
        Glide.with(view.context)
            .load(itemImage)
            .override(widthDp.dpToPx(), heightDp.dpToPx())
            .skipMemoryCache(true)
            .dontAnimate()
            .into(view)
    }

    private fun handleItem(item: ItemData) {
        val (view, widthDp, heightDp) = when (item.itemType) {
            "FACECOLOR" -> Triple(binding.ivCharacter, 105, 216)
            "EYE" -> Triple(binding.ivFace, 75, 33)
            "CLOTHES" -> Triple(binding.ivCloth, 69, 117)
            "HAIR" -> Triple(binding.ivHair, 123, 159)
            "BACKGROUND" -> Triple(binding.ivBackground, 300, 300)
            "GLASSES" -> Triple(binding.ivGlasses, 75, 39)
            "HEAD" -> Triple(binding.ivHat, 123, 81)
            // 다른 itemType 추가 가능
            else -> return
        }

        loadImage(view, item.itemImage, widthDp, heightDp)
    }
}