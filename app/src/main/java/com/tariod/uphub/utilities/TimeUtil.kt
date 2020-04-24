package com.tariod.uphub.utilities

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun parseGithubTime(tm: String): Date = guard {
    tm.takeIf { it.isNotBlank() }?.let { SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(it) }
        ?: Date(0)
} ?: Date(0)