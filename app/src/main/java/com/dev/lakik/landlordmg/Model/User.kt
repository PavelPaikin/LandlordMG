package com.dev.lakik.landlordmg.Model

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.github.kittinunf.fuel.android.core.Json
import com.google.gson.Gson
import com.google.gson.JsonElement
import org.json.JSONObject
import java.io.Serializable
import android.content.Context.MODE_PRIVATE
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


/**
 * Created by ppash on 1/26/2018.
 */

data class User (var id: String, var email: String, var name: String, var apiToken: String): Serializable{



    companion object {
        var instance: User? = null
        var userObjFileName = "user.dat"

        fun setUser(str: String?){
            var gson = Gson()
            try{
                val json = gson.fromJson(str, JsonElement::class.java).asJsonObject
                instance = User(json.get("id").asString, json.get("email").asString, json.get("name").asString, json.get("api_token").asString)
            }catch (ex: Exception){}
        }

        fun saveToFile(ctx: Context){
            val fos = ctx.openFileOutput(userObjFileName, Context.MODE_PRIVATE)
            val os = ObjectOutputStream(fos)
            os.writeObject(instance)
            os.close()
            fos.close()
        }

        fun loadFromFile(ctx: Context){
            val fis = ctx.openFileInput(userObjFileName)
            val `is` = ObjectInputStream(fis)
            instance = `is`.readObject() as User
            `is`.close()
            fis.close()
        }


    }

}