package com.tariod.uphub.data.contract

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.tariod.uphub.data.database.model.User
import com.tariod.uphub.domain.base.ImmutableState
import com.tariod.uphub.domain.base.PagingState
import com.tariod.uphub.domain.contract.LoginUseCase
import com.tariod.uphub.utilities.coroutine.JobComposite

interface UserRepository {

    fun login(
        login: String,
        password: String,
        code: String,
        jobComposite: JobComposite
    ): LoginUseCase.State

    fun logout()

    fun getFollowers(userId: Int, jobComposite: JobComposite): ImmutableState<PagedList<User>>

    fun getSubscription(userId: Int, jobComposite: JobComposite): ImmutableState<PagedList<User>>

    fun getUser(id: Int, jobComposite: JobComposite): ImmutableState<User>

    fun getCurrentUserId(): LiveData<Int>

    fun fetchMore(
        jobComposite: JobComposite,
        search: String = "",
        nextPage: Int = 1,
        nextId: String? = null,
        previousState: PagingState<User>? = null
    ): PagingState<User>
}