package com.terabyte.musicwave.activity

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.terabyte.musicwave.R
import com.terabyte.musicwave.application.MyApplication
import com.terabyte.musicwave.databinding.ActivityMainBinding
import com.terabyte.musicwave.di.MusicComponent
import com.terabyte.musicwave.ui.TabAdapter
import com.terabyte.musicwave.viewmodel.MainViewModel
import javax.inject.Inject
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_AUDIO
import android.graphics.BitmapFactory
import android.util.Log
import android.util.TypedValue
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.terabyte.musicwave.LOG_TAG_DEBUG
import com.terabyte.musicwave.databinding.ActivityRequirePermissionsBinding
import java.io.FileNotFoundException

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    lateinit var musicComponent: MusicComponent

    @Inject
    lateinit var tabAdapter: TabAdapter

    @Inject
    lateinit var viewModel: MainViewModel

    private val allPermissionsGrantedListener = {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //it is important to set observer to liveData before calling the method, otherwise
        //the method sets liveData before we observe it
        viewModel.liveDataAllMusicModels.observe(this) {
            configureTabLayout()
        }
        viewModel.getAllMusicModelsIfNecessary(this)

        viewModel.liveDataChosenMusicModel.observe(this) { chosenMusicModel ->
            binding.textPlayingSongName.text = chosenMusicModel.title
            binding.textPlayingSongArtist.text = chosenMusicModel.author

            binding.imagePlayingSong.setImageURI(chosenMusicModel.imageUri)
            if (binding.imagePlayingSong.drawable==null) {
                binding.imagePlayingSong.setImageResource(R.drawable.song_icon_default)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        musicComponent = (application as MyApplication).appComponent.musicComponent()
            .create(this, supportFragmentManager, lifecycle)
        musicComponent.inject(this)

        super.onCreate(savedInstanceState)

        configureStatusBarColor()

        if(checkPermissions()) {
            allPermissionsGrantedListener()
        }
        else {
            val bindingRequirePermissions = ActivityRequirePermissionsBinding.inflate(layoutInflater)
            setContentView(bindingRequirePermissions.root)
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            allPermissionsGrantedListener()
        }
        else {
            Toast.makeText(applicationContext, R.string.unable_to_work_without_permissions, Toast.LENGTH_LONG).show()
        }
    }

    private fun checkPermissions(): Boolean {
        return if(Build.VERSION.SDK_INT>32) {
            ContextCompat.checkSelfPermission(this, READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
        }
        else {
            ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }

    //this fun needs invoking only after checkPermissions() fun's call above
    private fun requestPermissions() {
        if(Build.VERSION.SDK_INT>32) {
            ActivityCompat.requestPermissions(this, arrayOf(READ_MEDIA_AUDIO), PERMISSION_READ_MEDIA_AUDIO_CODE)
        }
        else {
            ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE), PERMISSION_READ_EXTERNAL_STORAGE_CODE)
        }

    }

    private fun configureTabLayout() {
        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        binding.viewPager.adapter = tabAdapter
        binding.viewPager.isUserInputEnabled = true
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when(position) {
                0 -> {
                    getString(R.string.tab_all_songs)
                }
                1 -> {
                    getString(R.string.tab_playlists)
                }
                else -> getString(R.string.tab_all_songs)
            }
        }.attach()
    }

    private fun configureStatusBarColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.color_status_bar, typedValue, true)
        window.statusBarColor = typedValue.data
    }

    companion object {
        const val PERMISSION_READ_MEDIA_AUDIO_CODE = 0
        const val PERMISSION_READ_EXTERNAL_STORAGE_CODE = 1
    }
}