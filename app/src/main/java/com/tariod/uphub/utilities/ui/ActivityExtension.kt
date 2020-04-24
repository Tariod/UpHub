package com.tariod.uphub.utilities.ui

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Activity.col(@ColorRes resId: Int) = resources.getColor(resId)

fun Activity.str(@StringRes resId: Int) = resources.getString(resId)

fun Fragment.str(@StringRes resId: Int) = resources.getString(resId)

fun Fragment.str(@StringRes resId: Int, vararg values: Any) = resources.getString(resId, *values)

fun View.col(@ColorRes resId: Int) = resources.getColor(resId)

fun Activity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}