package com.tariod.uphub.ui.main.profile.follower

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.tariod.uphub.data.database.model.User
import com.tariod.uphub.domain.base.ImmutableState
import com.tariod.uphub.domain.contract.GetUserFollowsUseCase
import com.tariod.uphub.ui.main.profile.base.ProfileBaseListViewModel
import javax.inject.Inject

class ProfileFollowerViewModel @Inject constructor(private val useCase: GetUserFollowsUseCase) :
    ProfileBaseListViewModel<User>() {

    override fun requestState(userId: Int?): LiveData<ImmutableState<PagedList<User>>> =
        useCase.getUserFollows(jobComposite, userId)
}