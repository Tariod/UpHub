package com.tariod.uphub.ui.main.profile.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.tariod.uphub.data.database.model.Repository
import com.tariod.uphub.domain.base.ImmutableState
import com.tariod.uphub.domain.contract.GetUserReposUseCase
import com.tariod.uphub.ui.main.profile.base.ProfileBaseListViewModel
import javax.inject.Inject

class ProfileRepositoryViewModel @Inject constructor(private val useCase: GetUserReposUseCase) :
    ProfileBaseListViewModel<Repository>() {

    override fun requestState(userId: Int?): LiveData<ImmutableState<PagedList<Repository>>> =
        useCase.getUserRepos(jobComposite, userId)
}