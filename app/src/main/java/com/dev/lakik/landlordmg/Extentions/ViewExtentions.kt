package com.dev.lakik.landlordmg.Extentions

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created by ppash on 2/12/2018.
 */

fun Fragment.hideKeyboard() {
    activity.hideKeyboard()
}

fun Activity.hideKeyboard() {
    hideKeyboard(if (currentFocus == null) View(this) else currentFocus)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.showKeyboard() {
    activity.showKeyboard()
}

fun Activity.showKeyboard() {
    showKeyboard(if (currentFocus == null) View(this) else currentFocus)
}

fun Context.showKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 1)
}