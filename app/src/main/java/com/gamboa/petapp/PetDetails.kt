package com.gamboa.petapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.lang.IllegalStateException

class PetDetails : AppCompatActivity() {


    internal var storage: FirebaseStorage?=null
    internal var storageReference: StorageReference?=null


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

        petName_display.text = intent.getStringExtra("petName")
        petStatus_display.text = intent.getStringExtra("petStatus")
        petAge_display.text = intent.getStringExtra("petAge")
        petType_display.text = intent.getStringExtra("petType")
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
    }

}