package com.example.myapplication.ui.fragment

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.data.viewmodel.AuthVM
import com.example.myapplication.data.viewmodel.SettingVM
import com.example.myapplication.data.viewmodel.VMGeneralFactory
import com.example.myapplication.data.viewmodel.VMSettingFactory
import com.example.myapplication.databinding.FragmentLoginBinding
import com.example.myapplication.ui.activity.AuthActivity
import com.example.myapplication.utility.Constanta
import com.example.myapplication.utility.Helper
import com.example.myapplication.utility.SettingPreferences
import com.example.myapplication.utility.dataStore



class LoginFragment : Fragment() {
    companion object {
        fun newInstance() = LoginFragment()
    }

    private var viewModel: AuthVM? = null
    private lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animation = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = SettingPreferences.getInstance((activity as AuthActivity).dataStore)
        val settingViewModel =
            ViewModelProvider(this, VMSettingFactory(pref))[SettingVM::class.java]
        viewModel = ViewModelProvider(
            this,
            VMGeneralFactory((activity as AuthActivity))
        )[AuthVM::class.java]
        viewModel?.let { vm ->
            vm.loginResult.observe(viewLifecycleOwner) { login ->
                // success login process triggered -> save preferences
                settingViewModel.setUserPreferences(
                    login.loginResult.token,
                    login.loginResult.userId,
                    login.loginResult.name,
                    viewModel!!.tempEmail.value ?: Constanta.preferenceDefaultValue
                )
            }
            vm.error.observe(viewLifecycleOwner) { error ->
                if (error.isNotEmpty()) {
                    Helper.showDialogInfo(requireContext(), error)
                }
            }
            vm.loading.observe(viewLifecycleOwner) { state ->
                binding.loading.root.visibility = state
            }
        }
        settingViewModel.getUserPreferences(Constanta.UserPreferences.UserToken.name)
            .observe(viewLifecycleOwner) { token ->
                if (token != Constanta.preferenceDefaultValue) (activity as AuthActivity).routeToMainActivity()
            }
        binding.btnAction.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            when {
                email.isEmpty() or password.isEmpty() -> {
                    Helper.showDialogInfo(
                        requireContext(),
                        getString(R.string.empty_email_password)
                    )
                }
                !email.matches(Constanta.emailPattern) -> {
                    Helper.showDialogInfo(
                        requireContext(),
                        getString(R.string.validation_invalid_email)
                    )
                }
                password.length <= 7 -> {
                    Helper.showDialogInfo(
                        requireContext(),
                        getString(R.string.validation_password_rules)
                    )
                }
                else -> {
                    viewModel?.login(email, password)
                }
            }
        }
        binding.btnRegister.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.container, RegisterFragment(), RegisterFragment::class.java.simpleName)
                addSharedElement(binding.labelAuth, "auth")
                addSharedElement(binding.edLoginEmail, "email")
                addSharedElement(binding.edLoginPassword, "password")
                addSharedElement(binding.containerMisc, "misc")
                commit()
            }
        }
    }

}