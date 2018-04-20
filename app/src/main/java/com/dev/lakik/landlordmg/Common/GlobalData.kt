package com.dev.lakik.landlordmg.Common

import com.dev.lakik.landlordmg.Model.Property

class GlobalData{
    companion object {
        var mainActiveFragment: String = ""
        var subActiveFragment: String = ""

        var selectedProperty: Property? = null
    }
}