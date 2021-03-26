package com.programo90.promaps

class RetrofitGeo(
     var status: String
    ,var meta: GeoMeta
    ,var addresses: List<GeoAddress>
    ,var errorMessage: String?
)

class GeoMeta(
    var totalCount: String
    ,var page: String
    ,var count: String
)

class GeoAddress(
    var roadAddress: String?
    ,var jibunAddress: String?
    ,var englishAddress: String?
    ,var addressElements: List<GeoAddressElement>
    ,var x: String
    ,var y: String
    ,var distance: String
)

class GeoAddressElement(
    var types: List<String>
    ,var longName: String?
    ,var shortname: String?
    ,var code: String?
)