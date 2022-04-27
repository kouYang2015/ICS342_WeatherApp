package ics340kyang.weatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ServiceViewModel @Inject constructor(private val service: Api) : ViewModel() {
    private val _currentConditionCall = MutableLiveData<CurrentConditions>()
    private val _latitude = MutableLiveData<String>()
    private val _longitude = MutableLiveData<String>()

    val currentConditionCall: LiveData<CurrentConditions>
        get() = _currentConditionCall
    val latitude: LiveData<String>
        get() = _latitude
    val longitude: LiveData<String>
        get() = _longitude

    fun submitLocationButton() = runBlocking {
        launch {
            try {
                _currentConditionCall.value =
                    latitude.value?.let { lat ->
                        longitude.value?.let { long ->
                            service.getCurrentConditionsLatLon(lat, long)
                        }
                    }
                Log.d("WeatherMapAPI", "Call success in ServiceViewModel")
            } catch (e: Throwable) {
                Log.d("WeatherMapAPI", "Call failed with lat lon")
            }
        }
    }

    fun updateLocation(latIn: Double, longIn: Double) {
        _latitude.value = latIn.toString()
        _longitude.value = longIn.toString()
    }
}