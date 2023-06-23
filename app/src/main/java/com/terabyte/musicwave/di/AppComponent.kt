package com.terabyte.musicwave.di

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.terabyte.musicwave.activity.MainActivity
import dagger.Binds
import dagger.BindsInstance
import dagger.Component

@Component(modules = [AppSubcomponents::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(): AppComponent
    }

    fun musicComponent(): MusicComponent.Factory
}