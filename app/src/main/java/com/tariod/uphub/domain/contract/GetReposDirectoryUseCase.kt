package com.tariod.uphub.domain.contract

import com.tariod.uphub.data.database.model.DirectoryItem
import com.tariod.uphub.domain.base.ImmutableState
import com.tariod.uphub.utilities.coroutine.JobComposite


interface GetReposDirectoryUseCase {

    fun getDirectories(
        jobComposite: JobComposite,
        reposId: Int,
        sha: String
    ): ImmutableState<List<DirectoryItem>>
}