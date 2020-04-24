package com.tariod.uphub.domain.usecase.repos

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.tariod.uphub.data.contract.ReposRepository
import com.tariod.uphub.data.database.model.Repository
import com.tariod.uphub.data.preferences.UserPreferences
import com.tariod.uphub.domain.base.ImmutableState
import com.tariod.uphub.domain.contract.GetUserReposUseCase
import com.tariod.uphub.utilities.coroutine.JobComposite
import com.tariod.uphub.utilities.livedata.map
import com.tariod.uphub.utilities.livedata.wrap
import javax.inject.Inject

class GetUserReposUseCaseImpl @Inject constructor(
    val repos: ReposRepository,
    val prefs: UserPreferences
) :
    GetUserReposUseCase {

    override fun getUserRepos(
        jobComposite: JobComposite,
        userId: Int?
    ): LiveData<ImmutableState<PagedList<Repository>>> =
        userId?.let { repos.fetchRepositories(jobComposite, it).wrap() }
            ?: prefs.currentUserId.map { repos.fetchRepositories(jobComposite, it) }
}