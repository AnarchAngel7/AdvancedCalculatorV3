package com.example.calculator

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDatabaseDao {

    @Insert
    fun insert(history: History)

    @Query("SELECT * FROM history_table ORDER BY histId DESC LIMIT 1")
    fun getHistory(): History?

}