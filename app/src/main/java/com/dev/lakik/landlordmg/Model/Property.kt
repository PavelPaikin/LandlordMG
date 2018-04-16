package com.dev.lakik.landlordmg.Model

import com.dev.lakik.landlordmg.Common.URLS
import com.dev.lakik.landlordmg.Helpers.HTTPHelper
import com.google.gson.Gson
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
            url = URLS.PROPERTY_UPDATE
        }

        var propType = "SINGLE_UNIT"
        if(type == PropertyType.MULTI_UNIT) propType = "MULTI_UNIT"

        val json = JSONObject()
        json.put("id", id)
        json.put("parentId", parentId)
        json.put("userId", userId)
        json.put("name", name)
        json.put("type", propType)
        json.put("address1", address1)
        json.put("address2", address2)
        json.put("city", city)
        json.put("province", province)
        json.put("postalCode", postalCode)
        json.put("rooms", rooms.toString())
        json.put("bathrooms", bathrooms.toString())
        json.put("notes", notes)
        json.put("petFriendly", petFriendly)
        json.put("furneture", furneture)
        json.put("images", "")
        json.put("api_token", User.instance!!.apiToken)



        HTTPHelper.makePostRequest(url, json, { data ->
            val res = Property.parseJSON(data)
            if(res !=  null) {
                callback()
            }else{
            }
        },{

        })
    }

    fun remove(callback: () -> Unit){
        callback()
    }

    fun readByParentId(success: (List<Property>?) -> Unit, error: () -> Unit){
        var units: List<Property>? = null

        val json = JSONObject()
        json.put("api_token", User.instance!!.apiToken)
        json.put("userId", User.instance!!.id)
        json.put("parentId", id)

        HTTPHelper.makePostRequest(URLS.PROPERTY_READ_UNITS_BY_PARENT_ID, json, { data ->
            units = parseJSONToList(data)
            success(units)
        },{
            error()
        })
    }

    companion object {
        fun parseJSONToList(data: String): List<Property>?{
            var gson = Gson()
            try{
                var propertyList: List<Property> = gson.fromJson(data, object : TypeToken<List<Property>>() {}.type)
                return propertyList
            }catch (ex: Exception){
                val e = ex
            }
            return null
        }

        fun parseJSON(data: String): Property?{
            var gson = Gson()
            try{
                var property : Property = gson.fromJson(data, object : TypeToken<Property>() {}.type)
                return property
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
                properties = parseJSONToList(data)
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