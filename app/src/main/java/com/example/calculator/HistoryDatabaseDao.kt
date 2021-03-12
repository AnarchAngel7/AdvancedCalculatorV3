package com.example.calculator

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(history: History)

    @Query("SELECT * FROM history_table ORDER BY histId")
    fun getLastHistory(): History

    @Query("SELECT * FROM history_table ORDER BY histId")
    fun getHistory(): List<History>

}

