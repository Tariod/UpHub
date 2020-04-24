package com.tariod.uphub.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tariod.uphub.data.database.model.User
import com.tariod.uphub.data.database.model.UserFollowing

@Dao
abstract class UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(user: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertFollowing(following: UserFollowing)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertFollowing(following: List<UserFollowing>)

    @Query("SELECT * FROM user WHERE id = :id")
    abstract fun listen(id: Int): LiveData<User>

    @Query("SELECT login FROM user WHERE id = :id")
    abstract fun getUsernameById(id: Int): String

    // Global GET
    @Query("SELECT * FROM user")
    abstract fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE id = :id")
    abstract fun getUser(id: Int): User?

    // Paging GET
    @Query("SELECT * FROM user WHERE id IN (SELECT followerId FROM userFollowing WHERE userId = :id) ORDER BY id DESC LIMIT :limit OFFSET :offset")
    abstract fun getFollowers(id: Int, offset: Int, limit: Int): List<User>

    @Query("SELECT * FROM user WHERE id IN (SELECT userId FROM userFollowing WHERE followerId = :id) ORDER BY id DESC LIMIT :limit OFFSET :offset")
    abstract fun getSubs(id: Int, offset: Int, limit: Int): List<User>

    // Search
    @Query("SELECT * FROM user WHERE (name LIKE '%' || :search || '%') OR (login LIKE '%' || :search || '%') ORDER BY followers DESC")
    abstract fun searchPattern(search: String): List<User>

    fun searchUser(search: String): List<User> =
        if (search.isNotEmpty()) searchPattern(search) else getAll()

    // Update
    fun updateUser(newUser: User) {
        insert(getUser(newUser.id)?.let { newUser.merge(it) } ?: newUser)
    }

    fun updateUser(newUsers: List<User>) {
        insert(newUsers.map { newUser ->
            getUser(newUser.id)?.let { newUser.merge(it) } ?: newUser
        })
    }
}