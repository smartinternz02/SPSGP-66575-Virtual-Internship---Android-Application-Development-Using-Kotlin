package com.harshaltawade.mygroapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

val glist : MutableList<Info> = mutableListOf()
class MainActivity : AppCompatActivity() {
//    val glist : MutableList<Info> = mutableListOf(Info("Apple","12"))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        val recyclerView = findViewById<RecyclerView>(R.id.rv)
        val etname = findViewById<EditText>(R.id.etname2)
        val etq = findViewById<EditText>(R.id.etq2)
        val btn2 = findViewById<Button>(R.id.btn3)
        val linlay = findViewById<LinearLayout>(R.id.LinLay)
        var gname:String
        var gqty:String

        fab.setOnClickListener{
            linlay.visibility = VISIBLE
            fab.visibility = INVISIBLE
            recyclerView.visibility = INVISIBLE
        }

        btn2.setOnClickListener{
            gname = etname.text.toString()
            gqty = etq.text.toString()
            linlay.visibility = INVISIBLE
            fab.visibility = VISIBLE
            recyclerView.visibility = VISIBLE
            if(gname=="" || gqty=="")
            {
                if(gname=="")
                {
                    Toast.makeText(applicationContext,"Please Enter valid grocery name!!!",Toast.LENGTH_SHORT).show()
                }
                if(gqty=="")
                {
                    Toast.makeText(applicationContext,"Please Enter valid grocery quantity!!!",Toast.LENGTH_SHORT).show()
                }

            }else
            {
                glist.add(Info(gname,gqty))
            }

            etname.setText("")
            etq.setText("")

            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = MyRecyclerViewAdapter(glist)
        }



        }




    }

