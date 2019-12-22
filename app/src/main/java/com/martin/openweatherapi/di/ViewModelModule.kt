package com.martin.openweatherapi.di


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.martin.openweatherapi.ui.WeatherViewModel
import com.martin.openweatherapi.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(WeatherViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: WeatherViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}
