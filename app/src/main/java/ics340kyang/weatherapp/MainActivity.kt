package ics340kyang.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // storing ID of the button in a variable
        val button = findViewById<Button>(R.id.forecast_button)

        // operations to be performed when user tap on the button
        button?.setOnClickListener()
        {
            val intent = Intent(this, ForecastActivity::class.java)
            startActivity(intent)
        }
    }
}