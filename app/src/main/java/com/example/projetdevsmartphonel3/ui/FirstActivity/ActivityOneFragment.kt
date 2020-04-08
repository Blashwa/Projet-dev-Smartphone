package com.example.projetdevsmartphonel3.ui.FirstActivity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.projetdevsmartphonel3.MainActivity
import com.example.projetdevsmartphonel3.R
import com.example.projetdevsmartphonel3.Trame
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.fragment_first.view.*
import kotlinx.coroutines.*
import com.google.android.gms.maps.model.LatLng

class ActivityOneFragment : Fragment() {

    private lateinit var firstViewModel: FirstViewModel
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
                    launchbtn.isClickable=true
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firstViewModel =
            ViewModelProviders.of(this).get(FirstViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_first, container, false)

        val btn = root.launchbtn
        btn.setOnClickListener(){
            btn.isClickable=false
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