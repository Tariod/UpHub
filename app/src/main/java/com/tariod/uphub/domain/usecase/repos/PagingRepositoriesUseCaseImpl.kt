package com.tariod.uphub.domain.usecase.repos

import com.tariod.uphub.data.contract.ReposRepository
import com.tariod.uphub.domain.contract.PagingRepositoriesUseCase
import com.tariod.uphub.utilities.coroutine.JobComposite
import javax.inject.Inject

class PagingRepositoriesUseCaseImpl @Inject constructor(private val repository: ReposRepository
) : PagingRepositoriesUseCase {

    override fun fetch(jobComposite: JobComposite, search: String) =
            repository.fetchMore(jobComposite, search)
}