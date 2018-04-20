package com.dev.lakik.landlordmg.Model

import android.net.Uri
import android.util.Log
import com.dev.lakik.landlordmg.Common.GlobalData
import com.dev.lakik.landlordmg.Common.URLS
import com.dev.lakik.landlordmg.Helpers.HTTPHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.io.File
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.cloudinary.android.MediaManager
import com.google.gson.JsonArray
import com.google.gson.JsonElement


class Tenant(var id: String,
             var userId: String,
             var leaseId: String,
             var name: String,
             var phones: ArrayList<Phone>,
             var emails: ArrayList<Email>,
             var img: String): Serializable {

    var imageFile: File? = null

    class Phone (var title: String) :Serializable{
        constructor():this("")
    }


    class Email (var title: String) :Serializable{
        constructor():this("")
    }


    fun save(callback: () -> Unit, error: () -> Unit){
        if(imageFile == null) {
            finalSave(callback)
        }else{
            uploadImage(callback, error)
        }
    }

    fun uploadImage(callback: () -> Unit, error: () -> Unit){
        val requestId = MediaManager.get().upload(imageFile!!.absolutePath).callback(object : UploadCallback {
            override fun onStart(requestId: String) {
                // your code here
            }

            override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                // example code starts here
                val progress = bytes.toDouble() / totalBytes
                // post progress to app UI (e.g. progress bar, notification)
                // example code ends here
            }

            override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                // your code here
                Log.d("aaaa", requestId)
                var public_id = resultData.get("public_id").toString()
                img = public_id
                finalSave(callback)
            }

            override fun onError(requestId: String, error: ErrorInfo) {
                // your code here
                Log.d("aaaa", requestId)
                error()
            }

            override fun onReschedule(requestId: String, error: ErrorInfo) {
                // your code here
                error()
            }
        })
                .dispatch()
    }

    private fun finalSave(callback: () -> Unit){

        val gson = Gson()

        var url = ""
        if(id.isEmpty()){
            url = URLS.TENANT_CREATE
        }else{
            url = URLS.TENANT_UPDATE
        }

        val json = JSONObject()
        json.put("id", id)
        json.put("userId", userId)
        json.put("leaseId", GlobalData.selectedProperty!!.lease!!.id)
        json.put("name", name)
        json.put("phones", gson.toJson(phones))
        json.put("emails", gson.toJson(emails))
        json.put("img", img)
        json.put("api_token", User.instance!!.apiToken)

        HTTPHelper.makePostRequest(url, json, { data ->
            val res = Tenant.parseJSON(data)
            if (res != null) {
                callback()
            } else {
            }
        }, {

        })
    }

    constructor() : this(
            id = "",
            userId = User.instance!!.id,
            leaseId = "",
            name = "",
            phones = ArrayList<Phone>(),
            emails = ArrayList<Email>(),
            img = ""
    )

    fun delete(callback: () -> Unit){

        val json = JSONObject()
        json.put("id", id)
        json.put("api_token", User.instance!!.apiToken)

        HTTPHelper.makePostRequest(URLS.TENANT_DELETE, json, { data ->
            callback()
        }, {

        })
    }

    companion object {
        fun parseJSONToList(data: String): ArrayList<Tenant>?{
            var gson = Gson()
            var tenantList = ArrayList<Tenant>()
            try{
                val elements = gson.fromJson(data, JsonArray::class.java)
                for(item in elements){
                    var tenant = parseJSON(item.toString())
                    if(tenant!=null){
                        tenantList.add(tenant)
                    }
                }
                return tenantList
            }catch (ex: Exception){
                val e = ex
            }
            return null
        }

        fun parseJSON(data: String): Tenant?{
            var gson = Gson()
            var tenant = Tenant()
            try{
                val element = gson.fromJson(data, JsonElement::class.java)
                val jsonObj = element.getAsJsonObject()
                tenant.id = jsonObj.get("id").asString
                tenant.userId = jsonObj.get("userId").asString
                tenant.leaseId = jsonObj.get("leaseId").asString
                tenant.name = jsonObj.get("name").asString
                tenant.phones = gson.fromJson(jsonObj.get("phones").asString, object : TypeToken<ArrayList<Phone>>() {}.type)
                tenant.emails = gson.fromJson(jsonObj.get("emails").asString, object : TypeToken<ArrayList<Email>>() {}.type)
                tenant.img = jsonObj.get("img").asString

                return tenant
            }catch (ex: Exception){
                val e = ex
            }
            return null
        }

        fun readAllByLeaseId(leaseId: String, success: (List<Tenant>?) -> Unit, error: () -> Unit){
            var tenants: List<Tenant>? = null

            val json = JSONObject()
            json.put("api_token", User.instance!!.apiToken)
            json.put("leaseId", leaseId)

            HTTPHelper.makePostRequest(URLS.TENANT_READ_BY_LEASE_ID, json, { data ->
                tenants = Tenant.parseJSONToList(data)
                success(tenants)
            },{
                error()
            })
        }

        fun readAll(success: (List<Tenant>?) -> Unit, error: () -> Unit){
            var tenants: List<Tenant>? = null

            val json = JSONObject()
            json.put("api_token", User.instance!!.apiToken)
            json.put("userId", User.instance!!.id)

            HTTPHelper.makePostRequest(URLS.TENANT_READ_ALL, json, { data ->
                tenants = Tenant.parseJSONToList(data)
                success(tenants)
            },{
                error()
            })
        }
    }

}