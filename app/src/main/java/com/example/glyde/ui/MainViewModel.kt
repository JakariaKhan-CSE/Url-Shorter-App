package com.example.glyde.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.glyde.data.UrlRepository
import com.example.glyde.data.local.AppDatabase
import com.example.glyde.data.local.HistoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UrlRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = UrlRepository(database.historyDao())
    }

    val history: Flow<List<HistoryEntity>> = repository.getHistory()

    fun shortenUrl(url: String, onResult: (Result<String>) -> Unit) {
        viewModelScope.launch {
            val result = repository.shortenUrl(url)
            onResult(result)
        }
    }
}
