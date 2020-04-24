package com.tariod.uphub.domain.usecase.user

import com.tariod.uphub.data.contract.UserRepository
import com.tariod.uphub.data.database.model.User
import com.tariod.uphub.domain.base.PagingState
import com.tariod.uphub.domain.contract.PagingUsersUseCase
import com.tariod.uphub.utilities.coroutine.JobComposite
import javax.inject.Inject

class PagingUsersUseCaseImpl @Inject constructor(val repo: UserRepository) : PagingUsersUseCase {

    override fun fetch(jobComposite: JobComposite, search: String): PagingState<User> =
        repo.fetchMore(jobComposite, search)
}