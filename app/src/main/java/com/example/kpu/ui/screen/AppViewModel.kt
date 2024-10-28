package com.example.kpu.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kpu.data.Entry
import com.example.kpu.data.EntryDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(private val entryDao: EntryDao) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _entries = MutableStateFlow<List<Entry>>(emptyList())
    val entries = searchText
        .combine(_entries) { text, entries ->
            if (text.isBlank()) {
                entries
            } else {
                entries.filter {
                    it.matchSearchQuery(text)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _entries.value
        )

    init {
        viewModelScope.launch {
            _entries.value = entryDao.getEntries()
        }
    }

    fun addEntry(entry: Entry, callback: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            try {
                entryDao.insertEntry(entry)
                _entries.value += entry
                callback(Result.success(Unit))
            } catch (e: Exception) {
                callback(Result.failure(e))
            }
        }
    }

    fun deleteEntry(entry: Entry, callback: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            try {
                entryDao.delete(entry)
                _entries.value = _entries.value.filter { it.nik != entry.nik }
                callback(Result.success(Unit))
            } catch (e: Exception) {
                callback(Result.failure(e))
            }
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun loginUser(username: String, password: String, callback: (Result<Unit>) -> Unit) {
        if (username == "petugas" && password == "admin") {
            callback(Result.success(Unit))
        } else {
            callback(Result.failure(Exception()))
        }
    }
}