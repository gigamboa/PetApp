package com.gamboa.petapp

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    internal var pets: MutableList<Pet> = ArrayList()
    internal lateinit var adapter: FirebaseRecyclerAdapter<Pet, ItemViewHolder>

    internal var expandState = SparseBooleanArray()

    private var filePath: Uri? = null

    internal var storage:FirebaseStorage?=null
    internal var storageReference: StorageReference?=null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        recycler_data.setHasFixedSize(true)
        recycler_data.layoutManager = LinearLayoutManager(this)

        retrieveData()

        setData()

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            //addNewItemDialog()

            val addPet =  Intent(this@MainActivity, AddPet::class.java)
            startActivity(addPet)
        }

    }

    private fun setData() {
        val query = FirebaseDatabase.getInstance().reference.child("pet_item")
        val options = FirebaseRecyclerOptions.Builder<Pet>()
            .setQuery(query, Pet::class.java)
            .build()

        adapter = object : FirebaseRecyclerAdapter<Pet, ItemViewHolder>(options) {

//            override fun getItemViewType(position: Int): Int {
//                return if (pets[position].isExpandable)
//                    1
//                else
//                    0
//            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

                    val itemView = LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.pet_row, parent, false)
                    return ItemViewHolder(itemView, viewType == 1)
            }

            override fun onBindViewHolder(holder: ItemViewHolder, position: Int, model: Pet) {


                        holder.setIsRecyclable(false)
                        holder.txt_item_text.text = model.petName

                        holder.setItemClickListener(object : ItemClickListener {
                            override fun onClick(view: View, position: Int) {

                                Toast.makeText(this@MainActivity, "" + model.petName, Toast.LENGTH_SHORT).show()

                                val petDetail =  Intent(this@MainActivity, PetDetails::class.java)
                                petDetail.putExtra("petId", adapter.getRef(position).key)
                                petDetail.putExtra("petName", adapter.getItem(position).petName)
                                startActivity(petDetail)

                            }
                        })
            }

        }

        expandState.clear()
        for (i in pets.indices)
            expandState.append(i, false)
        recycler_data.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recycler_data.adapter = adapter
    }

    private fun retrieveData() {
        pets.clear()

        val db = FirebaseDatabase.getInstance()
            .reference
            .child("pet_item")

        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

                Log.d("ERROR", "" + p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {

                for (itemSnapshot in p0.children) {

                    val item = itemSnapshot.getValue(Pet::class.java)

                    pets.add(item!!)
                }
            }

        })


    }

    override fun onStart() {
        super.onStart()
        if (adapter != null)
            adapter.startListening()
    }

    override fun onStop() {
        if (adapter != null)
            adapter.stopListening()
        super.onStop()
    }





//    private fun addNewItemDialog() {
//
//
//        val alert = AlertDialog.Builder(this)
//        val itemEditText = EditText(this)
//        val image = ImageView(this)
//
//        alert.setMessage("Add New Item")
//        alert.setTitle("Enter To Do Item Text")
//        alert.setView(itemEditText)
//        alert.setView(image)
//
//        alert.setNeutralButton("Imagem") {dialog, neutralButton ->
//
//            val intent = Intent(Intent.ACTION_PICK)
//            intent.type = "image/*"
//            startActivityForResult(intent, 0)
//
//
//        }
//        alert.setPositiveButton("Submit") { dialog, positiveButton ->
//
//            val db = FirebaseDatabase.getInstance().getReference("pet_item")
//
//            val petItem = Pet.create()
//            petItem.petName = itemEditText.text.toString()
//            petItem.status = false
//            //We first make a push so that a new item is made with a unique ID
//            val newItem = db.push().key!!
//
//            petItem.petId = newItem
//
//            db.child(newItem).setValue(petItem).addOnCompleteListener{
//                Toast.makeText(applicationContext, "salvou", Toast.LENGTH_SHORT).show()
//
//
//            }
//            //then, we used the reference to set the value on that ID
//            dialog.dismiss()
//
//        }
//        alert.show()
//    }
//


}






