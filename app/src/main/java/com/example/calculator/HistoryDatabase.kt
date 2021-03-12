package com.example.calculator

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(entities = [History::class], version = 5, exportSchema = false)
abstract class HistoryDatabase : RoomDatabase() {

    abstract val historyDatabaseDao: HistoryDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: HistoryDatabase? = null
        private const val NUMBER_OF_THREADS = 1
        val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

        fun getInstance(context: Context): HistoryDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        HistoryDatabase::class.java,
                        "history_table"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}