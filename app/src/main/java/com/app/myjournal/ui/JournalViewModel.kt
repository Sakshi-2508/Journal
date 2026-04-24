package com.app.myjournal.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.myjournal.data.JournalEntry
import com.app.myjournal.data.JournalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class JournalViewModel(private val repository: JournalRepository) : ViewModel() {

    val allEntries: StateFlow<List<JournalEntry>> = repository.allEntries
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insert(entry: JournalEntry) {
        viewModelScope.launch {
            repository.insert(entry)
        }
    }

    fun updateEntry(entry: JournalEntry) {
        viewModelScope.launch {
            repository.update(entry)
        }
    }

    fun deleteEntry(entry: JournalEntry) {
        viewModelScope.launch {
            repository.delete(entry)
        }
    }

    fun getEntryById(id: Long): Flow<JournalEntry?> {
        return repository.getEntryByIdFlow(id)
    }

    class Factory(private val repository: JournalRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(JournalViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return JournalViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
