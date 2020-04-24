package com.tariod.uphub.domain.usecase.user

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.tariod.uphub.data.contract.UserRepository
import com.tariod.uphub.data.database.model.User
import com.tariod.uphub.domain.base.ImmutableState
import com.tariod.uphub.utilities.coroutine.JobComposite
import com.tariod.uphub.utilities.livedata.map
import com.tariod.uphub.utilities.livedata.wrap
import com.tariod.uphub.domain.contract.GetUserSubscriptionUseCase
import javax.inject.Inject

class GetUserSubscriptionUseCaseImpl @Inject constructor(private val repos: UserRepository) :
    GetUserSubscriptionUseCase {

    override fun getSubscription(
        jobComposite: JobComposite,
        userId: Int?
    ): LiveData<ImmutableState<PagedList<User>>> =
        userId?.let {
            repos.getSubscription(it, jobComposite).wrap()
        } ?: repos.getCurrentUserId().map {
            repos.getSubscription(it, jobComposite)
        }
}