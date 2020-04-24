package com.tariod.uphub.data.contract

import androidx.paging.PagedList
import com.tariod.uphub.data.database.model.Branch
import com.tariod.uphub.data.database.model.DirectoryItem
import com.tariod.uphub.data.database.model.Repository
import com.tariod.uphub.domain.base.ImmutableState
import com.tariod.uphub.domain.base.PagingState
import com.tariod.uphub.utilities.coroutine.JobComposite

interface ReposRepository {
    /**
     * Downloads next page of repositories with a lastId. Stores models in the database
     */
    fun fetchMore(
        jobComposite: JobComposite,
        search: String = "",
        nextPage: Int = 1,
        nextId: String? = null,
        previousState: PagingState<Repository>? = null
    ): PagingState<Repository>

    fun fetchRepositories(
        jobComposite: JobComposite,
        userId: Int
    ): ImmutableState<PagedList<Repository>>

    fun fetchDirectory(
        jobComposite: JobComposite,
        reposId: Int,
        sha: String
    ): ImmutableState<List<DirectoryItem>>

    fun fetchBranches(jobComposite: JobComposite, reposId: Int): ImmutableState<List<Branch>>
}