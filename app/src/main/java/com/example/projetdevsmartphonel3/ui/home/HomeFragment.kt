package com.example.projetdevsmartphonel3.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.projetdevsmartphonel3.R
import com.example.projetdevsmartphonel3.Trame
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.*
import kotlin.properties.Delegates
import com.google.android.gms.maps.model.LatLng

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val btn = root.launchbtn
        btn.setOnClickListener(){
            btn.visibility=View.INVISIBLE
            val trame = Trame()
            trame.execute()
            var reponse :ArrayList<LatLng> by Delegates.observable(ArrayList()){
                    _, _, newValue -> dataReceived(newValue)
            }

            GlobalScope.launch {
                reponse = trame.get()
            }
        }
        return root
    }

    fun dataReceived(value : ArrayList<LatLng>){
        //Peut être utilisée pour tracer dès les données reçues et traitées
    }


}