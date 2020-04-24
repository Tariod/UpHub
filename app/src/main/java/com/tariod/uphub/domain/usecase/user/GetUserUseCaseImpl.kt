package com.tariod.uphub.domain.usecase.user

import androidx.lifecycle.LiveData
import com.tariod.uphub.data.contract.UserRepository
import com.tariod.uphub.data.database.model.User
import com.tariod.uphub.domain.base.ImmutableState
import com.tariod.uphub.domain.contract.GetUserUseCase
import com.tariod.uphub.utilities.coroutine.JobComposite
import com.tariod.uphub.utilities.livedata.map
import com.tariod.uphub.utilities.livedata.wrap

import javax.inject.Inject

class GetUserUseCaseImpl @Inject constructor(
    val repos: UserRepository
) : GetUserUseCase {

    override fun getUser(jobComposite: JobComposite, userId: Int?): LiveData<ImmutableState<User>> =
        userId?.let { repos.getUser(it, jobComposite).wrap() } ?: repos.getCurrentUserId()
            .map { repos.getUser(it, jobComposite) }
}