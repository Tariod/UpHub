package com.tariod.uphub.ui.main.profile.subscription

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.tariod.uphub.data.database.model.User
import com.tariod.uphub.domain.base.ImmutableState
import com.tariod.uphub.domain.contract.GetUserSubscriptionUseCase
import com.tariod.uphub.ui.main.profile.base.ProfileBaseListViewModel
import javax.inject.Inject

class ProfileSubscriptionViewModel @Inject constructor(private val useCase: GetUserSubscriptionUseCase) :
    ProfileBaseListViewModel<User>() {

    override fun requestState(userId: Int?): LiveData<ImmutableState<PagedList<User>>> =
        useCase.getSubscription(jobComposite, userId)
}