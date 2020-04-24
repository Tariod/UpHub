package com.tariod.uphub.data.preferences

import android.content.Context
import com.tariod.uphub.utilities.livedata.LiveIntPreference
import com.tariod.uphub.utilities.livedata.LiveStringPreference
import javax.inject.Inject

class UserPreferences @Inject constructor(context: Context) {

    companion object {
        private const val USER_PREFS = "user_prefs"

        private const val SHOW_LOGIN = "show_login"
        private const val CURRENT_USER_LOGIN = "current_user_login"
        private const val CURRENT_USER_ID = "current_user_id"
        private const val CURRENT_USER_PASSWORD_HASH = "current_user_password_hash"
    }

    private val prefs =
        context.applicationContext.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)

    var currentUserLogin = LiveStringPreference(prefs, CURRENT_USER_LOGIN)

    var currentUserId = LiveIntPreference(prefs, CURRENT_USER_ID)

    var currentUserPasswordHash: String
        set(value) = prefs.edit().putString(CURRENT_USER_PASSWORD_HASH, value).apply()
        get() = prefs.getString(CURRENT_USER_PASSWORD_HASH, "")!!

    fun setUserToken(login: String, passwordHash: String, token: String) =
        prefs.edit().putString(getUserLoginKey(login, passwordHash), token).apply()

    fun getUserToken(login: String, passwordHash: String): String =
        prefs.getString(getUserLoginKey(login, passwordHash), "")!!

    var hideLogin: Boolean
        set(value) = prefs.edit().putBoolean(SHOW_LOGIN, value).apply()
        get() = prefs.getBoolean(SHOW_LOGIN, false)

    fun getCurrentToken() = getUserToken(currentUserLogin.getPrefValue(), currentUserPasswordHash)

    private fun getUserLoginKey(login: String, passwordHash: String) =
        "${login}_${passwordHash}_auth_token"
}