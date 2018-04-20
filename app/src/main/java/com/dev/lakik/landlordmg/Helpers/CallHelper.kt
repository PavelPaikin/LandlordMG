package com.dev.lakik.landlordmg.Helpers

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity

class CallHelper(var ctx: Activity, var fragment: Fragment){
    var phone: String = ""

    fun makeCall(phone: String){
        this.phone = phone
        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ctx, arrayOf(Manifest.permission.CALL_PHONE), 222)
        } else {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone))
            fragment.startActivity(intent)
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 222) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                makeCall(phone)
            }
        }

    }
}