package com.programo90.promaps

import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class NGeocoding(var search: String):AsyncTask<Any?, Any?, RetrofitGeo?>() {
    override fun onPostExecute(result: RetrofitGeo?) {

    }

    override fun doInBackground(vararg params: Any?): RetrofitGeo? {

        return getAddrRetrofit(search)
    }

    fun getAddrRetrofit(searchAddr: String): RetrofitGeo? {
        var retrofit = Retrofit.Builder()
                .baseUrl("https://naveropenapi.apigw.ntruss.com/map-geocode/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val service = retrofit.create(RetrofitService::class.java)

        var retrofitGeo: RetrofitGeo? = null

        service.getAddress("c0ur4dcf04","1eSsj40gTS8X9XTJC3IcINq6d0lWiOgxak6TmxTZ", "구로구 고척로52길 21",5)
                .enqueue(object : Callback<RetrofitGeo>{
                    override fun onFailure(call: Call<RetrofitGeo>, t: Throwable) {
                        Log.d("Retrofit Error ", t.message.toString())
                        retrofitGeo = null
                    }

                    override fun onResponse(call: Call<RetrofitGeo>, response: Response<RetrofitGeo>) {
                        if(response.isSuccessful) {
                            Log.d("Retrofit ",response.message())

                            Log.d("retrofitGeo",response.body()!!.meta.totalCount)
                            retrofitGeo = response.body()
                        }
                    }
                })

        return retrofitGeo
    }

    fun getAddress(searchAddr: String):RetrofitGeo? {
        var url = URL("https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + "구로구 고척로52길 21" + "&count=5")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        connection.setRequestProperty("X-NCP-APIGW-API-KEY-ID","c0ur4dcf04")
        connection.setRequestProperty("X-NCP-APIGW-API-KEY","1eSsj40gTS8X9XTJC3IcINq6d0lWiOgxak6TmxTZ")

        var buffer = ""
        if(connection.responseCode == HttpURLConnection.HTTP_OK) {
            var reader = BufferedReader(InputStreamReader(connection.inputStream))
            buffer = reader.readLine()
            Log.d("geocoding result ",buffer)

            var retrofitGeo = Gson().fromJson(buffer,RetrofitGeo::class.java)
            return retrofitGeo
        } else {
            Log.d("Geocoding conn ","fail")
            return null
        }
    }



}
