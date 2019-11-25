package com.gamboa.petapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.lang.IllegalStateException

class PetDetails : AppCompatActivity() {


    internal var storage: FirebaseStorage?=null
    internal var storageReference: StorageReference?=null
    internal lateinit var adapter: FirebaseRecyclerAdapter<Pet, ItemViewHolder>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_details)


        val petName_display = findViewById(com.gamboa.petapp.R.id.petName_display) as TextView
        val petStatus_display = findViewById(com.gamboa.petapp.R.id.petStatus_display) as TextView
        val petAge_display = findViewById(com.gamboa.petapp.R.id.petAge_display) as TextView
        val petType_display = findViewById(com.gamboa.petapp.R.id.petType_display) as TextView
        val petImage_display = findViewById(R.id.petPhoto_display) as ImageView

        val navBarTitle = "Mais detalhes"
        supportActionBar?.title = navBarTitle

        var petName = intent.getStringExtra("petName")
        var petAge = intent.getStringExtra("petAge")
        var petType = intent.getStringExtra("petType")
        var petStatus = intent.getStringExtra("petStatus")
        var petId = intent.getStringExtra("petId")

        petName_display.text = petName
        petStatus_display.text = petAge
        petAge_display.text = petType
        petType_display.text = petStatus

        var imageUrl = intent.getStringExtra("petImage")

        Picasso.with(this).load(imageUrl).into(petImage_display)


        val deleteBtn = findViewById(R.id.delete_btn) as Button
        deleteBtn.setOnClickListener { view ->

            val db = FirebaseDatabase.getInstance().getReference("pet_item")

            val petItemCurrent = intent.getStringExtra("petId")

            db.child(petItemCurrent).removeValue()


            val intent =  Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val editBtn = findViewById(R.id.edit_btn) as Button
        editBtn.setOnClickListener { view ->

            val editPet =  Intent(this, AddPet::class.java)
            editPet.putExtra("CallerId", "editPet")
            editPet.putExtra("petId", petId)
            editPet.putExtra("petName", petName)
            editPet.putExtra("petAge", petAge)
            editPet.putExtra("petStatus", petStatus)
            editPet.putExtra("petType", petType)
            startActivity(editPet)
        }
    }

}