package com.example.calculator

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
data class History(
    @PrimaryKey(autoGenerate = true)
    var histId: Int = 0,

    @ColumnInfo(name = "equation")
    var equation: String = ""

)