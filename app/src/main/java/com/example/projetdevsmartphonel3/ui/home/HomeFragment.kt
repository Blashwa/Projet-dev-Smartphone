package com.example.projetdevsmartphonel3.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
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
import com.example.projetdevsmartphonel3.MainActivity
import com.example.projetdevsmartphonel3.R
import com.example.projetdevsmartphonel3.Trame
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.*
import kotlin.properties.Delegates
import com.google.android.gms.maps.model.LatLng

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    val DATA_RECEIVED = 1
    var reponse :ArrayList<LatLng> = ArrayList<LatLng>()

    private val handler: Handler = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(inputMessage: Message) {
            val trame = inputMessage.obj as Trame
            when(inputMessage.what){
                DATA_RECEIVED -> {
                    val activity : MainActivity = getActivity() as MainActivity
                    while(reponse.isEmpty()){
                    }
                    activity.setcoords(reponse)
                    texteConnexion.text= getString(R.string.connectionDone)
                }
            }
        }
    }

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
            root.texteConnexion.text=getString(R.string.connectionProgress)
            trame.execute(this)

            GlobalScope.launch {
                reponse = trame.get()
            }
        }
        return root
    }


    fun handleState(trame : Trame, state : Int){
        when(state){
            DATA_RECEIVED->{
                handler.obtainMessage(state,trame)?.apply{
                    sendToTarget()
                }
            }
        }
    }


}