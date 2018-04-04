package com.dev.lakik.landlordmg.Model

import android.content.Context
import android.support.design.widget.Snackbar
import com.dev.lakik.landlordmg.Common.URLS
import com.dev.lakik.landlordmg.Helpers.HTTPHelper
import com.dev.lakik.landlordmg.R
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import com.google.gson.JsonElement
import org.json.JSONObject
import java.io.Serializable
import com.google.gson.reflect.TypeToken



/**
 * Created by ppash on 3/16/2018.
 */
class Property(var id: String,
               var parentId: String,
               var userId: String,
               var type: PropertyType,
               var name: String,
               var address1: String,
               var address2: String,
               var city: String,
               var province: String,
               var postalCode: String,
               var rooms: Int,
               var bathrooms: Int,
               var notes: String,
               var furneture: Int,
               var petFriendly: Int): Serializable {

    enum class PropertyType{
        SINGLE_UNIT,
        MULTI_UNIT
    }



    fun save(callback: () -> Unit){
        var url = ""
        if(id.isEmpty()){
            url = URLS.PROPERTY_CREATE
        }else{

        }

        var propType = "single"
        if(type == PropertyType.MULTI_UNIT) propType = "multy"

        val json = JSONObject()
        json.put("id", id)
        json.put("parentId", parentId)
        json.put("userId", userId)
        json.put("name", name)
        json.put("type", propType)
        json.put("address1", address1)
        json.put("address2", address2)
        json.put("city", city)
        json.put("state", province)
        json.put("postalCode", postalCode)
        json.put("rooms", rooms.toString())
        json.put("bathrooms", bathrooms.toString())
        json.put("notes", notes)
        json.put("petFriendly", petFriendly)
        json.put("furneture", furneture)
        json.put("images", "")
        json.put("api_token", User.instance!!.apiToken)


        Fuel.post(url).body(json.toString()).response { request, response, result ->
            val (data, error) = result
            if ((error == null) && (data!=null)) {
                val res = Property.parseJSON(String(data))
                //if(res) {
                    callback()
                //}else{
                    //Snackbar.make(view!!, R.string.something_wrong, Snackbar.LENGTH_SHORT).show()
                //}
            } else {
                //Snackbar.make(view!!, R.string.bad_request, Snackbar.LENGTH_SHORT).show()
            }
        }

    }

    fun remove(callback: () -> Unit){
        callback()
    }

    companion object {
        fun parseJSON(data: String): List<Property>?{
            var gson = Gson()
            try{
                var propertyList: List<Property> = gson.fromJson(data, object : TypeToken<List<Property>>() {}.type)
                return propertyList
            }catch (ex: Exception){
                val e = ex
            }
            return null
        }

        fun readAll(success: (List<Property>?) -> Unit, error: () -> Unit){
            var properties: List<Property>? = null

            val json = JSONObject()
            json.put("api_token", User.instance!!.apiToken)
            json.put("userId", User.instance!!.id)

            HTTPHelper.makePostRequest(URLS.PROPERTY_READ_ALL, json, { data ->
                properties = parseJSON(data)
                success(properties)
            },{
                error()
            })


        }

        fun readById(id: String, callback: (Property) -> Unit){
            var property: Property? = null
            callback(property!!)
        }
    }
}