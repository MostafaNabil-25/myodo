package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "phone_table")
data class Phone(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val phoneNumber: String
)
