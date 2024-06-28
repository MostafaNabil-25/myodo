package com.example.myapplication

import android.app.AlertDialog
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    lateinit var listView: ListView
    var dbHelper: DBHelper = DBHelper(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val buttonf = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        buttonf.setOnClickListener {
            insert(it)
        }
        listView = findViewById(R.id.listView)
        var button = findViewById<FloatingActionButton>(R.id.button)

        listView.setOnItemClickListener { parent, view, position, id ->
            var insert_name = EditText(this)
            var itemValue = listView.getItemAtPosition(position) as String
            var dialog = AlertDialog.Builder(view.context)
            dialog.setTitle("Update")
            dialog.setView(insert_name)
            //end
            dialog.setPositiveButton("Update"){dialog, id ->
                if(insert_name.text.toString().isEmpty()){
                    Toast.makeText(this, "Please insert phone number", Toast.LENGTH_SHORT).show()
            }else{
                dbHelper.update(insert_name.text.toString(), itemValue)
                    Toast.makeText(this, "Update success", Toast.LENGTH_SHORT).show()
                }
        }
            dialog.setNegativeButton("Delete"){dialog, id ->
                try {
                    dbHelper.delete(itemValue)
                    Toast.makeText(this, "Delete success", Toast.LENGTH_SHORT).show()
                }catch (e:SQLiteException){
                     Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
                }

            }
            dialog.show()
    }


        button.setOnClickListener {
            show(it)
        }


    }
    fun show(view: View){
        var phoneList : List<String> = dbHelper.viewPhone()
        var myListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, phoneList)
        listView.adapter = myListAdapter
    }

    fun insert(view: View){
        var db = DBHelper(this)
        var insert_Phone = EditText(this)
        var dialog = AlertDialog.Builder(view.context)
        dialog.setTitle("Insert")
        dialog.setView(insert_Phone)
        dialog.setPositiveButton("Insert"){dialog, id ->
          if(insert_Phone.text.toString().isEmpty()){
              Toast.makeText(this, "Please insert phone number", Toast.LENGTH_SHORT).show()
          }else{
              db.insertData(insert_Phone.text.toString())
              Toast.makeText(this, "Insert success", Toast.LENGTH_SHORT).show()
          }
        }
        dialog.setNegativeButton("Cancel"){dialog, id ->
            dialog.cancel()
        }
        dialog.show()

    }
}