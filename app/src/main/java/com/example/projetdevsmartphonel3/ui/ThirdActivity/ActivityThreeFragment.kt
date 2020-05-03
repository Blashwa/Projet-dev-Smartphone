package com.example.projetdevsmartphonel3.ui.ThirdActivity

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
        val buttonClear : Button = root.findViewById(R.id.clearButton)
        val activity : MainActivity = getActivity() as MainActivity
        buttonClear.setOnClickListener{ activity.clearMap()}


        notificationsViewModel.text.observe(this, Observer {

        })
        return root
    }

    fun saveGpx(){
        val file = File(context?.getExternalFilesDir(null),"datanmea.gpx")

        if(!file.exists()){
            file.createNewFile()
        }
        var data: String =""
        data+="<gpx><metadata></metadata><trk><name></name><desc></desc><trkseg>"

        val activity : MainActivity = getActivity() as MainActivity
        val listePoints = activity.getPoints()

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

        val activity : MainActivity = getActivity() as MainActivity
        val listePoints = activity.getPoints()



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