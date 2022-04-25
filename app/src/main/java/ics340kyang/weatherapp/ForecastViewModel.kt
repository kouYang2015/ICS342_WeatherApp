package ics340kyang.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ForecastViewModel @Inject constructor(private val service: Api) : ViewModel() {

    private val _forecastList = MutableLiveData<ForecastList>()
    val forecastList: LiveData<ForecastList>
        get() = _forecastList

    fun loadData(locInput: Coordinates) = runBlocking {
        launch { _forecastList.value = service.getForecastLatLon(locInput.latitude.toString(),locInput.longitude.toString()) }
    }
}