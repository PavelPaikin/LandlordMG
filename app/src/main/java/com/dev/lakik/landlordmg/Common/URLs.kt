package com.dev.lakik.landlordmg.Common

/**
 * Created by ppash on 1/26/2018.
 */

class URLS(){
    companion object {
        private val MAIN_API_URL = "https://php.scweb.ca/~ppaikin/LandlordAPI/public/"
        val API_SIGN_UP = MAIN_API_URL + "signup"
        var API_LOGIN = MAIN_API_URL + "login"

        var PROPERTY_CREATE = MAIN_API_URL + "property/create"
        var PROPERTY_UPDATE = MAIN_API_URL + "property/update"
        var PROPERTY_READ_ALL = MAIN_API_URL + "property/readAll"
        var PROPERTY_READ_UNITS_BY_PARENT_ID = MAIN_API_URL + "property/readByParentId"

        var LEASE_CREATE = MAIN_API_URL + "lease/create"
        var LEASE_UPDATE = MAIN_API_URL + "lease/update"
    }
}