package com.tariod.uphub.data.source.repos

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.tariod.uphub.data.api.ErrorHandler
import com.tariod.uphub.data.api.github.GithubService
import com.tariod.uphub.data.database.RepositoryDao
import com.tariod.uphub.data.database.UserDao
import com.tariod.uphub.data.database.model.Repository
import com.tariod.uphub.utilities.coroutine.JobComposite
import com.tariod.uphub.utilities.livedata.set

class RepositoryDataSourceFactory(
    private var userId: Int,
    private val userDb: UserDao,
    private val db: RepositoryDao,
    private val service: GithubService,
    private val jobComposite: JobComposite,
    private val errorHandler: ErrorHandler
) : DataSource.Factory<Int, Repository>() {

    val model = MutableLiveData<RepositoryDataSource>()

    override fun create(): DataSource<Int, Repository> = RepositoryDataSource(
        userId,
        userDb,
        db,
        service,
        jobComposite,
        errorHandler
    ).also { model.set(it) }
}