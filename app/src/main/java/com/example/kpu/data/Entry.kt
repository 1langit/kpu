package com.example.kpu.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "voters_entry")
data class Entry(
    @PrimaryKey val nik: String,
    val name: String,
    val phone: String,
    val gender: String,
    val date: String,
    var address: String,
    val image: String
) {
    fun matchSearchQuery(query: String): Boolean {
        return listOf(name, nik).any {
            it.contains(query, ignoreCase = true)
        }
    }
}
