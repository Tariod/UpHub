package com.tariod.uphub.ui.explore.directory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tariod.uphub.domain.contract.GetReposDirectoryUseCase
import com.tariod.uphub.ui.base.BaseViewModel
import com.tariod.uphub.utilities.livedata.map
import com.tariod.uphub.utilities.livedata.set
import com.tariod.uphub.utilities.livedata.switchMap
import com.tariod.uphub.utilities.ui.list.dir.model.DirUI
import javax.inject.Inject

class DirectoryViewModel @Inject constructor(val useCase: GetReposDirectoryUseCase) :
    BaseViewModel() {

    private val fetch = MutableLiveData<Pair<Int, String>>() //ReposId, Sha

    val state = fetch.map { (reposId, sha) -> useCase.getDirectories(jobComposite, reposId, sha) }

    val models: LiveData<List<DirUI>> = state.switchMap {
        it.model.map {
            it.map { DirUI(it) }.let {
                it.filter {
                    it.dir?.isFolder == true
                }.sortedBy { it.dir?.name } + it.filter {
                    it.dir?.isFolder == false
                }.sortedBy { it.dir?.name }
            }
        }
    }

    val progress = state.switchMap { it.progress }

    fun onFetchDirectory(reposId: Int, sha: String) = fetch.set(reposId to sha)
}