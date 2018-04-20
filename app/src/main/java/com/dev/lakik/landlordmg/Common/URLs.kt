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
        var PROPERTY_DELETE = MAIN_API_URL + "property/delete"
        var PROPERTY_READ_ALL = MAIN_API_URL + "property/readAll"
        var PROPERTY_READ_BY_ID = MAIN_API_URL + "property/readById"
        var PROPERTY_READ_UNITS_BY_PARENT_ID = MAIN_API_URL + "property/readByParentId"

        var LEASE_CREATE = MAIN_API_URL + "lease/create"
        var LEASE_UPDATE = MAIN_API_URL + "lease/update"
        var LEASE_CLOSE = MAIN_API_URL + "lease/close"
        var LEASE_DELETE = MAIN_API_URL + "lease/delete"
        var LEASE_READ_BY_UNIT_ID = MAIN_API_URL + "lease/readByUnitId"
        var LEASE_READ_ALL_ACTIVE = MAIN_API_URL + "lease/readAllActive"
        var LEASE_READ_ALL_CLOSED = MAIN_API_URL + "lease/readAllClosed"

        var TENANT_CREATE = MAIN_API_URL + "tenant/create"
        var TENANT_UPDATE = MAIN_API_URL + "tenant/update"
        var TENANT_DELETE = MAIN_API_URL + "tenant/delete"
        var TENANT_READ_ALL = MAIN_API_URL + "tenant/readAll"
        var TENANT_READ_BY_LEASE_ID = MAIN_API_URL + "tenant/readAllByLeaseId"
    }
}