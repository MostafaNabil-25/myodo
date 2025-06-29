package com.example.myapplication.data

import kotlinx.coroutines.flow.Flow

class PhoneRepository(private val phoneDao: PhoneDao) {

    val allPhones: Flow<List<Phone>> = phoneDao.getAllPhones()

    suspend fun insert(phone: Phone) {
        phoneDao.insertPhone(phone)
    }

    suspend fun update(phone: Phone) {
        phoneDao.updatePhone(phone)
    }

    suspend fun delete(phone: Phone) {
        phoneDao.deletePhone(phone)
    }

    fun getPhoneById(id: Int): Flow<Phone> {
        return phoneDao.getPhoneById(id)
    }

    fun getPhoneByNumber(phoneNumber: String): Flow<Phone?> {
        return phoneDao.getPhoneByNumber(phoneNumber)
    }
}
