package com.martin.openweatherapi.di


import com.martin.openweatherapi.api.WeatherApiService
import com.martin.openweatherapi.util.API_KEY
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideWeatherService(): WeatherApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }

//    @Singleton
//    @Provides
//    fun provideInterceptor(): Interceptor = Interceptor { chain ->
//        val request = chain.request()
//        val url = request.url().newBuilder()
//            .addQueryParameter("appid", API_KEY)
//            .build()
//        val newRequest = request.newBuilder()
//            .url(url)
//            .build()
//        chain.proceed(newRequest)
//    }


}
