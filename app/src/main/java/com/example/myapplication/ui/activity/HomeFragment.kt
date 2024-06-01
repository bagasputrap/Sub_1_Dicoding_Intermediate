package com.example.myapplication.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.R
import com.example.myapplication.data.viewmodel.SettingVM
import com.example.myapplication.data.viewmodel.StoryVM
import com.example.myapplication.data.viewmodel.VMGeneralFactory
import com.example.myapplication.data.viewmodel.VMSettingFactory
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.utility.Constanta
import com.example.myapplication.utility.Helper
import com.example.myapplication.utility.SettingPreferences
import com.example.myapplication.utility.dataStore
import java.util.*
import kotlin.concurrent.schedule

class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var viewModel: StoryVM? = null
    private lateinit var binding: FragmentHomeBinding
    private val rvAdapter = HomeAdapter()
    private var tempToken = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            this,
            VMGeneralFactory((activity as MainActivity))
        )[StoryVM::class.java]
        val pref = SettingPreferences.getInstance((activity as MainActivity).dataStore)
        val settingViewModel =
            ViewModelProvider(this, VMSettingFactory(pref))[SettingVM::class.java]
        settingViewModel.getUserPreferences(Constanta.UserPreferences.UserToken.name)
            .observe(viewLifecycleOwner) { token ->
                tempToken = StringBuilder("Bearer ").append(token).toString()
                viewModel?.loadStoryData(tempToken)
            }
        /* Toolbar */
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_people_24)

        binding.btnJumpUp.visibility = View.GONE
        binding.swipeRefresh.setOnRefreshListener {
            onRefresh()
        }
        binding.rvStory.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
            adapter = rvAdapter
        }
        viewModel?.apply {
            loading.observe(viewLifecycleOwner) { binding.loading.root.visibility = it }
            error.observe(
                viewLifecycleOwner
            ) { if (it.isNotEmpty()) Helper.showDialogInfo(requireContext(), it) }
            storyList.observe(viewLifecycleOwner) {
                rvAdapter.apply {
                    initData(it)
                    notifyDataSetChanged()
                }
                binding.btnJumpUp.visibility = View.VISIBLE
            }
        }
        binding.btnJumpUp.setOnClickListener {
            binding.nestedScrollView.smoothScrollTo(0, 0)
        }
        return binding.root
    }

    override fun onRefresh() {
        binding.swipeRefresh.isRefreshing = true
        viewModel?.loadStoryData(tempToken)
        Timer().schedule(2000) {
            binding.swipeRefresh.isRefreshing = false
        }
        binding.nestedScrollView.smoothScrollTo(0, 0)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.activity_main_actionbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.swipeRefresh -> {
                onRefresh()
            }
        }
        return true
    }
}