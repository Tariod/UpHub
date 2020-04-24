package com.tariod.uphub.data.source.user.follower

import com.tariod.uphub.data.api.ErrorHandler
import com.tariod.uphub.data.api.github.GithubService
import com.tariod.uphub.data.database.UserDao
import com.tariod.uphub.data.database.model.User
import com.tariod.uphub.data.source.BaseDataSource
import com.tariod.uphub.utilities.coroutine.JobComposite
import com.tariod.uphub.utilities.livedata.set

class FollowerDataSource(
    private var userId: Int,
    private val db: UserDao,
    private val service: GithubService,
    private val jobComposite: JobComposite,
    errorHandler: ErrorHandler
) : BaseDataSource<User>(errorHandler) {

    override fun loadWithParams(page: Int, perPage: Int, callback: (List<User>, Int?) -> Unit) {
        doJob {
            progress.set(true)
            service.getUserFollowers(db.getUsernameById(userId), page, perPage).await().map {
                it.asGeneralModel()
            }.also {
                db.updateUser(it)
                callback(it, if (it.isEmpty()) null else (page + 1))
            }
            progress.set(false)
        }.handleError {
            doJob {
                db.getFollowers(userId, (page - 1) * perPage, perPage).also {
                    callback(it, if (it.isEmpty()) null else (page + 1))
                }
                progress.set(false)
            }.handleError {
                progress.set(false)
            }.run(jobComposite)
        }.run(jobComposite)
    }
}