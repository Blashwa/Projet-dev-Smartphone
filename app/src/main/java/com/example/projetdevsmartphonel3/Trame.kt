package com.example.projetdevsmartphonel3

import android.os.AsyncTask
import androidx.fragment.app.Fragment
import java.net.Socket
import com.example.projetdevsmartphonel3.ui.FirstActivity.ActivityOneFragment
import java.io.BufferedReader
import java.io.InputStreamReader
import com.google.android.gms.maps.model.LatLng

class Trame : AsyncTask<Fragment, String, ArrayList<LatLng>>() {
    lateinit var server: Socket
    lateinit var filteredResponse : String
    val port = 55555

    fun connexion(adresseServeur: String) {
        server = Socket(adresseServeur,port)
    }

    fun getServerResponse():String{
        val br = BufferedReader(InputStreamReader(server.getInputStream()))
        val responseServerTotal = mutableListOf<String>()
        while(!server.isClosed){
            val texte = br.readLine()
            if(texte!=null) {
                responseServerTotal.add(texte)
            }
            else
                server.close()
        }
        var responseFiltered=""
        for (line in responseServerTotal) {
            if(line.startsWith("\$GPRMC"))
                responseFiltered += line + "\n"
        }
        return responseFiltered
    }

    fun decode(reponse : String) : ArrayList<LatLng>{
        val listePoints : ArrayList<LatLng> = ArrayList()   //Liste des coordonnées renvoyées

        //On regarde chaque ligne reçue
        val reponseSplitLignes =reponse.split("\n")
        for( ligne in reponseSplitLignes) {
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

    override fun doInBackground(vararg params: Fragment?): ArrayList<LatLng>? {
        if(params[0]!! is ActivityOneFragment) {
            val fragment = params[0]!! as ActivityOneFragment
            connexion("192.168.1.73")
            filteredResponse = getServerResponse()
            val decodedReponse = decode(filteredResponse)
            fragment.handleState(this, fragment.DATA_RECEIVED)
            return decodedReponse
        }
        return null
    }



}