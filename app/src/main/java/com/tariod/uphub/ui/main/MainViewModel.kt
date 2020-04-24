package com.tariod.uphub.ui.main

import androidx.lifecycle.MutableLiveData
import com.tariod.uphub.data.preferences.UserPreferences
import com.tariod.uphub.ui.base.BaseViewModel
import com.tariod.uphub.utilities.livedata.SingleLiveEvent
import com.tariod.uphub.utilities.livedata.click
import com.tariod.uphub.utilities.livedata.default
import javax.inject.Inject

class MainViewModel @Inject constructor(private val userPrefs: UserPreferences) : BaseViewModel() {

    companion object {

        const val STATE_REPOSITORY = 54
        const val STATE_PROFILE = 23
    }

    val navigateToLogin = SingleLiveEvent<Unit>()

    val currentFragment = MutableLiveData<Int>().default(STATE_REPOSITORY)

    fun checkNavigation() {
        if (!userPrefs.hideLogin) {
            navigateToLogin.click()
        }
    }

}