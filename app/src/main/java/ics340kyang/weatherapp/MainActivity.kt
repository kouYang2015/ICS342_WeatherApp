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
import dagger.hilt.android.AndroidEntryPoint
import ics340kyang.weatherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val apiKey = "bb04a900b609b9b072402a95ab92a33c"
    @Inject lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.forecastButton.setOnClickListener() {
            startActivity(Intent(this, ForecastActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.currentConditions.observe(this) {currentConditions ->
            bindData(currentConditions)
        }
        viewModel.loadData()
    }

    //Binds data from the response object
    private fun bindData(currentConditions: CurrentConditions) {
        binding.cityName.text = currentConditions.name
        binding.temp.text = getString(R.string.temp, currentConditions.main.temp.toInt())
        binding.feelsLike.text = getString(R.string.feels_like, currentConditions.main.feelsLike.toInt())
        binding.low.text = getString(R.string.low, currentConditions.main.tempMin.toInt())
        binding.high.text = getString(R.string.high, currentConditions.main.tempMax.toInt())
        binding.humidity.text = getString(R.string.humidity, currentConditions.main.humidity.toInt())
        binding.pressure.text = getString(R.string.pressure, currentConditions.main.feelsLike.toInt())
        val iconName = currentConditions.weather.firstOrNull()?.icon
        val iconUrl = "https://openweathermap.org/img/wn/${iconName}@2x.png"
        Glide.with(this)
            .load(iconUrl)
            .into(binding.conditionIcon)
    }

}