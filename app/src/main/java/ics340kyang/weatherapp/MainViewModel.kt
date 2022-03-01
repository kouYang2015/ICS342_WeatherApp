package ics340kyang.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainViewModel @Inject constructor(private val api: Api) : ViewModel() {

    private val _currentConditions = MutableLiveData<CurrentConditions>()
    val currentConditions: LiveData<CurrentConditions>
        get() = _currentConditions

    fun loadData() {
        val call = api.getCurrentConditions("55429")
        call.enqueue(object : Callback<CurrentConditions> {
            override fun onResponse(
                call: Call<CurrentConditions>,
                response: Response<CurrentConditions>
            ) {
                _currentConditions.value = response.body()
            }

            override fun onFailure(call: Call<CurrentConditions>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}