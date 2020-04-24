package com.tariod.uphub.domain.contract

import com.tariod.uphub.data.database.model.Repository
import com.tariod.uphub.domain.base.PagingState
import com.tariod.uphub.utilities.coroutine.JobComposite


interface PagingRepositoriesUseCase {

    fun fetch(jobComposite: JobComposite, search: String = ""): PagingState<Repository>
}