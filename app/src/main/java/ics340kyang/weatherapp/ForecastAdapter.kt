package ics340kyang.weatherapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ics340kyang.weatherapp.databinding.RowDataBinding
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ForecastAdapter(private val data: List<DayForecast>) :
    RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    @SuppressLint("NewApi")
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: RowDataBinding = RowDataBinding.bind(view)
        private val dateFormatter = DateTimeFormatter.ofPattern("MMM dd")
        private val hourlyFormatter = DateTimeFormatter.ofPattern("hh:mma")

        fun bind(dayForecast: DayForecast) {
            val instant = Instant.ofEpochSecond(dayForecast.date)
            val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            val sunriseTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(dayForecast.sunrise),
                ZoneId.systemDefault()
            )
            val sunsetTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(dayForecast.sunset),
                ZoneId.systemDefault()
            )

            binding.date.text = dateFormatter.format(dateTime)
            binding.sunrise.text = "Sunrise: " + hourlyFormatter.format(sunriseTime)
            binding.sunset.text = "Sunset: " + hourlyFormatter.format(sunsetTime)
            binding.tempFor.text = "Temp: " + dayForecast.temp.day.toInt() + "°"
            binding.lowFor.text = "Low: " + dayForecast.temp.min.toInt() + "°"
            binding.highFor.text = "High: " + dayForecast.temp.max.toInt() + "°"
            val iconName = dayForecast.weather.firstOrNull()?.icon
            val iconUrl = "https://openweathermap.org/img/wn/${iconName}@2x.png"
            Glide.with(this.itemView.context)
                .load(iconUrl)
                .into(binding.forecastCondIcon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_data, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size
}