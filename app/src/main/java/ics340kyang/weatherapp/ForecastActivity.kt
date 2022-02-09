package ics340kyang.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ForecastActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    val forecastTempData = listOf<ForecastTemp>(
        ForecastTemp(30F, 25F, 40F),
        ForecastTemp(23F, 21F, 25F),
        ForecastTemp(11F, 9F, 15F),
        ForecastTemp(13F, 10F, 16F),
        ForecastTemp(19F, 17F, 23F),
        ForecastTemp(17F, 15F, 26F),
        ForecastTemp(26F, 16F, 32F),
        ForecastTemp(30F, 18F, 37F),
        ForecastTemp(33F, 25F, 40F),
        ForecastTemp(36F, 28F, 45F),
        ForecastTemp(34F, 24F, 43F),
        ForecastTemp(31F, 29F, 39F),
        ForecastTemp(20F, 13F, 28F),
        ForecastTemp(14F, 11F, 24F),
        ForecastTemp(17F, 10F, 25F),
        ForecastTemp(26F, 13F, 30F),
    )

    val adapterData = listOf<DayForecast>(
        DayForecast(1644341400, 1644301800, 1644341400, forecastTempData[0], 80F, 1021),
        DayForecast(1644428100, 1644384900, 1644428100, forecastTempData[1], 78F, 1100),
        DayForecast(1644471600, 1644471600, 1644518400, forecastTempData[2], 83F, 1078),
        DayForecast(1644605040, 1644558300, 1644605040, forecastTempData[3], 81F, 1050),
        DayForecast(1644637080, 1644637080, 1644691560, forecastTempData[4], 77F, 1021),
        DayForecast(1644778200, 1644731400, 1644778200, forecastTempData[5], 67F, 1025),
        DayForecast(1644809880, 1644817620, 1644864420, forecastTempData[6], 82F, 1027),
        DayForecast(1644896280, 1644904620, 1644950520, forecastTempData[7], 89F, 1031),
        DayForecast(1644982680, 1644982680, 1645037820, forecastTempData[8], 87F, 1040),
        DayForecast(1644341400, 1644301800, 1644341400, forecastTempData[9], 83F, 1010),
        DayForecast(1644341400, 1644301800, 1644341400, forecastTempData[10], 99F, 1015),
        DayForecast(1644341400, 1644301800, 1644341400, forecastTempData[11], 100F, 1023),
        DayForecast(1644341400, 1644301800, 1644341400, forecastTempData[12], 97F, 1029),
        DayForecast(1644341400, 1644301800, 1644341400, forecastTempData[13], 94F, 1043),
        DayForecast(1644341400, 1644301800, 1644341400, forecastTempData[14], 88F, 1047),
        DayForecast(1644341400, 1644301800, 1644341400, forecastTempData[15], 78F, 1078),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        val actionBar = supportActionBar

        actionBar!!.title = "ForecastActivity"

        actionBar.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MyAdapter(adapterData)
    }
}