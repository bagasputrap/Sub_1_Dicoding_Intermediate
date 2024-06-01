package com.example.myapplication.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.viewmodel.SettingVM
import com.example.myapplication.data.viewmodel.VMSettingFactory
import com.example.myapplication.databinding.ActivitySplashScreenBinding
import com.example.myapplication.utility.Constanta
import com.example.myapplication.utility.SettingPreferences
import com.example.myapplication.utility.dataStore
import java.util.*
import kotlin.concurrent.schedule


class  SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel =
            ViewModelProvider(this, VMSettingFactory(pref))[SettingVM::class.java]

        settingViewModel.getUserPreferences(Constanta.UserPreferences.UserToken.name)
            .observe(this) { token ->
                if (token == Constanta.preferenceDefaultValue) Timer().schedule(3000) {
                    startActivity(Intent(this@SplashScreenActivity, AuthActivity::class.java))
                    finish()
                } else Timer().schedule(3000) {
                    startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                    finish()
                }
            }
    }
}