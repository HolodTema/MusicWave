package com.terabyte.musicwave.di

import androidx.activity.viewModels
import com.terabyte.musicwave.activity.MainActivity
import com.terabyte.musicwave.viewmodel.MainViewModel
import dagger.Module
import dagger.Provides

@Module
class MusicModule {

    @Provides
    fun provideMainViewModel(mainActivity: MainActivity): MainViewModel {
        return mainActivity.viewModels<MainViewModel>().value
    }
}