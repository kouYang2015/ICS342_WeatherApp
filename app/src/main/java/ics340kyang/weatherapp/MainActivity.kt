package ics340kyang.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var button: Button
    private val apiKey = "bb04a900b609b9b072402a95ab92a33c"

    private lateinit var api: Api
    private lateinit var cityName: TextView
    private lateinit var currentTemp: TextView
    private lateinit var feelsLike: TextView
    private lateinit var currentLow: TextView
    private lateinit var currentHigh: TextView
    private lateinit var currentHumidity: TextView
    private lateinit var currentPressure: TextView
    private lateinit var conditionIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cityName = findViewById(R.id.city_name)
        currentTemp = findViewById(R.id.temp)
        feelsLike = findViewById(R.id.feels_like)
        currentLow = findViewById(R.id.low)
        currentHigh = findViewById(R.id.high)
        currentHumidity = findViewById(R.id.humidity)
        currentPressure = findViewById(R.id.pressure)
        conditionIcon = findViewById(R.id.condition_icon)

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https:api.openweathermap.org/data/2.5/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        api = retrofit.create(Api::class.java)

        button = findViewById<Button>(R.id.forecast_button)

        // operations to be performed when user tap on the button
        button?.setOnClickListener() {
            val intent = Intent(this, ForecastActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val call: Call<CurrentConditions> = api.getCurrentConditions("55429")
        call.enqueue(object : Callback<CurrentConditions> {
            override fun onResponse(
                call: Call<CurrentConditions>,
                response: Response<CurrentConditions>
            ) {
                val currentCondition = response.body()
                currentCondition?.let {
                    bindData(it)
                }
            }

            override fun onFailure(call: Call<CurrentConditions>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    //Binds data from the response object
    private fun bindData(currentConditions: CurrentConditions) {
        cityName.text = currentConditions.name
        currentTemp.text = getString(R.string.temp, currentConditions.main.temp.toInt())
        feelsLike.text = getString(R.string.feels_like, currentConditions.main.feelsLike.toInt())
        currentLow.text = getString(R.string.low, currentConditions.main.tempMin.toInt())
        currentHigh.text = getString(R.string.high, currentConditions.main.tempMax.toInt())
        currentHumidity.text = getString(R.string.humidity, currentConditions.main.humidity.toInt())
        currentPressure.text =
            getString(R.string.pressure, currentConditions.main.feelsLike.toInt())
        val iconName = currentConditions.weather.firstOrNull()?.icon
        val iconUrl = "https://openweathermap.org/img/wn/${iconName}@2x.png"
        Glide.with(this)
            .load(iconUrl)
            .into(conditionIcon)
    }

}