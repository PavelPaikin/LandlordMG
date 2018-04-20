package com.dev.lakik.landlordmg.Model

import com.dev.lakik.landlordmg.Common.URLS
import com.dev.lakik.landlordmg.Helpers.HTTPHelper
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.io.Serializable
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import com.google.gson.JsonObject
import com.google.gson.JsonElement
import kotlin.collections.ArrayList


class Lease(var id: String,
            var userId: String,
            var unitId: String,
            var startDate: Calendar,
            var endDate: Calendar,
            var rentCost: Double,
            var nextPayday: Calendar,
            var period: String,
            var oneTimeChargeList: List<OneTimeCharge>,
            var securityDepositList: List<securityDeposit>,
            var isActive: Boolean): Serializable {

    var unitName: String? = null
    var buildingName: String? = null
    var img: String? = null

    class OneTimeCharge (var title: String,
                         var amount: Double) :Serializable{
        constructor():this("", 0.0)
    }


    class securityDeposit (var title: String,
                           var amount: Double) :Serializable{
        constructor():this("", 0.0)
    }


    fun save(callback: () -> Unit){

        val gson = Gson()

        var url = ""
        if(id.isEmpty()){
            url = URLS.LEASE_CREATE
        }else{
            url = URLS.LEASE_UPDATE
        }

        val json = JSONObject()
        json.put("id", id)
        json.put("userId", userId)
        json.put("unitId", unitId)
        json.put("startDate", SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).format(startDate.time))
        json.put("endDate", SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).format(endDate.time))
        json.put("rentCost", rentCost)
        json.put("nextPayday", SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).format(nextPayday.time))
        json.put("period", period)
        json.put("oneTimeChargeList", gson.toJson(oneTimeChargeList))
        json.put("securityDepositList", gson.toJson(securityDepositList))
        json.put("isActive", isActive)
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

    fun close(callback: () -> Unit){

        val json = JSONObject()
        json.put("id", id)
        json.put("api_token", User.instance!!.apiToken)

        HTTPHelper.makePostRequest(URLS.LEASE_CLOSE, json, { data ->
            callback()
        }, {

        })
    }

    fun delete(callback: () -> Unit){

        val json = JSONObject()
        json.put("id", id)
        json.put("api_token", User.instance!!.apiToken)

        HTTPHelper.makePostRequest(URLS.LEASE_DELETE, json, { data ->
            callback()
        }, {

        })
    }

    constructor() : this(
            id = "",
            userId = User.instance!!.id,
            unitId = "",
            startDate = Calendar.getInstance(),
            endDate = Calendar.getInstance(),
            rentCost = 0.0,
            nextPayday = Calendar.getInstance(),
            period = "",
            oneTimeChargeList = ArrayList<OneTimeCharge>(),
            securityDepositList = ArrayList<securityDeposit>(),
            isActive = true
    )

    companion object {
        fun readAllActive(success: (ArrayList<Lease>) -> Unit, error: () -> Unit){
            var leases: ArrayList<Lease>? = null

            val json = JSONObject()
            json.put("api_token", User.instance!!.apiToken)
            json.put("userId", User.instance!!.id)

            HTTPHelper.makePostRequest(URLS.LEASE_READ_ALL_ACTIVE, json, { data ->
                leases = Lease.parseJSONToList(data)
                success(leases!!)
            },{
                error()
            })
        }

        fun readAllClosed(success: (ArrayList<Lease>) -> Unit, error: () -> Unit){
            var leases: ArrayList<Lease>? = null

            val json = JSONObject()
            json.put("api_token", User.instance!!.apiToken)
            json.put("userId", User.instance!!.id)

            HTTPHelper.makePostRequest(URLS.LEASE_READ_ALL_CLOSED, json, { data ->
                leases = Lease.parseJSONToList(data)
                success(leases!!)
            },{
                error()
            })
        }

        fun readByUnitId(id: String, success: (Lease?) -> Unit, error: () -> Unit){
            var lease: Lease? = null

            val json = JSONObject()
            json.put("api_token", User.instance!!.apiToken)
            json.put("unitId", id)

            HTTPHelper.makePostRequest(URLS.LEASE_READ_BY_UNIT_ID, json, { data ->
                lease = Lease.parseJSON(data)
                success(lease)
            },{
                error()
            })
        }

        fun parseJSONToList(data: String): ArrayList<Lease>{
            var gson = Gson()
            var leaseList = ArrayList<Lease>()
            try{
                val elements = gson.fromJson(data, JsonArray::class.java)
                for(item in elements){
                    var lease = Lease.parseJSON(item.toString())
                    if(lease!=null){
                        leaseList.add(lease)
                    }
                }
                return leaseList
            }catch (ex: Exception){
                val e = ex
            }
            return leaseList
        }

        fun parseJSON(data: String): Lease?{
            var gson = Gson()
            var lease = Lease()
            try{
                val element = gson.fromJson(data, JsonElement::class.java)
                val jsonObj = element.getAsJsonObject()
                lease.id = jsonObj.get("id").asString
                lease.userId = jsonObj.get("userId").asString
                lease.unitId = jsonObj.get("unitId").asString

                lease.startDate.time = SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).parse(jsonObj.get("startDate").asString)
                lease.endDate.time = SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).parse(jsonObj.get("endDate").asString)
                lease.rentCost = jsonObj.get("rentCost").asDouble
                lease.nextPayday.time = SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).parse(jsonObj.get("nextPayday").asString)
                lease.period = jsonObj.get("period").asString
                lease.oneTimeChargeList = gson.fromJson(jsonObj.get("oneTimeChargeList").asString, object : TypeToken<List<OneTimeCharge>>() {}.type)
                lease.securityDepositList = gson.fromJson(jsonObj.get("securityDepositList").asString, object : TypeToken<List<securityDeposit>>() {}.type)
                lease.isActive = jsonObj.get("isActive").asBoolean

                if(jsonObj.get("unitName")!= null && !jsonObj.get("unitName").isJsonNull){
                    lease.unitName = jsonObj.get("unitName").asString
                }

                if(jsonObj.get("buildingName")!= null && !jsonObj.get("buildingName").isJsonNull){
                    lease.buildingName = jsonObj.get("buildingName").asString
                }

                if(jsonObj.get("img")!= null && !jsonObj.get("img").isJsonNull){
                    lease.img = jsonObj.get("img").asString
                }

                return lease
            }catch (ex: Exception){
                val e = ex
            }
            return null
        }
    }

}
