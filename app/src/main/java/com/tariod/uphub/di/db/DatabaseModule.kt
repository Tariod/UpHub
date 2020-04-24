package com.tariod.uphub.di.db

import android.content.Context
import androidx.room.Room
import com.tariod.uphub.data.database.Database
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    companion object {
        private const val DB_NAME = "uphub-db"
    }

    @Provides
    @Singleton
    fun provideDatabase(context: Context): Database =
        Room.databaseBuilder(context, Database::class.java, DB_NAME)
            .fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideRepositoryDao(db: Database) = db.repositoryDao()

    @Provides
    @Singleton
    fun provideUserDao(db: Database) = db.userDao()

    @Provides
    @Singleton
    fun provideDirectoryDao(db: Database) = db.directoryDao()
}