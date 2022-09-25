package com.example.mygrocerylist.Activities

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import com.example.mygrocerylist.UI.RecyclerViewAdapter
import com.example.mygrocerylist.Model.Grocery
import com.example.mygrocerylist.Data.DatabaseHandler
import android.os.Bundle
import com.example.mygrocerylist.R
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.widget.EditText
import android.support.design.widget.Snackbar
import android.content.Intent
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import com.example.mygrocerylist.Activities.MainActivity
import java.util.ArrayList

class ListActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var recyclerViewAdapter: RecyclerViewAdapter? = null
    private var groceryList: List<Grocery>? = null
    private var listItems: MutableList<Grocery>? = null
    private var db: DatabaseHandler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            createPopupDialog()
        }
        db = DatabaseHandler(this)
        recyclerView = findViewById<View>(R.id.recyclerViewID) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        groceryList = ArrayList()
        listItems = ArrayList()

        // Get items from database
        groceryList = db!!.allGroceries
        for (c in groceryList!!) {
            val grocery = Grocery()
            grocery.name = c.name
            grocery.quantity = "Qty: " + c.quantity
            grocery.id = c.id
            grocery.dateItemAdded = "Added on: " + c.dateItemAdded
            (listItems as ArrayList<Grocery>).add(grocery)
        }
        recyclerViewAdapter = RecyclerViewAdapter(this, listItems as ArrayList<Grocery>)
        recyclerView!!.adapter = recyclerViewAdapter
        recyclerViewAdapter!!.notifyDataSetChanged()
    }

    private var dialogBuilder: AlertDialog.Builder? = null
    private lateinit var dialog: AlertDialog
    private var groceryItem: EditText? = null
    private var quantity: EditText? = null
    private var saveButton: Button? = null

    //  private DatabaseHandler db;
    private fun createPopupDialog() {
        dialogBuilder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.popup, null)
        groceryItem = view.findViewById<View>(R.id.groceryItem) as EditText
        quantity = view.findViewById<View>(R.id.groceryQty) as EditText
        saveButton = view.findViewById<View>(R.id.saveButton) as Button
        dialogBuilder!!.setView(view)
        dialog = dialogBuilder!!.create()
        dialog.show()
        saveButton!!.setOnClickListener { v -> //Todo: Save to db
            //Todo: Go to next screen
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

        // Log.d("Item Added ID:", String.valueOf(db.getGroceriesCount()));
        Handler().postDelayed({
            dialog!!.dismiss()
            //start a new activity
            startActivity(Intent(this@ListActivity, MainActivity::class.java))
        }, 1200) //  1 second.
    }
}