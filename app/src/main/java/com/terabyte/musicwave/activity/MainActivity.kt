package com.terabyte.musicwave.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.terabyte.musicwave.R
import com.terabyte.musicwave.application.MyApplication
import com.terabyte.musicwave.databinding.ActivityMainBinding
import com.terabyte.musicwave.di.MusicComponent
import com.terabyte.musicwave.ui.TabAdapter
import com.terabyte.musicwave.viewmodel.MainViewModel
import javax.inject.Inject

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    lateinit var musicComponent: MusicComponent

    @Inject
    lateinit var tabAdapter: TabAdapter

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        musicComponent = (application as MyApplication).appComponent.musicComponent()
            .create(this, supportFragmentManager, lifecycle)
        musicComponent.inject(this)

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureTabLayout()
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
}