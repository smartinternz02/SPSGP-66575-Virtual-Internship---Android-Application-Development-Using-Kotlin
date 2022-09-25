package com.example.mygrocerylist.UI

import android.app.AlertDialog
import android.content.Context
import com.example.mygrocerylist.Model.Grocery.name
import com.example.mygrocerylist.Model.Grocery.quantity
import com.example.mygrocerylist.Model.Grocery.dateItemAdded
import com.example.mygrocerylist.Model.Grocery.id
import com.example.mygrocerylist.Data.DatabaseHandler.deleteGrocery
import com.example.mygrocerylist.Data.DatabaseHandler.updateGrocery
import com.example.mygrocerylist.Model.Grocery
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mygrocerylist.R
import android.widget.TextView
import com.example.mygrocerylist.Data.DatabaseHandler
import android.widget.EditText
import android.support.design.widget.Snackbar
import android.content.Intent
import android.view.View
import android.widget.Button
import com.example.mygrocerylist.Activities.DetailsActivity

class RecyclerViewAdapter(private val context: Context, private val groceryItems: List<Grocery>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    private var alertDialogBuilder: AlertDialog.Builder? = null
    private var dialog: AlertDialog? = null
    private var inflater: LayoutInflater? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_row, parent, false)
        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val grocery = groceryItems[position]
        holder.groceryItemName.text = grocery.name
        holder.quantity.text = grocery.quantity
        holder.dateAdded.text = grocery.dateItemAdded
    }

    override fun getItemCount(): Int {
        return groceryItems.size
    }

    inner class ViewHolder(view: View, ctx: Context) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        var groceryItemName: TextView
        var quantity: TextView
        var dateAdded: TextView
        var editButton: Button
        var deleteButton: Button
        var id = 0
        override fun onClick(v: View) {
            when (v.id) {
                R.id.editButton -> {
                    val position = adapterPosition
                    val grocery = groceryItems[position]
                    editItem(grocery)
                }
                R.id.deleteButton -> {
                    position = adapterPosition
                    grocery = groceryItems[position]
                    deleteItem(grocery.id)
                }
            }
        }

        fun deleteItem(id: Int) {
            //create an alertDialog
            alertDialogBuilder = AlertDialog.Builder(context)
            inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.confirmation_dialog, null)
            val noButton = view.findViewById<View>(R.id.noButton) as Button
            val yesButton = view.findViewById<View>(R.id.yesButton) as Button
            alertDialogBuilder!!.setView(view)
            dialog = alertDialogBuilder!!.create()
            dialog.show()
            noButton.setOnClickListener { dialog.dismiss() }
            yesButton.setOnClickListener { //delete the item
                val db = DatabaseHandler(context)
                db.deleteGrocery(id)
                groceryItems.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
                dialog.dismiss()
            }
        }

        fun editItem(grocery: Grocery) {
            alertDialogBuilder = AlertDialog.Builder(context)
            inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.popup, null)
            val groceryItem = view.findViewById<View>(R.id.groceryItem) as EditText
            val quantity = view.findViewById<View>(R.id.groceryQty) as EditText
            val title = view.findViewById<View>(R.id.tile) as TextView
            title.text = "Edit Grocery"
            val saveButton = view.findViewById<View>(R.id.saveButton) as Button
            alertDialogBuilder!!.setView(view)
            dialog = alertDialogBuilder!!.create()
            dialog.show()
            saveButton.setOnClickListener {
                val db = DatabaseHandler(context)
                //Update item
                grocery.name = groceryItem.text.toString()
                grocery.quantity = quantity.text.toString()
                if (!groceryItem.text.toString().isEmpty() &&
                    !quantity.text.toString().isEmpty()
                ) {
                    db.updateGrocery(grocery)
                    notifyItemChanged(adapterPosition, grocery)
                } else {
                    Snackbar.make(view, "Add Grocery and Quantity", Snackbar.LENGTH_LONG).show()
                }
                dialog.dismiss()
            }
        }

        init {
            context = ctx
            groceryItemName = view.findViewById<View>(R.id.name) as TextView
            quantity = view.findViewById<View>(R.id.quantity) as TextView
            dateAdded = view.findViewById<View>(R.id.dateAdded) as TextView
            editButton = view.findViewById<View>(R.id.editButton) as Button
            deleteButton = view.findViewById<View>(R.id.deleteButton) as Button
            editButton.setOnClickListener(this)
            deleteButton.setOnClickListener(this)
            view.setOnClickListener { //go to next screen / Details activity
                val position = adapterPosition
                val grocery = groceryItems[position]
                val intent = Intent(context, DetailsActivity::class.java)
                intent.putExtra("name", grocery.name)
                intent.putExtra("quantity", grocery.quantity)
                intent.putExtra("id", grocery.id)
                intent.putExtra("date", grocery.dateItemAdded)
                context.startActivity(intent)
            }
        }
    }
}