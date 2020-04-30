package com.example.projetdevsmartphonel3.ui.SecondActivity

import android.content.Context
import android.content.res.Configuration
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.projetdevsmartphonel3.MainActivity
import com.example.projetdevsmartphonel3.R
import com.google.android.gms.maps.model.LatLng
import kotlin.collections.ArrayList

class ActivityTwoFragment : Fragment(), SensorEventListener {

    private lateinit var dashboardViewModel: SecondViewModel
    private lateinit var sensorManager : SensorManager
    private var accelerometer : Sensor? = null
    var points: ArrayList<LatLng> = ArrayList<LatLng>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(SecondViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_second, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(this, Observer {
            textView.text = it
        })

        // On initialise le SensorManager
        this.sensorManager = context!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // On initialise l'accelerometre en s'assurant que ce dernier existe bien
        this.accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // On demarre le SensorManager
        this.sensorManager.registerListener(this, this.accelerometer, SensorManager.SENSOR_DELAY_NORMAL)

        return root
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            //Log.v("INFOS", "X = " + event.values[0] + "\tY = " + event.values[1] + "\tZ = " + event.values[2])
            translateAccelerometer(event.values[0], event.values[1], event.values[2])
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer.also { light ->
            sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)

    }

    private fun translateAccelerometer(valueX: Float, valueY: Float, valueZ: Float) {

        var activity : MainActivity = getActivity() as MainActivity
        var newPoint: LatLng?
        val coeffLat = 0.000001
        val coeffLng = 0.0001
        var sensibilite = 3
        points.add(0, LatLng(46.147994, -1.169709))

        var avant: Boolean
        var arriere: Boolean
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

        //Execution de la tache
        if (avant && gauche) {
            Log.v("INFOS", "AVANT GAUCHE")
            var newLat = points[points.size - 1].latitude + points[points.size - 1].latitude * coeffLat
            var newLong = points[points.size - 1].longitude + points[points.size - 1].longitude * coeffLng
            newPoint = LatLng(newLat, newLong)
            points.add(newPoint)
        } else if (avant && droite) {
            Log.v("INFOS", "AVANT DROITE")
            var newLat = points[points.size - 1].latitude + points[points.size - 1].latitude * coeffLat
            var newLong = points[points.size - 1].longitude - points[points.size - 1].longitude * coeffLng
            newPoint = LatLng(newLat, newLong)
            points.add(newPoint)
        } else if (avant) {
            Log.v("INFOS", "AVANT")
            var newLat = points[points.size - 1].latitude + points[points.size - 1].latitude * coeffLat
            var newLong = points[points.size - 1].longitude
            newPoint = LatLng(newLat, newLong)
            points.add(newPoint)
        } else if (arriere && gauche) {
            Log.v("INFOS", "ARRIERE GAUCHE")
            var newLat = points[points.size - 1].latitude - points[points.size - 1].latitude * coeffLat
            var newLong = points[points.size - 1].longitude + points[points.size - 1].longitude * coeffLng
            newPoint = LatLng(newLat, newLong)
            points.add(newPoint)
        } else if (arriere && droite) {
            Log.v("INFOS", "ARRIERE DROITE")
            var newLat = points[points.size - 1].latitude - points[points.size - 1].latitude * coeffLat
            var newLong = points[points.size - 1].longitude - points[points.size - 1].longitude * coeffLng
            newPoint = LatLng(newLat, newLong)
            points.add(newPoint)
        } else if (arriere) {
            Log.v("INFOS", "ARRIERE")
            var newLat = points[points.size - 1].latitude - points[points.size - 1].latitude * coeffLat
            var newLong = points[points.size - 1].longitude
            newPoint = LatLng(newLat, newLong)
            points.add(newPoint)
        } else if (gauche) {
            Log.v("INFOS", "GAUCHE")
            var newLat = points[points.size - 1].latitude
            var newLong = points[points.size - 1].longitude + points[points.size - 1].longitude * coeffLng
            newPoint = LatLng(newLat, newLong)
            points.add(newPoint)
        } else if (droite) {
            Log.v("INFOS", "DROITE")
            var newLat = points[points.size - 1].latitude
            var newLong = points[points.size - 1].longitude - points[points.size - 1].longitude * coeffLng
            newPoint = LatLng(newLat, newLong)
            points.add(newPoint)
        }
        activity.setcoords(points)
    }
}