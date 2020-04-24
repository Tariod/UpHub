package com.tariod.uphub.domain.usecase.repos


import com.tariod.uphub.data.contract.ReposRepository
import com.tariod.uphub.data.database.model.Branch
import com.tariod.uphub.domain.base.ImmutableState
import com.tariod.uphub.domain.contract.GetReposBranchesUseCase
import com.tariod.uphub.utilities.coroutine.JobComposite
import javax.inject.Inject

class GetReposBranchesUseCaseImpl @Inject constructor(val repository: ReposRepository) :
    GetReposBranchesUseCase {

    override fun fetchBranches(
        jobComposite: JobComposite,
        reposId: Int
    ): ImmutableState<List<Branch>> = repository.fetchBranches(jobComposite, reposId)
}