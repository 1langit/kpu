package com.example.kpu.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface EntryDao {

    @Query("SELECT * FROM voters_entry")
    suspend fun getEntries(): List<Entry>

    @Query("SELECT * FROM voters_entry WHERE nik = :nik")
    suspend fun getEntry(nik: String): Entry

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertEntry(entry: Entry)

    @Update
    suspend fun update(entry: Entry)

    @Delete
    suspend fun delete(entry: Entry)
}