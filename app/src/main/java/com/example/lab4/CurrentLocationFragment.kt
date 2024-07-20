package com.example.lab4

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import androidx.fragment.app.activityViewModels

class CurrentLocationFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_current_location, container, false)
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        view.findViewById<Button>(R.id.updateLocationButton).setOnClickListener {
            updateParkingLocation()
        }

        view.findViewById<Button>(R.id.parkHereButton).setOnClickListener {
            requestLocationPermission()
        }

        return view
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
    }

    private fun updateParkingLocation() {
        val newLocation = LatLng(37.7749, -122.4194) // Example coordinates
        googleMap.addMarker(MarkerOptions().position(newLocation).title("Parking Location"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 15f))
        sharedViewModel.setParkingLocation("Latitude: ${newLocation.latitude}, Longitude: ${newLocation.longitude}")
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission already granted
            // Handle location updates or related logic here
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100

        @JvmStatic
        fun newInstance() = CurrentLocationFragment()
    }
}

