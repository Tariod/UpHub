package com.tariod.uphub.domain.contract

import androidx.lifecycle.LiveData
import com.tariod.uphub.data.database.model.User
import com.tariod.uphub.domain.base.ImmutableState
import com.tariod.uphub.utilities.coroutine.JobComposite

interface GetUserUseCase {

    fun getUser(jobComposite: JobComposite, userId: Int? = null): LiveData<ImmutableState<User>>
}