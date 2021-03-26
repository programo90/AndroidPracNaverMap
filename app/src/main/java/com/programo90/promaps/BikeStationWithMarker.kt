package com.programo90.promaps

import android.graphics.drawable.Drawable
import android.util.Log
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage

class BikeStationWithMarker(
    rackTotCnt: Int?
    ,stationName: String?
    ,parkingBikeTotCnt: Int?
    ,shared: Double?
    ,stationLatitude: Double?
    ,stationLongitude: Double?
    ,stationId: String?
    ,stationDt: String?
):BikeStation(
    rackTotCnt
    ,stationName
    ,parkingBikeTotCnt
    ,shared
    ,stationLatitude
    ,stationLongitude
    ,stationId
    ,stationDt
) {
    val marker: Marker = Marker()
    init {
        if(stationLatitude!=null && stationLongitude!=null) {
            marker.position = LatLng(stationLatitude, stationLongitude)
            marker.icon = OverlayImage.fromResource(R.drawable.bikemarker2)
            marker.width = 100
            marker.height = 100
            marker.captionText = "" + parkingBikeTotCnt
            marker.captionTextSize = 30f
            marker.setCaptionAligns(Align.TopRight)
            marker.minZoom = 14.0
        }
    }
}

