package ics340kyang.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ForecastActivity : AppCompatActivity() {

    lateinit var recyclerView : RecyclerView

    val adapterData = listOf<Data>(
        Data(1644287001),
        Data(1644373401),
        Data(1644459801),
        Data(1644546201),
        Data(1644632601),
        Data(1644719001),
        Data(1644805401),
        Data(1644891801),
        Data(1644978201),
        Data(1645064601),
        Data(1645151001),
        Data(1645237401),
        Data(1645323801),
        Data(1645410201),
        Data(1645496601),
        Data(1645583001),
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