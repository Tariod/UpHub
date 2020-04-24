package com.tariod.uphub.ui.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tariod.uphub.data.database.model.Branch
import com.tariod.uphub.domain.contract.GetReposBranchesUseCase
import com.tariod.uphub.ui.base.BaseViewModel
import com.tariod.uphub.utilities.livedata.map
import com.tariod.uphub.utilities.livedata.set
import com.tariod.uphub.utilities.livedata.switchMap

import javax.inject.Inject

class ExploreViewModel @Inject constructor(
    private val useCase: GetReposBranchesUseCase
) : BaseViewModel() {

    private val fetchReposId = MutableLiveData<Int>()
    private val state = fetchReposId.map { useCase.fetchBranches(jobComposite, it) }

    val branches: LiveData<List<Branch>> = state.switchMap { it.model }
    val progress: LiveData<Boolean> = state.switchMap { it.progress }

    fun onFetch(reposId: Int) = fetchReposId.set(reposId)
}