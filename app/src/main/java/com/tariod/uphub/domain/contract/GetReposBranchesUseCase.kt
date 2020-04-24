package com.tariod.uphub.domain.contract

import com.tariod.uphub.data.database.model.Branch
import com.tariod.uphub.domain.base.ImmutableState
import com.tariod.uphub.utilities.coroutine.JobComposite

interface GetReposBranchesUseCase {

    fun fetchBranches(jobComposite: JobComposite, reposId: Int): ImmutableState<List<Branch>>
}