package com.tariod.uphub.data.source.user.follower

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.tariod.uphub.data.api.ErrorHandler
import com.tariod.uphub.data.api.github.GithubService
import com.tariod.uphub.data.database.UserDao
import com.tariod.uphub.data.database.model.User
import com.tariod.uphub.utilities.coroutine.JobComposite
import com.tariod.uphub.utilities.livedata.set

class FollowerDataSourceFactory(
    private var userId: Int,
    private val db: UserDao,
    private val service: GithubService,
    private val jobComposite: JobComposite,
    private val errorHandler: ErrorHandler
) : DataSource.Factory<Int, User>() {

    val model = MutableLiveData<FollowerDataSource>()

    override fun create(): DataSource<Int, User> =
        FollowerDataSource(userId, db, service, jobComposite, errorHandler).also { model.set(it) }
}