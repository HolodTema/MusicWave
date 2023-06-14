package com.terabyte.musicwave.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.terabyte.musicwave.R
import com.terabyte.musicwave.databinding.ActivityMainBinding
import com.terabyte.musicwave.ui.TabAdapter
import com.terabyte.musicwave.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setContentView(binding.root)

        configureTabLayout()
    }

    private fun configureTabLayout() {
        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = TabAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter
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