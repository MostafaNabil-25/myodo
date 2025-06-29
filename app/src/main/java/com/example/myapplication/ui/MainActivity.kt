package com.example.myapplication.ui

import android.app.AlertDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.Phone
import com.example.myapplication.viewmodel.MainViewModel
import com.example.myapplication.viewmodel.MainViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var phoneListAdapter: PhoneListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize ViewModel
        val viewModelFactory = MainViewModelFactory(application)
        mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerView) // Assumes R.id.recyclerView in activity_main.xml
        phoneListAdapter = PhoneListAdapter { phone ->
            // Handle item click - show update/delete dialog
            showUpdateDeleteDialog(phone)
        }
        recyclerView.adapter = phoneListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Observe LiveData
        mainViewModel.allPhones.observe(this) { phones ->
            phones?.let { phoneListAdapter.submitList(it) }
        }

        // Setup FAB for inserting new phone number
        val fabInsert = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fabInsert.setOnClickListener {
            showInsertDialog()
        }

        // Setup "show/refresh" button - its role is now implicitly handled by LiveData
        // We can repurpose it or remove it. For now, let's make it a explicit refresh.
        val buttonRefresh = findViewById<FloatingActionButton>(R.id.button) // Assuming this is the "show" button
        buttonRefresh.setOnClickListener {
            // LiveData updates automatically, but if you wanted an explicit refresh:
            // mainViewModel.refreshPhones() // You'd need to implement this in ViewModel
            Toast.makeText(this, "List is always up-to-date!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showInsertDialog() {
        val editText = EditText(this).apply { hint = "Enter phone number" }
        AlertDialog.Builder(this)
            .setTitle("Add New Phone Number")
            .setView(editText)
            .setPositiveButton("Add") { dialog, _ ->
                val phoneNumberString = editText.text.toString().trim()
                if (phoneNumberString.isNotEmpty()) {
                    lifecycleScope.launch { // Use lifecycleScope for coroutine
                        val existingPhone = mainViewModel.getPhoneByNumber(phoneNumberString)
                        if (existingPhone == null) {
                            mainViewModel.insert(Phone(phoneNumber = phoneNumberString))
                            Toast.makeText(applicationContext, "Phone number added.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Phone number already exists.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Phone number cannot be empty.", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    private fun showUpdateDeleteDialog(phone: Phone) {
        val editText = EditText(this).apply {
            setText(phone.phoneNumber)
            hint = "Enter new phone number"
        }
        AlertDialog.Builder(this)
            .setTitle("Update or Delete Phone Number")
            .setView(editText)
            .setPositiveButton("Update") { dialog, _ ->
                val updatedPhoneNumberString = editText.text.toString().trim()
                if (updatedPhoneNumberString.isNotEmpty()) {
                    lifecycleScope.launch {
                        val existingPhoneWithNewNumber = mainViewModel.getPhoneByNumber(updatedPhoneNumberString)
                        if (existingPhoneWithNewNumber == null || existingPhoneWithNewNumber.id == phone.id) {
                            val updatedPhone = phone.copy(phoneNumber = updatedPhoneNumberString)
                            mainViewModel.update(updatedPhone)
                            Toast.makeText(applicationContext, "Phone number updated.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "New phone number already exists for another contact.", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Phone number cannot be empty.", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Delete") { dialog, _ ->
                mainViewModel.delete(phone)
                Toast.makeText(applicationContext, "Phone number deleted.", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNeutralButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }
}