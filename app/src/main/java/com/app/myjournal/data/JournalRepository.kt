package com.app.myjournal.data

import kotlinx.coroutines.flow.Flow

class JournalRepository(private val journalDao: JournalDao) {
    val allEntries: Flow<List<JournalEntry>> = journalDao.getAllEntries()

    suspend fun insert(entry: JournalEntry) {
        journalDao.insertEntry(entry)
    }

    fun getEntryByIdFlow(id: Long): Flow<JournalEntry?> {
        return journalDao.getEntryByIdFlow(id)
    }

    suspend fun getEntryById(id: Long): JournalEntry? {
        return journalDao.getEntryById(id)
    }

    suspend fun update(entry: JournalEntry) {
        journalDao.updateEntry(entry)
    }

    suspend fun delete(entry: JournalEntry) {
        journalDao.deleteEntry(entry)
    }
}
