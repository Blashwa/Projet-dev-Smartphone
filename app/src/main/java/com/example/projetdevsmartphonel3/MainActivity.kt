package com.example.projetdevsmartphonel3

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.fragment_home.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager : SensorManager
    private lateinit var accelerometer : Sensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // On initialise le SensorManager
        this.sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // On initialise l'accelerometre en s'assurant que ce dernier existe bien
        this.accelerometer = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // On demarre le SensorManager
        this.sensorManager.registerListener(this, this.accelerometer, SensorManager.SENSOR_DELAY_NORMAL)

        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            Log.v("WESH", "X = " + event.values[0] + "\tY = " + event.values[1] + "\tZ = " + event.values[2])
        }
    }
}
