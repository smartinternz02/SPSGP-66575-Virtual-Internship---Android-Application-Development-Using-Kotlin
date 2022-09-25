package com.example.mygrocerylist.Activities

import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle
import android.view.View
import com.example.mygrocerylist.R

class DetailsActivity : AppCompatActivity() {
    private var itemName: TextView? = null
    private var quantity: TextView? = null
    private var dateAdded: TextView? = null
    private var groceryId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        itemName = findViewById<View>(R.id.itemNameDet) as TextView
        quantity = findViewById<View>(R.id.quantityDet) as TextView
        dateAdded = findViewById<View>(R.id.dateAddedDet) as TextView
        val bundle = intent.extras
        if (bundle != null) {
            itemName!!.text = bundle.getString("name")
            quantity!!.text = bundle.getString("quantity")
            dateAdded!!.text = bundle.getString("date")
            groceryId = bundle.getInt("id")
        }
    }
}