package com.tariod.uphub.utilities

fun <T> guard(dangerTask: () -> T): T? = try {
    dangerTask()
} catch (e: Exception) {
    e.printStackTrace()
    null
}

fun mergeMembers(vararg a: String?) = a.find { it?.isNotBlank() ?: false } ?: ""

fun mergeMembers(vararg a: Int) = a.max() ?: 0

fun mergeMembers(vararg a: Boolean) = a.fold(false) { acc, it -> acc or it }