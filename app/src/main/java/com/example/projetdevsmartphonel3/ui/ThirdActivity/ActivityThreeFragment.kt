package com.example.projetdevsmartphonel3.ui.ThirdActivity

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.projetdevsmartphonel3.MainActivity
import com.example.projetdevsmartphonel3.R
import com.example.projetdevsmartphonel3.Waypoint
import kotlinx.android.synthetic.main.fragment_third.*
import kotlinx.android.synthetic.main.fragment_third.view.*
import java.io.File

class ActivityThreeFragment : Fragment() {

    private lateinit var notificationsViewModel: ThirdViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(ThirdViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_third, container, false)
        val buttonGpx = root.btnGPX
        buttonGpx.setOnClickListener(){
            saveGpx()
        }

        val buttonJson = root.btnJSON
        buttonJson.setOnClickListener(){
            saveJson()
        }
        return root
    }

    fun saveGpx(){
        val file = File(context?.getExternalFilesDir(null),"datanmea.gpx")

        if(!file.exists()){
            file.createNewFile()
        }
        var data: String =""
        data+="<gpx><metadata></metadata><trk><name></name><desc></desc><trkseg>"
/*
        val activity : MainActivity = getActivity() as MainActivity
        val listePoints = activity.getPoints()

 */
        val listePoints =ArrayList<Waypoint>()
        listePoints.add(Waypoint(45.0, -1.5, ""))
        listePoints.add(Waypoint(44.0, -1.0, ""))
        listePoints.add(Waypoint(43.0, -0.5, ""))
        listePoints.add(Waypoint(30.0, 10.0, ""))
        listePoints.add(Waypoint(41.0, 0.5, ""))
        for (point in listePoints){
            data+="<trkpt lat=\""+point.coordX+"\" lon=\""+point.coordY+"\"/>"
        }

        data+="</trkseg></trk></gpx>"
        file.writeText(data)
    }

    fun saveJson(){
        val file = File(context?.getExternalFilesDir(null),"datanmea.json")

        var data: String =""

        if(!file.exists()){
            file.createNewFile()
        }
        data+="{\"type\":\"FeatureCollection\",\"features\":["
        data+="{\"type\":\"Feature\",\"geometry\":{"
        data+="\"type\":\"LineString\",\"coordinates\":["
/*
        val activity : MainActivity = getActivity() as MainActivity
        val listePoints = activity.getPoints()


 */
        val listePoints =ArrayList<Waypoint>()
        listePoints.add(Waypoint(45.0, -1.5, ""))
        listePoints.add(Waypoint(44.0, -1.0, ""))
        listePoints.add(Waypoint(43.0, -0.5, ""))
        listePoints.add(Waypoint(42.0, 0.0, ""))
        listePoints.add(Waypoint(41.0, 0.5, ""))

        for (i in 0 until listePoints.size-1){
            val point=listePoints.get(i)
            data+="["+point.coordX+","+point.coordY+"],"
        }
        data+="["+listePoints.get(listePoints.size-1).coordX+","+listePoints.get(listePoints.size-1).coordY+"]"

        data+="]}}]}"

        file.writeText(data)
        Log.d("test",data)
    }
}