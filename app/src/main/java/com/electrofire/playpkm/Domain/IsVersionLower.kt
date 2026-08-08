package com.electrofire.playpkm.Domain

fun isVersionLower(current: String, min: String): Boolean {
    val c = current.split(".").map { it.toInt() }
    val m = min.split(".").map { it.toInt() }

    for (i in 0 until maxOf(c.size, m.size)) {
        val cv = c.getOrElse(i) { 0 }
        val mv = m.getOrElse(i) { 0 }
        if (cv < mv) return true
        if (cv > mv) return false
    }
    return false
}
