package com.dev.lakik.landlordmg.Model

import java.io.Serializable

class Tenant(var id: String,
             var userId: String,
             var name: String,
             var phoneList: List<String>,
             var emailsList: List<String>,
             var imgUrl: String,
             var isActive: Boolean): Serializable {

}