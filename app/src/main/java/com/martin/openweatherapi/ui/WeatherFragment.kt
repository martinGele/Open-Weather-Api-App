package com.martin.openweatherapi.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
import com.martin.openweatherapi.R
import com.martin.openweatherapi.databinding.WeatherFragmentBinding
import com.martin.openweatherapi.di.Injectable
import com.martin.openweatherapi.util.checkPermissions
import com.martin.openweatherapi.util.isConnected
import com.martin.openweatherapi.util.isLocationEnabled
import com.martin.openweatherapi.util.requestPermissionsWeather
import kotlinx.android.synthetic.main.weather_fragment.*
import javax.inject.Inject

class WeatherFragment : Fragment(), Injectable {

    lateinit var dataBinding: WeatherFragmentBinding
    lateinit var mFusedLocationClient: FusedLocationProviderClient


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


        /**
         * init the location service client
         */
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        getLastLocation()
        /**
         * pass the value of the search query into the api call
         */
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
        /**
         * check if loading is started and if the loading of the data is finished
         */
        weatherViewModel.loadError.observe(this, onErrorLiveDataObserver)
        /**
         * check for loader if the get call is success
         */
        weatherViewModel.loading.observe(this, loadingLiveDataObserver)

    }

    /**
     * if there is no data for the searched city pass an error
     */
    private val onErrorLiveDataObserver = Observer<Boolean> { it ->
        if (context!!.isConnected) {
            no_city.visibility = if (it) View.VISIBLE else View.GONE
        }

    }
    /**
     * set the loader
     */
    private val loadingLiveDataObserver = Observer<Boolean> { isLoading ->
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

    }


    /**
     * request for a new location based on high accuracy
     */
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            if (context!!.isConnected) {
                weatherViewModel.showDataByGeoLocation(
                    mLastLocation.latitude.toString(),
                    mLastLocation.longitude.toString()
                )
                progressBar.visibility = View.VISIBLE
            } else {
                noInternet.visibility = View.VISIBLE
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions(requireActivity())) {
            if (isLocationEnabled(requireActivity())) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {

                        if (context!!.isConnected) {
                            weatherViewModel.showDataByGeoLocation(
                                location.latitude.toString(),
                                location.longitude.toString()
                            )
                            progressBar.visibility = View.VISIBLE
                        } else {
                            noInternet.visibility = View.VISIBLE
                        }
                    }
                }
            } else {
                Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissionsWeather(requireActivity())
        }
    }

    override fun onResume() {
        super.onResume()
        getLastLocation()
    }

}



