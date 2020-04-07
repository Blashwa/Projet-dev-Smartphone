package com.example.projetdevsmartphonel3

import android.content.Context
import android.content.res.Configuration
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
            //Log.v("INFOS", "X = " + event.values[0] + "\tY = " + event.values[1] + "\tZ = " + event.values[2])
            translateAccelerometer(event.values[0], event.values[1], event.values[2])
        }
    }

    fun translateAccelerometer(valueX: Float, valueY: Float, valueZ: Float) {

        var sensibilite = 5

        var avant = false
        var arriere = false
        var gauche = false
        var droite = false

        // On traduit la valeur Z
        if (valueZ > sensibilite) {
            avant = true
            arriere = false
        } else if (valueZ < -sensibilite) {
            arriere = true
            avant = false
        } else {
            avant = false
            arriere = false
        }

        // Le telephone est oriente en mode portrait
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            // On traduit la valeur X
            if (valueX > sensibilite) {
                gauche = true
                droite = false
            } else if (valueX < -sensibilite) {
                droite = true
                gauche = false
            } else {
                gauche = false
                droite = false
            }
        }

        // Le telephone est oriente en mode paysage
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            // On traduit la valeur Y
            if (valueY > sensibilite) {
                gauche = true
                droite = false
            } else if (valueY < -sensibilite) {
                droite = true
                gauche = false
            } else {
                gauche = false
                droite = false
            }
        }

        // On affiche la traduction dans le terminal
        if (avant && gauche) {
            Log.v("INFOS", "AVANT GAUCHE")
        } else if (avant && droite) {
            Log.v("INFOS", "AVANT DROITE")
        } else if (avant) {
            Log.v("INFOS", "AVANT")
        } else if (arriere && gauche) {
            Log.v("INFOS", "ARRIERE GAUCHE")
        } else if (arriere && droite) {
            Log.v("INFOS", "ARRIERE DROITE")
        } else if (arriere) {
            Log.v("INFOS", "ARRIERE")
        } else if (gauche) {
            Log.v("INFOS", "GAUCHE")
        } else if (droite) {
            Log.v("INFOS", "DROITE")
        }

    }
}
