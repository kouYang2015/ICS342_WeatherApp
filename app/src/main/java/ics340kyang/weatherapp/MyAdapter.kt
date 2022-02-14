package ics340kyang.weatherapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MyAdapter(private val data: List<DayForecast>) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    @SuppressLint("NewApi")
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dateView: TextView = view.findViewById(R.id.date)
        private val dateFormatter = DateTimeFormatter.ofPattern("MMM dd")
        private val hourlyFormatter = DateTimeFormatter.ofPattern("hh:mma")
        private val sunriseView: TextView = view.findViewById(R.id.sunrise)
        private val sunsetView: TextView = view.findViewById(R.id.sunset)
        private val tempView: TextView = view.findViewById(R.id.tempFor)
        private val lowView: TextView = view.findViewById(R.id.lowFor)
        private val highView: TextView = view.findViewById(R.id.highFor)

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
            dateView.text = dateFormatter.format(dateTime)
            sunriseView.text = "Sunrise: " + hourlyFormatter.format(sunriseTime)
            sunsetView.text = "Sunset: " + hourlyFormatter.format(sunsetTime)
            tempView.text = "Temp: " + dayForecast.temp.day.toInt() + "°"
            lowView.text = "Low: " + dayForecast.temp.min.toInt() + "°"
            highView.text = "High: " + dayForecast.temp.max.toInt() + "°"
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