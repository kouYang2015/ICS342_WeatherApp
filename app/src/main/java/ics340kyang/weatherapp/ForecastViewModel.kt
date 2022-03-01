package ics340kyang.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ForecastViewModel @Inject constructor(private val api: Api) : ViewModel() {

    private val _forecastList = MutableLiveData<ForecastList>()
    val forecastList: LiveData<ForecastList>
        get() = _forecastList

    fun loadData(){
        val call: Call<ForecastList> = api.getForecast("55429")
        call.enqueue(object : Callback<ForecastList> {
            override fun onResponse(
                call: Call<ForecastList>,
                response: Response<ForecastList>
            ) {
                _forecastList.value = response.body()
            }

            override fun onFailure(call: Call<ForecastList>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }


}