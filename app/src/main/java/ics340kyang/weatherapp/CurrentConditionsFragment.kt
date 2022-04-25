package ics340kyang.weatherapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import ics340kyang.weatherapp.databinding.FragmentCurrentConditionsBinding
import javax.inject.Inject

@AndroidEntryPoint
class CurrentConditionsFragment : Fragment(R.layout.fragment_current_conditions) {

    private lateinit var binding: FragmentCurrentConditionsBinding
    private val args: CurrentConditionsFragmentArgs by navArgs()

    @Inject
    lateinit var viewModel: CurrentConditionsViewModel

    override fun onResume() {
        super.onResume()
        bindData(args.currentConditionObj)
    }

    //Binds data from the response object
    private fun bindData(currentConditions: CurrentConditions) {
        binding.toolbar.title = "Current Conditions"
        binding.cityName.text = currentConditions.name
        binding.temp.text = getString(R.string.temp, currentConditions.main.temp.toInt())
        binding.feelsLike.text =
            getString(R.string.feels_like, currentConditions.main.feelsLike.toInt())
        binding.low.text = getString(R.string.low, currentConditions.main.tempMin.toInt())
        binding.high.text = getString(R.string.high, currentConditions.main.tempMax.toInt())
        binding.humidity.text =
            getString(R.string.humidity, currentConditions.main.humidity.toInt())
        binding.pressure.text =
            getString(R.string.pressure, currentConditions.main.feelsLike.toInt())
        val iconName = currentConditions.weather.firstOrNull()?.icon
        val iconUrl = "https://openweathermap.org/img/wn/${iconName}@2x.png"
        Glide.with(this)
            .load(iconUrl)
            .into(binding.conditionIcon)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCurrentConditionsBinding.bind(view)

        binding.forecastButton.setOnClickListener() {
            val action =
                CurrentConditionsFragmentDirections.actionCurrentConditionsFragmentToForecastFragment(
                    args.currentConditionObj.coordinates
                )
            findNavController().navigate(action)
        }
    }
}