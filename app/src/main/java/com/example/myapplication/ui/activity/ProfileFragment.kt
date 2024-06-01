package com.example.myapplication.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.viewmodel.SettingVM
import com.example.myapplication.data.viewmodel.VMSettingFactory
import com.example.myapplication.databinding.FragmentProfileBinding
import com.example.myapplication.utility.Constanta
import com.example.myapplication.utility.SettingPreferences
import com.example.myapplication.utility.dataStore



class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = SettingPreferences.getInstance((activity as MainActivity).dataStore)
        val settingViewModel =
            ViewModelProvider(this, VMSettingFactory(pref))[SettingVM::class.java]
        settingViewModel.getUserPreferences(Constanta.UserPreferences.UserName.name)
            .observe(viewLifecycleOwner) {
                binding.textName.text = it
            }

        settingViewModel.getUserPreferences(Constanta.UserPreferences.UserEmail.name)
            .observe(viewLifecycleOwner) {
                binding.textEmail.text = it
            }
        settingViewModel.getUserPreferences(Constanta.UserPreferences.UserToken.name)
            .observe(viewLifecycleOwner) {
                if (it == Constanta.preferenceDefaultValue) {
                    (activity as MainActivity).routeToAuth()
                }
            }
        binding.btnLogout.setOnClickListener {
            settingViewModel.clearUserPreferences()
        }
        binding.btnSetPermission.setOnClickListener {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri: Uri =
                Uri.fromParts("package", (activity as MainActivity).packageName, null)
            intent.data = uri
            (activity as MainActivity).startActivity(intent)
        }
        binding.btnSetLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }
}