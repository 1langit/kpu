package com.example.kpu.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kpu.data.EntryDao

class AppViewModelFactory(private val entryDao: EntryDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(entryDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}