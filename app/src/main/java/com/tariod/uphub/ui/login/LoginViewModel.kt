package com.tariod.uphub.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tariod.uphub.data.api.ApiException
import com.tariod.uphub.data.preferences.UserPreferences
import com.tariod.uphub.domain.contract.LoginUseCase
import com.tariod.uphub.ui.base.BaseViewModel
import com.tariod.uphub.utilities.livedata.*
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val userPrefs: UserPreferences
) : BaseViewModel() {

    val login = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val signUp = SingleLiveEvent<String>()
    val hideKeyboard = SingleLiveEvent<Unit>()

    private val state = signUp.map {
        loginUseCase.login(
            login.value ?: "",
            password.value ?: "",
            it ?: "",
            jobComposite
        )
    }

    val progress: LiveData<Boolean> = state.switchMap { it.progress }
    val success: LiveData<Boolean> = state.switchMap { it.model }
    val error: LiveData<ApiException> = state.switchMap { it.error }

    val navigateBack = SingleLiveEvent<Unit>()
    val ask2FACode = SingleLiveEvent<Unit>()

    fun onSkipPressed() = navigateBack.click().also { userPrefs.hideLogin = true }

    @JvmOverloads
    fun onSignUp(code: String = "") = signUp.set(code).also { hideKeyboard.click() }

    fun on2FASignUp() = ask2FACode.click()
}