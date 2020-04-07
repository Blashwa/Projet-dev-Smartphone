package com.example.projetdevsmartphonel3

import android.os.AsyncTask
import android.widget.TextView
import java.net.Socket
import android.util.Log
import com.example.projetdevsmartphonel3.ui.home.HomeFragment
import java.io.BufferedReader
import java.io.InputStreamReader
import com.google.android.gms.maps.model.LatLng

class Trame : AsyncTask<HomeFragment, String, ArrayList<LatLng>>() {
    lateinit var server: Socket
    lateinit var finalText : String
    val port = 55555
    lateinit var homeFragment : HomeFragment

    fun connexion(adresseServeur: String) {
        server = Socket(adresseServeur,port)
    }

    fun getServerResponse():String{
        val br = BufferedReader(InputStreamReader(server.getInputStream()))
        val responseTotal = mutableListOf<String>()
        while(!server.isClosed){
            val texte = br.readLine()
            if(texte!=null) {
                responseTotal.add(texte)
            }
            else
                server.close()
        }
        var responseString=""
        for (line in responseTotal) {
            if(line.startsWith("\$GPRMC"))
                responseString += line + "\n"
        }
        return responseString
    }

    fun decode(reponse : String) : ArrayList<LatLng>{
        val listePoints : ArrayList<LatLng> = ArrayList()   //Liste des coordonnées renvoyées

        //On regarde chaque ligne reçue
        val reponseLignes =reponse.split("\n")
        for( ligne in reponseLignes) {
            if(ligne !="") {
                //On sépare la ligne pour chaque item
                val reponseSplit: List<String> = ligne.split(",")

                //Gestion latitude
                val tempLat = (( reponseSplit[3][0].toString() + reponseSplit[3][1].toString() + "."+reponseSplit[3][2].toString() + reponseSplit[3][3].toString()+reponseSplit[3][5].toString() + reponseSplit[3][6].toString()).toDouble())*100
                val DDt = tempLat.toInt()/100
                val SSt = tempLat - DDt *100
                val tempLatDecimal =DDt + (SSt / 60 )

                //Gestion longitude
                val tempLon =
                    ((reponseSplit[5][0].toString() + reponseSplit[5][1].toString() + reponseSplit[5][2].toString() + "."+ reponseSplit[5][3].toString() + reponseSplit[5][4].toString()+reponseSplit[5][6].toString() + reponseSplit[5][7].toString()).toDouble())*100
                val DDn = tempLon.toInt()/100
                val SSn = tempLon - DDn*100
                val tempLonDecimal =DDn + (SSn / 60 )

                //Gestion négatif et ajout du point à la liste
                if(reponseSplit[4] == "S" && reponseSplit[6] == "W")
                    listePoints.add(LatLng(-tempLatDecimal,-tempLonDecimal))
                if(reponseSplit[4] == "N" && reponseSplit[6] == "W")
                    listePoints.add(LatLng(tempLatDecimal,-tempLonDecimal))
                if(reponseSplit[4] == "S" && reponseSplit[6] == "E")
                    listePoints.add(LatLng(-tempLatDecimal,tempLonDecimal))
                if(reponseSplit[4] == "N" && reponseSplit[6] == "E")
                    listePoints.add(LatLng(tempLatDecimal,tempLonDecimal))

            }
        }
        return listePoints
    }

    override fun doInBackground(vararg params: HomeFragment?): ArrayList<LatLng>? {
        this.homeFragment = params[0]!!
        connexion("192.168.1.73")
        finalText = getServerResponse()
        val decodedReponse = decode(finalText)
        homeFragment.handleState(this,homeFragment.DATA_RECEIVED)
        return decodedReponse
    }



}