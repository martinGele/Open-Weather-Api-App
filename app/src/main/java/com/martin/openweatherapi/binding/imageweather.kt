package com.martin.openweatherapi.binding

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.martin.openweatherapi.R
import com.martin.openweatherapi.vo.WeatherX


@BindingAdapter("android:imageUrl")
fun loadImage(view: ImageView, uri: List<WeatherX>?) {
    view.loadImage(uri, getProgressDrawable(view.context))

}


@SuppressLint("NewApi")
fun getProgressDrawable(contex: Context): CircularProgressDrawable {

    return CircularProgressDrawable(contex).apply {
        strokeWidth = 6f
        centerRadius = 50f
        backgroundColor= contex.getColor(R.color.colorAccent)
        start()
    }
}

fun ImageView.loadImage(image: List<WeatherX>?, progressDrawable: CircularProgressDrawable) {


    var iconWeather: String = ""
    image?.forEach {
        iconWeather = it.icon
    }

//    val options = RequestOptions()
//        .placeholder(progressDrawable)
//        .error(R.drawable.ic_insert_photo)


    Glide.with(context)
//        .setDefaultRequestOptions(options)
        .load("http://openweathermap.org/img/wn/$iconWeather@2x.png")
        .into(this)

}