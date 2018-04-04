package com.dev.lakik.landlordmg.Helpers

import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import com.dev.lakik.landlordmg.Common.URLS
import com.dev.lakik.landlordmg.Model.User
import com.dev.lakik.landlordmg.R
import com.github.kittinunf.fuel.Fuel
import org.json.JSONObject

class HTTPHelper{

    companion object {
        fun makePostRequest(url: String, json: JSONObject, succed: (String) -> Unit, error: () -> Unit){
            Fuel.post(url).body(json.toString()).response { request, response, result ->
                val (data, error) = result
                if (error == null) {
                    succed(String(data!!))
                } else {
                    error()
                }
            }
        }
    }
}