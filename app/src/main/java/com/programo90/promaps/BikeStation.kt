package com.programo90.promaps

import java.io.Serializable

open class BikeStation(
    var rackTotCnt: Int? =null
    ,var stationName: String? =null
    ,var parkingBikeTotCnt: Int? =null
    ,var shared: Double? =null
    ,var stationLatitude: Double? =null
    ,var stationLongitude: Double? =null
    ,var stationId: String? =null
    ,var stationDt: String? =null
): Serializable
