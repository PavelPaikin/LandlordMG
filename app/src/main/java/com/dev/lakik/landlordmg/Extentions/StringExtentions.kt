package com.dev.lakik.landlordmg.Extentions

import java.util.regex.Pattern

/**
 * Created by ppash on 2/12/2018.
 */


fun String.ValidEmail() : Boolean{
    if (this.isEmpty()) return false
    if( !Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
    ).matcher(this).matches()) return false

    return true
}

fun String.ValidUserName() : Boolean{
    if (this.isEmpty()) return false

    return true
}

fun String.ValidPassword() : Boolean{
    if (this.isEmpty()) return false
    if (this.length !in 3..16) return false
    return true
}

fun String.ValidRePassword(pass: String) : Boolean{
    if (this.isEmpty()) return false
    if (this.compareTo(pass) != 0) return false

    return true
}

val Boolean.int
    get() = if (this) 1 else 0