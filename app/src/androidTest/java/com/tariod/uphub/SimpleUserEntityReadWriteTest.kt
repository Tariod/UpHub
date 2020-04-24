package com.tariod.uphub

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tariod.uphub.data.database.Database
import com.tariod.uphub.data.database.UserDao
import com.tariod.uphub.data.database.model.User
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SimpleUserEntityReadWriteTest {
    private lateinit var userDao: UserDao
    private lateinit var db: Database

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, Database::class.java
        ).build()
        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndRead() {
        val user: User = User(
            1, "login", "name", "company", "location", "email", "bio", 0, 0, 0, "avatarUrl"
        )
        userDao.insert(user)
        val byId = userDao.getUser(1)
        val byLogin = userDao.searchPattern("log").first()
        val byName = userDao.searchPattern("nam").first()
        Assert.assertEquals(user, byId)
        Assert.assertEquals(user, byLogin)
        Assert.assertEquals(user, byName)
    }
}