package ics340kyang.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ics340kyang.weatherapp.databinding.ActivityForecastBinding
import javax.inject.Inject

@AndroidEntryPoint
class ForecastActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    @Inject
    lateinit var viewModel: ForecastViewModel
    lateinit var binding: ActivityForecastBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForecastBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Creates back button next to activity title
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        viewModel.forecastList.observe(this) {
            recyclerView.adapter = ForecastAdapter(it.list)
        }
        viewModel.loadData()
    }
}