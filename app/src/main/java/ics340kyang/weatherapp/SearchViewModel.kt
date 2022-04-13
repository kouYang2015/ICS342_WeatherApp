package ics340kyang.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val service: Api) : ViewModel() {

    private val _enableButton = MutableLiveData(false)
    private val _zipCodeText = MutableLiveData<String>()
    private val _currentConditionCall = MutableLiveData<CurrentConditions>()
    private val _showErrorDialog = MutableLiveData(false)
    private val _latitude = MutableLiveData<String>()
    private val _longitude = MutableLiveData<String>()
    val showErrorDialog: LiveData<Boolean>
        get() = _showErrorDialog
    val enableButton: LiveData<Boolean>
        get() = _enableButton
    val zipCodeText: LiveData<String>
        get() = _zipCodeText
    val currentConditionCall: LiveData<CurrentConditions>
        get() = _currentConditionCall
    val latitude: LiveData<String>
        get() = _latitude
    val longitude: LiveData<String>
        get() = _longitude

    fun submitZipButton() = runBlocking {
        launch {
            try {
                _currentConditionCall.value =
                    zipCodeText.value?.let { service.getCurrentConditionsZip(it) }
            } catch (e: Throwable) {
                _showErrorDialog.value = true
            } finally {
                _showErrorDialog.value = false
            }
        }
    }

    fun submitLocationButton() = runBlocking {
        launch {
            try {
                _currentConditionCall.value =
                    latitude.value?.let { lat ->
                        longitude.value?.let { long ->
                            service.getCurrentConditionsLatLon(lat, long)
                        }
                    }
            } catch (e: Throwable) {
                _showErrorDialog.value = true
            } finally {
                _showErrorDialog.value = false
            }
        }
    }

    fun updateLocation(latIn: Double, longIn: Double) {
        _latitude.value = latIn.toString()
        _longitude.value = longIn.toString()
    }

    fun updateZipCode(zipcode: String) {
        if (zipcode != _zipCodeText.value) {
            _zipCodeText.value = zipcode
            _enableButton.value = isValidZipCode(zipcode)
        }
    }

    private fun isValidZipCode(zipcode: String): Boolean {
        return zipcode.length == 5 && zipcode.all { it.isDigit() }
    }
}