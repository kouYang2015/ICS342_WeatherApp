package ics340kyang.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class ForecastActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    private lateinit var api: Api

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https:api.openweathermap.org/data/2.5/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        api = retrofit.create(Api::class.java)

        // Creates back button next to activity title
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        val call: Call<ForecastList> = api.getForecast("55429")
        call.enqueue(object : Callback<ForecastList> {
            override fun onResponse(
                call: Call<ForecastList>,
                response: Response<ForecastList>
            ) {
                val forecastList = response.body()
                forecastList?.let {
                    recyclerView.adapter = MyAdapter(it.list)
                }
            }

            override fun onFailure(call: Call<ForecastList>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}