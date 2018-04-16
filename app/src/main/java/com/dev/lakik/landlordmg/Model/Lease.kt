package com.dev.lakik.landlordmg.Model

import com.dev.lakik.landlordmg.Common.URLS
import com.dev.lakik.landlordmg.Helpers.HTTPHelper
import com.google.gson.Gson
import org.json.JSONObject
import java.io.Serializable
import java.util.*

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

    class OneTimeCharge (var title: String,
                         var amount: Double){
        constructor():this("", 0.0)
    }


    class securityDeposit (var title: String,
                           var amount: Double){
        constructor():this("", 0.0)
    }


    fun save(callback: () -> Unit){

        val gson = Gson()

        var url = ""
        if(id.isEmpty()){
            url = URLS.PROPERTY_CREATE
        }else{
            url = URLS.PROPERTY_UPDATE
        }

        val json = JSONObject()
        json.put("id", id)
        json.put("userId", userId)
        json.put("unitId", unitId)
        json.put("startDate", startDate)
        json.put("endDate", endDate)
        json.put("rentCost", rentCost)
        json.put("nextPayday", nextPayday)
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

}
