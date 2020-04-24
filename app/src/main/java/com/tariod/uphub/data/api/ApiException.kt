package com.tariod.uphub.data.api

import com.tariod.uphub.R


class ApiException(val code: Int, cause: Throwable? = null) : Exception(cause) {

    companion object {
        const val UNKNOWN_CODE = -34
        const val UN_AUTH = 43
        const val AUTH_LIMIT = 12
        const val NO_CONNECTION = 12
    }

    val alert = when (code) {
        UN_AUTH -> R.string.login_error_creds
        NO_CONNECTION -> R.string.common_error_no_connection
        else -> R.string.common_error_smth_went_wrong
    }
}