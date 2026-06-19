package com.nyx.chat.di

import android.content.Context
import androidx.room.Room
import com.nyx.chat.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Red Team AI — Hilt module providing the Room database.
 * Network/Retrofit is built dynamically per provider inside ChatRepository.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "redteam-database"
        ).fallbackToDestructiveMigration()
         .build()
    }
}
