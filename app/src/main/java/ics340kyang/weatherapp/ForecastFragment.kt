package ics340kyang.weatherapp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ics340kyang.weatherapp.databinding.FragmentForecastBinding
import javax.inject.Inject

@AndroidEntryPoint
class ForecastFragment : Fragment(R.layout.fragment_forecast) {

    private val args: ForecastFragmentArgs by navArgs()

    @Inject
    lateinit var viewModel: ForecastViewModel
    lateinit var binding: FragmentForecastBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentForecastBinding.bind(view)
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        viewModel.forecastList.observe(this) {
            binding.recyclerView.adapter = ForecastAdapter(it.list)
        }
        viewModel.loadData(args.zipInput)
        binding.toolbar.title = "Forecast"
    }
}