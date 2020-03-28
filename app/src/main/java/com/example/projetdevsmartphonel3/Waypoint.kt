package com.example.projetdevsmartphonel3

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class Waypoint(var coordX: Double, var coordY: Double, var title: String?=null, var speedAtPoint: Double?=null) {

    /**
     * Ajouter un Marker sur une carte GoogleMap
     */
    fun addMarkerToMap(gMap: GoogleMap): Marker {
        var markerOptions = MarkerOptions()

        markerOptions.position(LatLng(coordX, coordY))
        markerOptions.title(title)

        return gMap.addMarker(markerOptions)
    }

}