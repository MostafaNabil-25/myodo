package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PhoneDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPhone(phone: Phone)

    @Update
    suspend fun updatePhone(phone: Phone)

    @Delete
    suspend fun deletePhone(phone: Phone)

    @Query("SELECT * FROM phone_table ORDER BY phoneNumber ASC")
    fun getAllPhones(): Flow<List<Phone>>

    @Query("SELECT * FROM phone_table WHERE id = :id")
    fun getPhoneById(id: Int): Flow<Phone>

    @Query("SELECT * FROM phone_table WHERE phoneNumber = :phoneNumber")
    fun getPhoneByNumber(phoneNumber: String): Flow<Phone?> // To check if a number exists
}
