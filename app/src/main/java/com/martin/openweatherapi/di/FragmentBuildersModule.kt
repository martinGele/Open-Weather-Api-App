package com.martin.openweatherapi.di


import com.martin.openweatherapi.ui.WeatherFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeMainFragment(): WeatherFragment


}
