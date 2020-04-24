package com.tariod.uphub.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tariod.uphub.data.database.model.*

@Database(
    entities = [Repository::class, User::class, UserFollowing::class, DirectoryItem::class, Branch::class],
    version = 1
)
abstract class Database : RoomDatabase() {

    abstract fun repositoryDao(): RepositoryDao

    abstract fun userDao(): UserDao

    abstract fun directoryDao(): DirectoryDao
}