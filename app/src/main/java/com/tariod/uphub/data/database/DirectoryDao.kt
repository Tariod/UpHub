package com.tariod.uphub.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tariod.uphub.data.database.model.Branch
import com.tariod.uphub.data.database.model.DirectoryItem

@Dao
abstract class DirectoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(repos: DirectoryItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(repos: List<DirectoryItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(branch: Branch)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertBranches(repos: List<Branch>)

    @Query("DELETE FROM directoryitem WHERE parent = :parentSha")
    abstract fun clearDirectory(parentSha: String)

    @Query("DELETE FROM branch WHERE reposId = :reposId")
    abstract fun clearBranches(reposId: Int)

    @Query("SELECT * FROM directoryitem WHERE parent = :parentSha")
    abstract fun listenDirectory(parentSha: String): LiveData<List<DirectoryItem>>

    @Query("SELECT * FROM branch WHERE reposId = :reposId")
    abstract fun listenBranches(reposId: Int): LiveData<List<Branch>>
}