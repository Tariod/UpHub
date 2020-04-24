package com.tariod.uphub.domain.usecase.user

import com.tariod.uphub.data.contract.UserRepository
import com.tariod.uphub.domain.contract.LoginUseCase
import com.tariod.uphub.utilities.coroutine.JobComposite
import javax.inject.Inject

class LoginUseCaseImpl @Inject constructor(private val userRepository: UserRepository) :
    LoginUseCase {

    override fun login(login: String, password: String, code: String, jobComposite: JobComposite) =
        userRepository.login(login, password, code, jobComposite)
}