package ics340kyang.weatherapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainViewModel: ViewModel() {

    val currentConditions: MutableLiveData<CurrentConditions> = MutableLiveData()

    fun loadData(){
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https:api.openweathermap.org/data/2.5/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        var api = retrofit.create(Api::class.java)

        val call = api.getCurrentConditions("55429")
        call.enqueue(object : Callback<CurrentConditions> {
            override fun onResponse(
                call: Call<CurrentConditions>,
                response: Response<CurrentConditions>
            ) {
                val currentCondition = response.body()
                currentCondition?.let {
                    currentConditions.value = it
                }
            }

            override fun onFailure(call: Call<CurrentConditions>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}