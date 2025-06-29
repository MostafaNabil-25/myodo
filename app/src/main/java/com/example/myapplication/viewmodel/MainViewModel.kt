package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.Phone
import com.example.myapplication.data.PhoneRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PhoneRepository
    val allPhones: LiveData<List<Phone>>

    init {
        val phoneDao = AppDatabase.getDatabase(application).phoneDao()
        repository = PhoneRepository(phoneDao)
        allPhones = repository.allPhones.asLiveData()
    }

    fun insert(phone: Phone) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(phone)
    }

    fun update(phone: Phone) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(phone)
    }

    fun delete(phone: Phone) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(phone)
    }

    // Function to check if a phone number already exists, returns the Phone object or null
    suspend fun getPhoneByNumber(phoneNumber: String): Phone? {
        return repository.getPhoneByNumber(phoneNumber).firstOrNull()
    }
}

// ViewModel Factory to pass application context to MainViewModel
class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
