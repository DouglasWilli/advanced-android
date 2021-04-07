package com.cesar.advanced_android.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cesar.advanced_android.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val recife = LatLng(-8.0578, -34.882)
        mMap.addMarker(MarkerOptions().position(recife).title("Recife"))

        val saoPaulo = LatLng(-23.5489, -46.6388)
        mMap.addMarker(MarkerOptions().position(saoPaulo).title("Sao Paulo"))

        val beloHorizonte = LatLng(-19.8157, -43.9542)
        mMap.addMarker(MarkerOptions().position(beloHorizonte).title("Belo Horizonte"))

        val rioDeJaneiro = LatLng(-22.9035, -43.2096)
        mMap.addMarker(MarkerOptions().position(rioDeJaneiro).title("Rio de Janeiro"))

        val salvador = LatLng(-12.9704, -38.5124)
        mMap.addMarker(MarkerOptions().position(salvador).title("Salvador"))

        val fozDoIguacu = LatLng(-25.5469, -54.5882)
        mMap.addMarker(MarkerOptions().position(fozDoIguacu).title("Foz do Iguacu"))

        mMap.moveCamera(CameraUpdateFactory.newLatLng(recife))
    }
}