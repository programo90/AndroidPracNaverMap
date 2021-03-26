package com.programo90.promaps

import android.os.AsyncTask
import android.util.Log
import android.widget.LinearLayout
import com.google.gson.Gson
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.collections.ArrayList

class BikeService(var listResult: MutableList<BikeStationWithMarker>, stationInfoBox: LinearLayout)
    : AsyncTask<Any?, Any?, MutableList<BikeStation>>() {
    override fun onPostExecute(result: MutableList<BikeStation>?) {
        super.onPostExecute(result)
        if(result!!.isNotEmpty()) {
            for(i in 0 until result.size) {
                val currStation = result.get(i)
                val bikeStationWithMarker = BikeStationWithMarker(
                        currStation.rackTotCnt
                        ,currStation.stationName
                        ,currStation.parkingBikeTotCnt
                        ,currStation.shared
                        ,currStation.stationLatitude
                        ,currStation.stationLongitude
                        ,currStation.stationId
                        ,currStation.stationDt
                )
                /*bikeStationWithMarker.marker.setOnClickListener {
                    Log.d("set click listener", "success")

                    false
                }*/
                listResult.add(bikeStationWithMarker)
            }
        }
    }

    override fun doInBackground(vararg params: Any?): MutableList<BikeStation> {
        //json 통신으로 공공데이터 포털에서 실시간 따릉이 대여정보를 받는다.
        val bikeStationList = ArrayList<BikeStation>()

        val urlString = "http://openapi.seoul.go.kr:8088/"
        val apiKey = "455a656b4a6a696d3934734c51464a"
        val urlSub = "/json/bikeList/"
        var date =  CurrTimer.getCurrDate()

        var totalRow = 1
        while(totalRow < 2000) {
            //val url: URL = URL(urlString + apiKey + urlSub + "${totalRow}/${totalRow+999}/" + date)
            val url: URL = URL(urlString + apiKey + urlSub + "${totalRow}/${totalRow+999}/ + date")
            totalRow += 1000
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Content-type","applcation/json")
            var buffer = ""
            if(connection.responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream, "UTF-8"))
                buffer = reader.readLine()

                val jsonObject = JSONObject(buffer).getJSONObject("rentBikeStatus")
                val jsonArray = jsonObject.getJSONArray("row")

                var index = 0
                for(index in 0 until jsonArray.length()) {
                    bikeStationList.add(Gson().fromJson(jsonArray.get(index).toString(), BikeStation::class.java))
                }
            }
        }

        return bikeStationList
    }

    fun getStationList(): List<BikeStationWithMarker> {
        //getRealtimeStationList() 메서드로 List<BikeStation>를
        //List<BikeStationWithMarker> 타입으로 변환해서 리턴한다.
        val bikeStationList = ArrayList<BikeStationWithMarker>()

        /*getRealtimeStationList().forEach { bikeStation ->
            bikeStation.
            bikeStationList.add()
        }*/

        return ArrayList<BikeStationWithMarker>()
    }

    companion object {
        fun getRealtimeStationList(): MutableList<BikeStationWithMarker> {
            //json 통신으로 공공데이터 포털에서 실시간 따릉이 대여정보를 받는다.
            //val urlString:String = "http://openapi.seoul.go.kr:8088/455a656b4a6a696d3934734c51464a/json/bikeList/1/1000/20210209"
            val urlString = "http://openapi.seoul.go.kr:8088/"
            val apiKey = "455a656b4a6a696d3934734c51464a"
            val urlSub = "/json/bikeList/"
            var date =  CurrTimer.getCurrDate()

            var urlStr = urlString + apiKey + urlSub + "1/10/" + date

            Log.d("date : ", date)

            val url: URL = URL(urlStr)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "GET"
            connection.setRequestProperty("Content-type","applcation/json")

            var buffer = ""
            if(connection.responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream, "UTF-8"))
                buffer = reader.readLine()
            }
            Log.d("get result : " , buffer)

            return  Gson().fromJson(buffer, Array<BikeStationWithMarker>::class.java).toMutableList()
        }
    }
}
