package com.programo90.promaps

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import androidx.core.content.ContextCompat
import com.naver.maps.geometry.LatLng
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class NReverseGeocoding(
    var currLocation: LatLng
    ,var mainActivity: Context
): AsyncTask<Any?, Any?, String>() {
    /*
    "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?coords=37.5670135,126.9783740&output=json"
     -H "X-NCP-APIGW-API-KEY-ID: j6cv0pdet0"
     -H "X-NCP-APIGW-API-KEY: sz3IcylC1mMyxga53qhmiYAdPIJf28CjzQeoGy7I" -v
    */

    override fun onPostExecute(result: String) {
        var intent = Intent(mainActivity, DirectionActivity::class.java)


        /*var intent = Intent(this@MainActivity,DirectionActivity::class.java)
        */
        intent.putExtra("set","origin")
        intent.putExtra("origin",result)
        ContextCompat.startActivity(mainActivity,intent,null)
    }

    override fun doInBackground(vararg params: Any?): String {

        //"37.504395, 126.856650"
        //currLocation.longitude + ","+currLocation.latitude

        var url =
            URL("https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?coords=" + currLocation.longitude + ","+currLocation.latitude  + "&orders=legalcode,addr,admcode,roadaddr&output=json")
        var connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.requestMethod ="GET"
        connection.setRequestProperty("X-NCP-APIGW-API-KEY-ID","j6cv0pdet0")
        connection.setRequestProperty("X-NCP-APIGW-API-KEY","sz3IcylC1mMyxga53qhmiYAdPIJf28CjzQeoGy7I")

        var buffer = ""
        if(connection.responseCode == HttpURLConnection.HTTP_OK) {
            var reader = BufferedReader(InputStreamReader(connection.inputStream))
            buffer = reader.readLine()

            //Log.d("ReverseGeocoding result : ", buffer)

            val jsonArray = JSONObject(buffer).getJSONArray("results")
            for(index in 0 until  jsonArray.length()) {
                var addrJsonObject = jsonArray.getJSONObject(index)
                Log.d("json name ",addrJsonObject.getString("name"))
            }
        } else {
            Log.d("ReverseGeocoding reponse code : ", connection.responseCode.toString())
        }


        return "서울 시청"
    }
}