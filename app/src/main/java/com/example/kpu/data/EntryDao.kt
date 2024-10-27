package com.example.kpu.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EntryDao {

    @Query("SELECT * FROM voters_entry")
    suspend fun getEntries(): List<Entry>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertEntry(entry: Entry)
}