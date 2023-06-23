package com.terabyte.musicwave.di

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.terabyte.musicwave.activity.MainActivity
import com.terabyte.musicwave.fragment.AllSongsFragment
import com.terabyte.musicwave.fragment.PlaylistsFragment
import dagger.BindsInstance
import dagger.Subcomponent

@MusicScope
@Subcomponent(modules = [MusicModule::class])
interface MusicComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance mainActivity: MainActivity, @BindsInstance fragmentManager: FragmentManager, @BindsInstance lifecycle: Lifecycle): MusicComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: AllSongsFragment)
    fun inject(fragment: PlaylistsFragment)
}