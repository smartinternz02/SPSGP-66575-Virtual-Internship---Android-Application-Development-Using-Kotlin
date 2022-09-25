package com.example.mygrocerylist.Data

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import com.example.mygrocerylist.Model.Grocery
import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.mygrocerylist.Util.Constants
import java.text.DateFormat
import java.util.*

class DatabaseHandler(private val ctx: Context?) : SQLiteOpenHelper(
    ctx, Constants.DB_NAME, null, Constants.DB_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_GROCERY_TABLE = ("CREATE TABLE "
                + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY,"
                + Constants.KEY_GROCERY_ITEM + " TEXT,"
                + Constants.KEY_QTY_NUMBER + " TEXT,"
                + Constants.KEY_DATE_NAME + " LONG);")
        db.execSQL(CREATE_GROCERY_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME)
        onCreate(db)
    }

    //Add Grocery
    fun addGrocery(grocery: Grocery) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(Constants.KEY_GROCERY_ITEM, grocery.name)
        values.put(Constants.KEY_QTY_NUMBER, grocery.quantity)
        values.put(Constants.KEY_DATE_NAME, System.currentTimeMillis())

        //Insert the row
        db.insert(Constants.TABLE_NAME, null, values)
        Log.d("Saved!!", "Saved to DB")
    }

    //Get a grocery
    fun getGrocery(id: Int): Grocery {
        val db = this.writableDatabase
        val cursor = db.query(
            Constants.TABLE_NAME, arrayOf(
                Constants.KEY_ID,
                Constants.KEY_GROCERY_ITEM, Constants.KEY_QTY_NUMBER, Constants.KEY_DATE_NAME
            ),
            Constants.KEY_ID + "=?", arrayOf(id.toString()), null, null, null, null
        )
        cursor?.moveToFirst()
        val grocery = Grocery()
        grocery.id =
            cursor!!.getString(cursor.getColumnIndex(Constants.KEY_ID))
                .toInt()
        grocery.name =
            cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM))
        grocery.quantity =
            cursor.getString(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER))

        //convert timestamp to something readable
        val dateFormat = DateFormat.getDateInstance()
        val formatedDate = dateFormat.format(
            Date(
                cursor.getLong(
                    cursor.getColumnIndex(Constants.KEY_DATE_NAME)
                )
            )
                .time
        )
        grocery.dateItemAdded = formatedDate
        return grocery
    }//convert timestamp to something readable

    // Add to the groceryList
    //Get all groceries
    val allGroceries: List<Grocery>
        get() {
            val db = this.readableDatabase
            val groceryList: MutableList<Grocery> = ArrayList()
            val cursor = db.query(
                Constants.TABLE_NAME, arrayOf(
                    Constants.KEY_ID, Constants.KEY_GROCERY_ITEM, Constants.KEY_QTY_NUMBER,
                    Constants.KEY_DATE_NAME
                ), null, null, null, null, Constants.KEY_DATE_NAME + " DESC"
            )
            if (cursor.moveToFirst()) {
                do {
                    val grocery = Grocery()
                    grocery.id = cursor.getString(cursor.getColumnIndex(Constants.KEY_ID)).toInt()
                    grocery.name =
                        cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM))
                    grocery.quantity =
                        cursor.getString(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER))

                    //convert timestamp to something readable
                    val dateFormat = DateFormat.getDateInstance()
                    val formatedDate = dateFormat.format(
                        Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME)))
                            .time
                    )
                    grocery.dateItemAdded = formatedDate

                    // Add to the groceryList
                    groceryList.add(grocery)
                } while (cursor.moveToNext())
            }
            return groceryList
        }

    //Updated Grocery
    fun updateGrocery(grocery: Grocery): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(Constants.KEY_GROCERY_ITEM, grocery.name)
        values.put(Constants.KEY_QTY_NUMBER, grocery.quantity)
        values.put(Constants.KEY_DATE_NAME, System.currentTimeMillis()) //get system time
        return db.update(
            Constants.TABLE_NAME,
            values,
            Constants.KEY_ID + "=?",
            arrayOf(grocery.id.toString())
        )
    }

    //Delete Grocery
    fun deleteGrocery(id: Int) {
        val db = this.writableDatabase
        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + " = ?", arrayOf(id.toString()))
        db.close()
    }

    //Get Count
    val groceriesCount: Int
        get() {
            val countQuery = "SELECT * FROM " + Constants.TABLE_NAME
            val db = this.readableDatabase
            val cursor = db.rawQuery(countQuery, null)
            return cursor.count
        }
}