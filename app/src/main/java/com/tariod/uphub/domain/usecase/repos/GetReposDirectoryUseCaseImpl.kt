package com.tariod.uphub.domain.usecase.repos


import com.tariod.uphub.data.contract.ReposRepository
import com.tariod.uphub.data.database.model.DirectoryItem
import com.tariod.uphub.domain.base.ImmutableState
import com.tariod.uphub.domain.contract.GetReposDirectoryUseCase
import com.tariod.uphub.utilities.coroutine.JobComposite
import javax.inject.Inject

class GetReposDirectoryUseCaseImpl @Inject constructor(private val reposRepos: ReposRepository) :
    GetReposDirectoryUseCase {

    override fun getDirectories(
        jobComposite: JobComposite,
        reposId: Int,
        sha: String
    ): ImmutableState<List<DirectoryItem>> = reposRepos.fetchDirectory(jobComposite, reposId, sha)
}