package com.martin.openweatherapi.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.martin.openweatherapi.api.WeatherApiService
import com.martin.openweatherapi.util.API_KEY
import com.martin.openweatherapi.vo.Weather
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WeatherViewModel @Inject constructor(private val weatherApiService: WeatherApiService) :
    ViewModel() {


    val weatherLiveData by lazy { MutableLiveData<Weather>() }
    val loadError by lazy { MutableLiveData<Boolean>() }
    val loading by lazy { MutableLiveData<Boolean>() }

    private val disposable = CompositeDisposable()


    fun showDataByLocation(location:String?) {
        loading.value = true


        getWeatherByLocation(location)
    }


    fun getWeatherByLocation(location:String?) {
        disposable.add(
            weatherApiService.getWeatherCity(location!!, API_KEY)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Weather>() {
                    override fun onSuccess(weather: Weather) {

                        Log.d("getData", weather.toString())
                        loadError.value = false
                        weatherLiveData.value = weather
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        loading.value = false
                        loadError.value = true
                        weatherLiveData.value = null
                    }

                })


        )
    }


    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
