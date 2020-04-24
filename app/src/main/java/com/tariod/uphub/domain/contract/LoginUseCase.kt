package com.tariod.uphub.domain.contract

import com.tariod.uphub.domain.base.MutableState
import com.tariod.uphub.utilities.coroutine.JobComposite


interface LoginUseCase {

    fun login(login: String, password: String, code: String, jobComposite: JobComposite): State

    class State : MutableState<Boolean>()
}