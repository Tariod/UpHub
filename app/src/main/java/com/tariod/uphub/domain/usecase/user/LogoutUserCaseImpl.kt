package com.tariod.uphub.domain.usecase.user

import com.tariod.uphub.data.contract.UserRepository
import com.tariod.uphub.domain.contract.LogoutUseCase
import javax.inject.Inject

class LogoutUserCaseImpl @Inject constructor(private val userRepos: UserRepository) :
    LogoutUseCase {

    override fun logout() = userRepos.logout()
}