package com.tariod.uphub.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tariod.uphub.data.database.model.Repository

@Dao
abstract class RepositoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(repos: Repository)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(repos: List<Repository>)

    @Query("SELECT * FROM repository")
    abstract fun getAll(): List<Repository>

    @Query("SELECT * FROM repository WHERE (name LIKE '%' || :search || '%') OR (description LIKE '%' || :search || '%') ORDER BY createdAt DESC")
    abstract fun searchPattern(search: String): List<Repository>

    @Query("SELECT * FROM repository WHERE id = :id")
    abstract fun getRepos(id: Int): Repository?

    @Query("SELECT * FROM repository WHERE ownerId = :userId ORDER BY id DESC LIMIT :limit OFFSET :offset")
    abstract fun getUserRepos(userId: Int, offset: Int, limit: Int): List<Repository>

    @Query("SELECT name FROM repository WHERE id = :id")
    abstract fun getNameById(id: Int): String

    @Query("SELECT login FROM user WHERE id = (SELECT ownerId FROM repository WHERE id = :id)")
    abstract fun getOwnerById(id: Int): String

    fun searchRepos(search: String): List<Repository> = if (search.isNotEmpty()) searchPattern(search) else getAll()
}