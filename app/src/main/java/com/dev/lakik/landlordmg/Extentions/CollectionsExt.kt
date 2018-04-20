package com.dev.lakik.landlordmg.Extentions

fun IntArray.containsOnly(num: Int): Boolean = filter { it == num }.isNotEmpty()