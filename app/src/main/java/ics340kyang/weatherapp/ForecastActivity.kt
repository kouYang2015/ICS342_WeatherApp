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
        DayForecast(1644377880, 1644321600, 1644364140, forecastTempData[0], 80F, 1021),
        DayForecast(1644428100, 1644321700, 1644364240, forecastTempData[1], 78F, 1100),
        DayForecast(1644476220, 1644321800, 1644364340, forecastTempData[2], 83F, 1078),
        DayForecast(1644605040, 1644321900, 1644364440, forecastTempData[3], 81F, 1050),
        DayForecast(1644663600, 1644322000, 1644364540, forecastTempData[4], 77F, 1021),
        DayForecast(1644778200, 1644322100, 1644364640, forecastTempData[5], 67F, 1025),
        DayForecast(1644832800, 1644322200, 1644364740, forecastTempData[6], 82F, 1027),
        DayForecast(1644909880, 1644322300, 1644364840, forecastTempData[7], 89F, 1031),
        DayForecast(1645006280, 1644322400, 1644364940, forecastTempData[8], 87F, 1040),
        DayForecast(1645102680, 1644322500, 1644365040, forecastTempData[9], 83F, 1010),
        DayForecast(1645209080, 1644322600, 1644365140, forecastTempData[10], 99F, 1015),
        DayForecast(1645305480, 1644322700, 1644365240, forecastTempData[11], 100F, 1023),
        DayForecast(1645401880, 1644322800, 1644365340, forecastTempData[12], 97F, 1029),
        DayForecast(1645508280, 1644322900, 1644365440, forecastTempData[13], 94F, 1043),
        DayForecast(1645554680, 1644323000, 1644365540, forecastTempData[14], 88F, 1047),
        DayForecast(1645601080, 1644323100, 1644365640, forecastTempData[15], 78F, 1078),
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