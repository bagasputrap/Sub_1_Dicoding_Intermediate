package com.example.myapplication.ui.activity

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.data.viewmodel.SettingVM
import com.example.myapplication.data.viewmodel.StoryVM
import com.example.myapplication.data.viewmodel.VMGeneralFactory
import com.example.myapplication.data.viewmodel.VMSettingFactory
import com.example.myapplication.databinding.ActivityAddStoryBinding
import com.example.myapplication.utility.Constanta
import com.example.myapplication.utility.Helper
import com.example.myapplication.utility.SettingPreferences
import com.example.myapplication.utility.dataStore
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private var userToken: String? = null
    private var storyViewModel: StoryVM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyViewModel = ViewModelProvider(
            this,
            VMGeneralFactory(this)
        )[StoryVM::class.java]

        /* Get Token from preference */
        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel =
            ViewModelProvider(this, VMSettingFactory(pref))[SettingVM::class.java]
        settingViewModel.getUserPreferences(Constanta.UserPreferences.UserToken.name)
            .observe(this) { token ->
                userToken = StringBuilder("Bearer ").append(token).toString()
            }



        val myFile = intent?.getSerializableExtra(EXTRA_PHOTO_RESULT) as File
        val bitmap = BitmapFactory.decodeFile((myFile.path))
        binding.storyImage.setImageBitmap(bitmap)
        binding.btnAdd.setOnClickListener {
            if (binding.edAddDescription.text.isNotEmpty()) {
                uploadImage(myFile, binding.edAddDescription.text.toString())
            } else {
                Helper.showDialogInfo(
                    this,
                    getString(R.string.empty_story_description)
                )
            }
        }
        storyViewModel?.let { vm ->
            vm.isSuccessUploadStory.observe(this) {
                if (it) {
                    val dialog = Helper.dialogInfoBuilder(
                        this,
                        getString(R.string.success_upload)
                    )
                    val btnOk = dialog.findViewById<Button>(R.id.button_ok)
                    btnOk.setOnClickListener {
                        finish()
                    }
                    dialog.show()
                }

            }
            vm.loading.observe(this) {
                binding.loading.root.visibility = it
            }
            vm.error.observe(this) {
                if (it.isNotEmpty()) {
                    Helper.showDialogInfo(this, it)
                }
            }
        }
    }

    private fun uploadImage(image: File, description: String) {
        if (userToken != null) {
            storyViewModel?.uploadNewStory(userToken!!, image, description)
        } else {
            Helper.showDialogInfo(
                this,
                getString(R.string.error_header_token)
            )
        }
    }

    companion object {
        const val EXTRA_PHOTO_RESULT = "PHOTO_RESULT"
        const val EXTRA_CAMERA_MODE = "CAMERA_MODE"
    }
}