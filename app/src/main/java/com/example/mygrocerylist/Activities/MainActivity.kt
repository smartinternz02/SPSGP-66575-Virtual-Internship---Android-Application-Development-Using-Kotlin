package com.example.mygrocerylist.Activities

import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import com.example.mygrocerylist.Data.DatabaseHandler
import android.os.Bundle
import com.example.mygrocerylist.R
import android.support.design.widget.FloatingActionButton
import com.example.mygrocerylist.Model.Grocery
import android.support.design.widget.Snackbar
import android.content.Intent
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private var dialogBuilder: AlertDialog.Builder? = null
    private lateinit var dialog: AlertDialog
    private var groceryItem: EditText? = null
    private var quantity: EditText? = null
    private var saveButton: Button? = null
    private var db: DatabaseHandler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DatabaseHandler(this)
        byPassActivity()
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { createPopupDialog() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun createPopupDialog() {
        dialogBuilder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.popup, null)
        groceryItem = view.findViewById<View>(R.id.groceryItem) as EditText
        quantity = view.findViewById<View>(R.id.groceryQty) as EditText
        saveButton = view.findViewById<View>(R.id.saveButton) as Button
        dialogBuilder!!.setView(view)
        dialog = dialogBuilder!!.create()
        dialog.show()
        saveButton!!.setOnClickListener { v ->
            if (!groceryItem!!.text.toString().isEmpty()
                && !quantity!!.text.toString().isEmpty()
            ) {
                saveGroceryToDB(v)
            }
        }
    }

    private fun saveGroceryToDB(v: View) {
        val grocery = Grocery()
        val newGrocery = groceryItem!!.text.toString()
        val newGroceryQuantity = quantity!!.text.toString()
        grocery.name = newGrocery
        grocery.quantity = newGroceryQuantity

        //Save to DB
        db!!.addGrocery(grocery)
        Snackbar.make(v, "Item Saved!", Snackbar.LENGTH_LONG).show()
        Handler().postDelayed({
            dialog!!.dismiss()
            //start a new activity
            startActivity(Intent(this@MainActivity, ListActivity::class.java))
        }, 1200) //  1 second.
    }

    fun byPassActivity() {
        //Checks if database is empty; if not, then we just
        //go to ListActivity and show all added items
        if (db!!.groceriesCount > 0) {
            startActivity(Intent(this@MainActivity, ListActivity::class.java))
            finish()
        }
    }
}