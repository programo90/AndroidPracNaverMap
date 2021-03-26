package com.programo90.promaps

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.annotation.UiThread
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.programo90.promaps.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {

    private lateinit var binding : ActivityMainBinding
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private lateinit var viewModel: MainViewModel

    private var locationManager: LocationManager? = null
    var locationListener: LocationListener? = null

    private lateinit var bikeStationList: MutableList<BikeStationWithMarker>
    private var showBikeStation = false
    private var showScooterStation = false
    private lateinit var origin: LatLng
    private lateinit var destination: LatLng
    private lateinit var checkedMarekr: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        locationSource = FusedLocationSource(this@MainActivity, PERMISSION_REQUEST_CODE)

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
                ?: MapFragment.newInstance().also {
                    fm.beginTransaction().add(R.id.map_fragment, it).commit()
                }

        mapFragment.getMapAsync(this)

        //locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //bikeStationList = BikeService.getRealtimeStationList()


        binding.viewBicleBtn.setOnClickListener {
            if (showBikeStation) {
                clearBikeMarker()
                showBikeStation = false
                it.background = null
            } else {
                showBikeStation = true
                showScooterStation = false
                updateMarkers()

                it.setBackgroundResource(R.drawable.under_radiusbtn)
                binding.viewScooterBtn.background = null
            }
        }

        binding.viewScooterBtn.setOnClickListener {
            if (showScooterStation) {
                showScooterStation = false
                it.background = null
            } else {
                naverMap.moveCamera(CameraUpdate.scrollTo(LatLng(37.5666102, 126.9783881)))
                clearBikeMarker()
                showBikeStation = false
                showScooterStation = true

                it.setBackgroundResource(R.drawable.under_radiusbtn)
                binding.viewBicleBtn.background = null
            }
        }

        binding.setOriginBtn.setOnClickListener {
            var intent = Intent(this@MainActivity,DirectionActivity::class.java)
            intent.putExtra("set","origin")
            intent.putExtra("origin",binding.stationNameBox.text.toString())
            startActivity(intent)
        }

        binding.setDestinationBtn.setOnClickListener {
            var intent = Intent(this@MainActivity,DirectionActivity::class.java)
            intent.putExtra("set","destination")
            intent.putExtra("destination",binding.stationNameBox.text.toString())
            startActivity(intent)
        }

        binding.directionSearchBox.setOnClickListener {
            startActivity(Intent(this@MainActivity,AddrSearchActivity::class.java))
        }

        binding.directionNavigatorBtn.setOnClickListener {
            NReverseGeocoding(LatLng(37.5670135,126.9783740),this@MainActivity).execute()


            /*var intent = Intent(this@MainActivity,DirectionActivity::class.java)
            naverMap.locationOverlay.position
            intent.putExtra("set","origin")
            intent.putExtra("origin",addr)
            startActivity(intent)*/
        }



    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) {
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        val uiSettings = naverMap.uiSettings
        uiSettings.isLocationButtonEnabled = true

        naverMap.locationSource = locationSource

        val locationOverlay = naverMap.locationOverlay
        locationOverlay.isVisible = true
        //locationOverlay.icon = OverlayImage.fromResource(R.drawable.here)
        locationOverlay.iconWidth = 100
        locationOverlay.iconHeight = 100
        locationOverlay.minZoom = 14.0
        locationOverlay.isMinZoomInclusive = true

        bikeStationList = ArrayList<BikeStationWithMarker>()
        BikeService(bikeStationList,binding.stationInfoBox).execute()

        setClickListenerOnMarkers(bikeStationList)

        naverMap.addOnCameraIdleListener {
            updateMarkers()
        }

        naverMap.locationTrackingMode = LocationTrackingMode.Follow

    }

    private fun setBikeMarker() {
        bikeStationList.forEach { station ->
            var marker = station.marker
            marker.map = naverMap
        }
    }

    private fun setClickListenerOnMarkers(bikeStationList: MutableList<BikeStationWithMarker>) {
        bikeStationList.forEach {
            it.marker.apply {
                setOnClickListener {
                    Log.d("click marker in main","click!")
                    true
                }
            }
        }
    }

    private fun clearBikeMarker() {
        bikeStationList.forEach { station ->
            station.marker.map = null
        }
    }

    private fun updateMarkers() {
        if (!showBikeStation) {
            return
        }

        for (index in 0 until bikeStationList.size) {
            var contentBounds = naverMap.contentBounds
            var currStaion = bikeStationList.get(index)
            var marker = currStaion.marker
            var position = marker.position
            if(contentBounds.contains(position)) {

                marker.setOnClickListener {
                    if(binding.stationInfoBox.visibility == LinearLayout.GONE) {
                        binding.stationNameBox.text = currStaion.stationName!!.substringAfter('.').trim()
                        binding.stationIdBox.text = currStaion.stationId
                        //naverMap.locationOverlay.position
                        binding.destanceFromCurrTextBox.text = position.distanceTo(LatLng(37.5670135,126.9783740)).toInt().toString()+"m"
                        binding.stationInfoBox.visibility = LinearLayout.VISIBLE
                    } else {
                        binding.stationInfoBox.visibility = LinearLayout.GONE
                    }

                    false
                }
                marker.map = naverMap
            }
        }
    }


    companion object {
        private const val PERMISSION_REQUEST_CODE = 1000
        private val PERMISSIONS = arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION
                , android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    override fun onStart() {
        super.onStart()
        if (ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE)
            return
        }
    }

    override fun onLocationChanged(location: Location) {
        if (location == null) return

        naverMap?.let {
            val coord = LatLng(location)

            val locationOverlay = it.locationOverlay
            locationOverlay.isVisible = true
            locationOverlay.position = coord
            locationOverlay.bearing = location.bearing

            it.moveCamera(CameraUpdate.scrollTo(coord))
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        locationManager?.removeUpdates(this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }


    override fun onStatusChanged(provider: String, status: Int,
                                 extras: Bundle) {
    }

    override fun onProviderEnabled(provider: String) {
    }

    override fun onProviderDisabled(provider: String) {
    }
}