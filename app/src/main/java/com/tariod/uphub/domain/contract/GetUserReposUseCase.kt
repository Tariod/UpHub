package com.tariod.uphub.domain.contract

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.tariod.uphub.data.database.model.Repository
import com.tariod.uphub.domain.base.ImmutableState
import com.tariod.uphub.utilities.coroutine.JobComposite


interface GetUserReposUseCase {

    fun getUserRepos(
        jobComposite: JobComposite,
        userId: Int?
    ): LiveData<ImmutableState<PagedList<Repository>>>
}