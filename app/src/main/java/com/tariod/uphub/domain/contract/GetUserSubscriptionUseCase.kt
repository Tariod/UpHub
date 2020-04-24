package com.tariod.uphub.domain.contract

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.tariod.uphub.data.database.model.User
import com.tariod.uphub.domain.base.ImmutableState
import com.tariod.uphub.utilities.coroutine.JobComposite


interface GetUserSubscriptionUseCase {

    fun getSubscription(
        jobComposite: JobComposite,
        userId: Int?
    ): LiveData<ImmutableState<PagedList<User>>>
}