package com.dev.lakik.landlordmg.Model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.github.kittinunf.fuel.android.core.Json
import com.google.gson.Gson
import com.google.gson.JsonElement
import org.json.JSONObject


/**
 * Created by ppash on 1/26/2018.
 */

data class User (var id: String, var email: String, var name: String, var apiToken: String){

    companion object {
        var instance: User? = null

        fun setUser(str: String?){
            var gson = Gson()
            try{
                val json = gson.fromJson(str, JsonElement::class.java).asJsonObject
                instance = User(json.get("id").asString, json.get("email").asString, json.get("name").asString, json.get("api_token").asString)
            }catch (ex: Exception){}
        }
    }

}