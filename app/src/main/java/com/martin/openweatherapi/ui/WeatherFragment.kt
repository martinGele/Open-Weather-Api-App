package com.martin.openweatherapi.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.martin.openweatherapi.R
import com.martin.openweatherapi.databinding.WeatherFragmentBinding
import com.martin.openweatherapi.di.Injectable
import com.martin.openweatherapi.util.isConnected
import kotlinx.android.synthetic.main.weather_fragment.*
import javax.inject.Inject

class WeatherFragment : Fragment(), Injectable {


    lateinit var dataBinding: WeatherFragmentBinding


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    val weatherViewModel: WeatherViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.weather_fragment,
                container,
                false
            )
        return dataBinding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBinding.lifecycleOwner = this
        dataBinding.viewModel = weatherViewModel


        search_view.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(city: String?): Boolean {

                if (context!!.isConnected) {
                    weatherViewModel.showDataByLocation(city)
                    progressBar.visibility = View.VISIBLE
                } else {
                    noInternet.visibility = View.VISIBLE
                }

                return true
            }

            override fun onQueryTextChange(city: String?): Boolean {

                return true
            }

        })



        weatherViewModel.loadError.observe(this, onErrorLiveDataObserver)

        weatherViewModel.loading.observe(this, loadingLiveDataObserver)

    }

    private val onErrorLiveDataObserver = Observer<Boolean> { it ->


        no_city.visibility = if (it) View.VISIBLE else View.GONE


    }
    private val loadingLiveDataObserver = Observer<Boolean> { isLoading ->
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

    }
}
