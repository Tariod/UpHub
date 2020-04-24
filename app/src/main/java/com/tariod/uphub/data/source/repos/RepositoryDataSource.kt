package com.tariod.uphub.data.source.repos

import com.tariod.uphub.data.api.ErrorHandler
import com.tariod.uphub.data.api.github.GithubService
import com.tariod.uphub.data.database.RepositoryDao
import com.tariod.uphub.data.database.UserDao
import com.tariod.uphub.data.database.model.Repository
import com.tariod.uphub.data.source.BaseDataSource
import com.tariod.uphub.utilities.coroutine.JobComposite
import com.tariod.uphub.utilities.livedata.set

class RepositoryDataSource(
    private var userId: Int,
    private val userDb: UserDao,
    private val db: RepositoryDao,
    private val service: GithubService,
    private val jobComposite: JobComposite,
    errorHandler: ErrorHandler
) : BaseDataSource<Repository>(errorHandler) {

    override fun loadWithParams(
        page: Int,
        perPage: Int,
        callback: (List<Repository>, Int?) -> Unit
    ) {
        doJob {
            progress.set(true)
            service.getUserRepositories(userDb.getUsernameById(userId), page, perPage).await().map {
                it.asGeneralModel()
            }.also {
                val repos = it.map { it.first }
                db.insert(repos)
                userDb.updateUser(it.map { it.second })
                callback(repos, if (it.isEmpty()) null else (page + 1))
            }
            progress.set(false)
        }.handleError {
            doJob {
                db.getUserRepos(userId, (page - 1) * perPage, perPage).also {
                    callback(it, if (it.isEmpty()) null else (page + 1))
                }
                progress.set(false)
            }.handleError {
                progress.set(false)
            }.run(jobComposite)
        }.run(jobComposite)
    }
}