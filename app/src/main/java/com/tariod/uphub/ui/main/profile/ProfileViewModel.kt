package com.tariod.uphub.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tariod.uphub.data.database.model.User
import com.tariod.uphub.domain.contract.GetUserUseCase
import com.tariod.uphub.domain.contract.LogoutUseCase
import com.tariod.uphub.ui.base.BaseViewModel
import com.tariod.uphub.utilities.livedata.SingleLiveEvent
import com.tariod.uphub.utilities.livedata.click
import com.tariod.uphub.utilities.livedata.set
import com.tariod.uphub.utilities.livedata.switchMap
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val loginUseCase: GetUserUseCase,
    private val logoutUseCase: LogoutUseCase
) : BaseViewModel() {

    private val getUser = MutableLiveData<Int?>()
    val user: LiveData<User> =
        getUser.switchMap { loginUseCase.getUser(jobComposite, it).switchMap { it.model } }
    val navigateToSignIn = SingleLiveEvent<Unit>()
    val navigateToSignOut = SingleLiveEvent<Unit>()

    fun onSignIn() = navigateToSignIn.click()

    fun onUser(userId: Int?) = getUser.set(userId)

    fun onSignOut() = navigateToSignOut.click()

    fun signOut() {
        logoutUseCase.logout()
        navigateToSignIn.click()
    }
}