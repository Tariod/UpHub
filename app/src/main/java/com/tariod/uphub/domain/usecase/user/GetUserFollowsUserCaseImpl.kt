package com.tariod.uphub.domain.usecase.user

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.tariod.uphub.data.contract.UserRepository
import com.tariod.uphub.data.database.model.User
import com.tariod.uphub.data.preferences.UserPreferences
import com.tariod.uphub.domain.base.ImmutableState
import com.tariod.uphub.domain.contract.GetUserFollowsUseCase
import com.tariod.uphub.utilities.coroutine.JobComposite
import com.tariod.uphub.utilities.livedata.map
import com.tariod.uphub.utilities.livedata.wrap
import javax.inject.Inject

class GetUserFollowsUserCaseImpl @Inject constructor(
    val repos: UserRepository,
    val prefs: UserPreferences
) :
    GetUserFollowsUseCase {

    override fun getUserFollows(
        jobComposite: JobComposite,
        userId: Int?
    ): LiveData<ImmutableState<PagedList<User>>> =
        userId?.let { repos.getFollowers(it, jobComposite).wrap() }
            ?: prefs.currentUserId.map { repos.getFollowers(it, jobComposite) }
}